cat > src/controller/LoginController.java << 'EOF'
package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.Main;
import src.model.Customer;

public class LoginController {
    
    @FXML private TextField customerIdField;
    @FXML private Label statusLabel;
    
    @FXML
    private void handleLogin() {
        String customerId = customerIdField.getText().trim();
        
        if (customerId.isEmpty()) {
            statusLabel.setText("Please enter Customer ID");
            return;
        }
        
        Customer customer = Main.getBank().findCustomerById(customerId);
        if (customer != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/view/dashboard.fxml"));
                Parent root = loader.load();
                
                Stage stage = (Stage) customerIdField.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));
                
            } catch (Exception e) {
                statusLabel.setText("Error loading dashboard");
            }
        } else {
            statusLabel.setText("Customer not found. Try: C001 or C002");
        }
    }
}
EOF