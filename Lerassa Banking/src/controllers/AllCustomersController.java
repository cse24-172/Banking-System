package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import model.Customer;
import database.Database;
import util.SceneSwitcher;

import java.sql.*;
import javafx.stage.Stage;

public class AllCustomersController {

    @FXML private TableView<Customer> customersTable;

    @FXML private TableColumn<Customer, Integer> idCol;
    @FXML private TableColumn<Customer, String> titleCol;
    @FXML private TableColumn<Customer, String> fullNameCol;
    @FXML private TableColumn<Customer, String> surnameCol;
    @FXML private TableColumn<Customer, String> phoneCol;
    @FXML private TableColumn<Customer, String> emailCol;
    @FXML private TableColumn<Customer, String> addressCol;
    @FXML private TableColumn<Customer, String> idNumberCol;
    @FXML private TableColumn<Customer, String> dobCol;

    @FXML private Button backBtn;

    @FXML
    public void initialize() {

        // ---- TABLE BINDINGS ----
        idCol.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        titleCol.setCellValueFactory(c -> c.getValue().titleProperty());
        fullNameCol.setCellValueFactory(c -> c.getValue().fullNameProperty());
        surnameCol.setCellValueFactory(c -> c.getValue().surnameProperty());
        phoneCol.setCellValueFactory(c -> c.getValue().phoneProperty());
        emailCol.setCellValueFactory(c -> c.getValue().emailProperty());
        addressCol.setCellValueFactory(c -> c.getValue().addressProperty());
        idNumberCol.setCellValueFactory(c -> c.getValue().idNumberProperty());
        dobCol.setCellValueFactory(c -> c.getValue().dobProperty());

        // Load customers from DB
        loadCustomers();

        // Back button
        backBtn.setOnAction(e -> goBack());
    }

    private void loadCustomers() {
        ObservableList<Customer> list = FXCollections.observableArrayList();

        try (Connection conn = Database.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM customers")) {

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

            customersTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goBack() {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        SceneSwitcher.switchTo(stage, "admin_dashboard.fxml");
    }
}
