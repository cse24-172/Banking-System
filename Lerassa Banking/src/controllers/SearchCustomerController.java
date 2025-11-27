package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import database.Database;
import model.Customer;
import util.SceneSwitcher;

import java.sql.*;
import javafx.stage.Stage;

public class SearchCustomerController {

    @FXML private TextField searchField;
    @FXML private Button searchBtn;
    @FXML private Button backBtn;

    @FXML private TableView<Customer> resultsTable;
    @FXML private TableColumn<Customer, Integer> idCol;
    @FXML private TableColumn<Customer, String> nameCol;
    @FXML private TableColumn<Customer, String> phoneCol;
    @FXML private TableColumn<Customer, String> emailCol;
    @FXML private TableColumn<Customer, String> addressCol;

    @FXML
    public void initialize() {

        // Table Bindings
        idCol.setCellValueFactory(v -> v.getValue().idProperty().asObject());
        nameCol.setCellValueFactory(v -> v.getValue().fullNameProperty());
        phoneCol.setCellValueFactory(v -> v.getValue().phoneProperty());
        emailCol.setCellValueFactory(v -> v.getValue().emailProperty());
        addressCol.setCellValueFactory(v -> v.getValue().addressProperty());

        searchBtn.setOnAction(e -> search());
        backBtn.setOnAction(e -> goBack());
    }

    private void search() {
        String text = searchField.getText().trim();
        ObservableList<Customer> list = FXCollections.observableArrayList();

        if (text.isEmpty()) return;

        try (Connection conn = Database.connect()) {

            PreparedStatement ps = conn.prepareStatement("""
                SELECT * FROM customers
                WHERE fullName LIKE ? 
                OR surname LIKE ?
                OR phone LIKE ?
                OR email LIKE ?
                OR idNumber LIKE ?
            """);

            String query = "%" + text + "%";

            ps.setString(1, query);
            ps.setString(2, query);
            ps.setString(3, query);
            ps.setString(4, query);
            ps.setString(5, query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("fullName"),
                        rs.getString("surname"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("idNumber"),
                        rs.getString("dob")
                ));
            }

            resultsTable.setItems(list);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void goBack() {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        SceneSwitcher.switchTo(stage, "admin_dashboard.fxml");
    }
}
