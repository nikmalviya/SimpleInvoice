/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleinvoice.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import simpleinvoice.model.Customer;
import simpleinvoice.repository.CustomerRepository;

/**
 * FXML Controller class
 *
 * @author programmer
 */
public class AddCustomerController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private JFXTextField tfCustName;
    @FXML
    private JFXTextField tfPhone;
    @FXML
    private JFXTextField tfGST;
    @FXML
    private JFXTextArea tfAddress;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnAddCustomer;
    private final BooleanProperty isUpdateMode = new SimpleBooleanProperty(false);
    private Customer old;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        isUpdateMode.addListener((ob, o, n) -> {
            if (n) {
                titleLabel.setText("Update Customer");
                btnAddCustomer.setText("Update Customer");
            } else {
                titleLabel.setText("Add Customer");
                btnAddCustomer.setText("Add Customer");
            }
        });
        btnAddCustomer.setOnMouseClicked(this::addCustomer);
        btnCancel.setOnMouseClicked(this::closeWindow);
    }

    private void closeWindow(MouseEvent e) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void addCustomer(MouseEvent e) {
        try {
            if (isUpdateMode.get()) {
                CustomerRepository.getCustomerRepository().updateCustomer(old, new Customer(
                        tfCustName.getText().trim(),
                        tfPhone.getText().trim(),
                        tfAddress.getText().trim(),
                        tfGST.getText().trim()
                ));
            } else {
                CustomerRepository.getCustomerRepository().addNewCustomer(new Customer(
                        tfCustName.getText().trim(),
                        tfPhone.getText().trim(),
                        tfAddress.getText().trim(),
                        tfGST.getText().trim()
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Cannot add or update Subject ...\n"+ex);
        }
        closeWindow(e);
    }

    public void initCustomer(Customer customer) {
        old = customer;
        tfCustName.setText(customer.getCustomerName());
        tfPhone.setText(customer.getContactNumber());
        tfAddress.setText(customer.getAddress());
        tfGST.setText(customer.getPartyGSTNo());
        isUpdateMode.set(true);
    }
}
