/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleinvoice.controller;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import simpleinvoice.model.Customer;
import simpleinvoice.repository.CustomerRepository;

/**
 * FXML Controller class
 *
 * @author programmer
 */
public class CustomersController implements Initializable {

    @FXML
    private AnchorPane rootNode;
    @FXML
    private TextField searchTextField;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private JFXButton addCustomerButton;
    @FXML
    private JFXButton updateButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private TableView<Customer> custTableView;
    @FXML
    private TableColumn<Customer,Integer> custIDColumn;
    @FXML
    private TableColumn<Customer,String> custNameColumn;
    @FXML
    private TableColumn<Customer,String> contactNoColoumn;
    @FXML
    private TableColumn<Customer,String> addressColoumn;
    @FXML
    private TableColumn<Customer,String> gstnColoumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {       
        searchTextField.setOnKeyReleased(this::search);
        refreshButton.setOnMouseClicked(e -> {
            try {
                CustomerRepository.getCustomerRepository().refresh();
            } catch (SQLException ex) {
                Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        updateButton.disableProperty().bind(custTableView.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(custTableView.getSelectionModel().selectedItemProperty().isNull());
        initTableCellValueFactory();
        addCustomerButton.setOnMouseClicked(this::AddCustomer);
        deleteButton.setOnMouseClicked(this::deleteCustomer);
        updateButton.setOnMouseClicked(this::updateCustomer);
        custTableView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                viewCustomer(e);
            }
        });
        try {
            custTableView.itemsProperty().set(CustomerRepository.getCustomerRepository().getCustomers());
        } catch (SQLException ex) {
            System.out.println("Cannot link customers with repository");
        }
    }

    private void AddCustomer(MouseEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/simpleinvoice/view/addCustomer.fxml"));
        AnchorPane node = null;
        try {
            node = loader.load();
        } catch (IOException ex) {
            System.out.println("Cannot Load addCustomer.fxml file");
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(node));
        stage.show();
    }

    private void deleteCustomer(MouseEvent e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do You Want to Delete?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            try {
                CustomerRepository.getCustomerRepository().deleteCustomer(custTableView.getSelectionModel().getSelectedItem());
//                TrayNotification n = new TrayNotification("Success", "Course Deleted SuccessFully", NotificationType.SUCCESS);
//                        n.setAnimationType(AnimationType.POPUP);
//                        n.showAndDismiss(Duration.seconds(3));
            } catch (SQLException ex) {
                Logger.getLogger(CustomersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void updateCustomer(MouseEvent e) {
        Stage stage = new Stage();
        stage.centerOnScreen();
        AnchorPane pane = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/simpleinvoice/view/addCustomer.fxml"));
        try {
            pane = loader.load();
        } catch (IOException ex) {
            System.err.println("Cannot Load Update Customer Window");
        }
        AddCustomerController c = loader.getController();
        c.initCustomer(custTableView.getSelectionModel().getSelectedItem());
        stage.setScene(new Scene(pane));
        stage.showAndWait();
        custTableView.getSelectionModel().selectNext();
    }
    private void viewCustomer(MouseEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/simpleinvoice/view/viewCustomer.fxml"));
        AnchorPane pane = null;
        try {
            pane = loader.load();
        } catch (IOException ex) {
            System.err.println("Cannot Load View Customer Window\n"+ex);
        }
//        ViewCustomerController controller = loader.getController();
//        controller.setCustomer(custTableView.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        stage.setScene(new Scene(pane));
        stage.showAndWait();
    }
    
    private void initTableCellValueFactory() {
        custIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        custNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        contactNoColoumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));        
        addressColoumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        gstnColoumn.setCellValueFactory(new PropertyValueFactory<>("partyGSTNo"));
        
    }

    private void search(KeyEvent e) {
        FilteredList<Customer> filter;
        filter = new FilteredList<>(custTableView.getItems(), p -> true);
        searchTextField.textProperty().addListener((ob, o, n) -> {
            filter.setPredicate(p -> {
                if (n.isEmpty() || n == null) {
                    return true;
                } else if (String.valueOf(p.getCustomerID()).contains(n.toLowerCase())) {
                    return true;
                } else if (p.getCustomerName().toLowerCase().contains(n.toLowerCase())) {
                    return true;
                } else if (p.getContactNumber().toLowerCase().contains(n.toLowerCase())) {
                    return true;
                } else {
                    return false;
                }
            });
            SortedList sort = new SortedList(filter);
            sort.comparatorProperty().bind(custTableView.comparatorProperty());
            custTableView.setItems(sort);
        });
    }    
    
}
