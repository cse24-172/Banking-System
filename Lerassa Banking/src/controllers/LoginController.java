package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import util.SceneSwitcher;
import session.Session;
import database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginBtn;

    @FXML
    public void initialize() {
        loginBtn.setOnAction(this::login);
    }

    private void login(ActionEvent e) {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Fill all fields");
            return;
        }

        try (Connection conn = Database.connect()) {

            if (conn == null) {
                errorLabel.setText("Database connection failed.");
                return;
            }

            // 1️⃣ Check ADMIN login (users table)
            PreparedStatement adminQuery = conn.prepareStatement(
                "SELECT id, role FROM users WHERE username=? AND password=?"
            );

            adminQuery.setString(1, user);
            adminQuery.setString(2, pass);

            ResultSet adminResult = adminQuery.executeQuery();

            if (adminResult.next()) {
                // Save admin session
                Session.username = user;
                Session.role = adminResult.getString("role");
                Session.customerId = adminResult.getInt("id");

                SceneSwitcher.switchTo(e, "admin_dashboard.fxml");
                return;
            }

            // 2️⃣ If not admin → Check CUSTOMER login (customers table)
            PreparedStatement customerQuery = conn.prepareStatement(
                "SELECT id, fullName FROM customers WHERE username=? AND password=?"
            );

            customerQuery.setString(1, user);
            customerQuery.setString(2, pass);

            ResultSet customerResult = customerQuery.executeQuery();

            if (customerResult.next()) {

                int customerId = customerResult.getInt("id");
                String fullName = customerResult.getString("fullName");

                // Save session
                Session.username = fullName;
                Session.role = "customer";
                Session.customerId = customerId;

                SceneSwitcher.switchTo(e, "customer_dashboard.fxml");
                return;
            }

            // 3️⃣ Invalid Login
            errorLabel.setText("Invalid username or password");

        } catch (Exception ex) {
            errorLabel.setText("Error connecting to database");
            ex.printStackTrace();
        }
    }
}
