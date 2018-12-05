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
import javafx.event.ActionEvent;
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
import simpleinvoice.model.Product;
import simpleinvoice.repository.ProductRepository;

/**
 * FXML Controller class
 *
 * @author programmer
 */
public class ProductsController implements Initializable {

    @FXML
    private AnchorPane rootNode;
    @FXML
    private TextField searchTextField;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private AnchorPane apCombobox;
    @FXML
    private JFXButton addProductButton;
    @FXML
    private JFXButton updateButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product,Integer> productIDColumn;
    @FXML
    private TableColumn<Product,String> productNameColumn;
    @FXML
    private TableColumn<Product,Integer> qtyColoumn;
    @FXML
    private TableColumn<Product,Float> priceColoumn;
    @FXML
    private TableColumn<Product,Float> gstColoumn;
    @FXML
    private TableColumn<Product,Integer> hsnColoumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        productTableView.getItems().clear();
//        ObservableList<Product> products = FXCollections.observableArrayList();
//        products.add(new Product(1,"Nikhil",4,2.5f,15f,123312));
//        products.add(new Product(1,"Nikhil",4,2.5f,15f,123312));
//        products.add(new Product(1,"Nikhil",4,2.5f,15f,123312));
//        products.add(new Product(1,"Nikhil",4,2.5f,15f,123312));
//        products.add(new Product(1,"Nikhil",4,2.5f,15f,123312));
//        productTableView.setItems(products);
        searchTextField.setOnKeyReleased(this::search);
        refreshButton.setOnMouseClicked(e -> {
            try {
                ProductRepository.getProductRepository().refresh();
            } catch (SQLException ex) {
                Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        updateButton.disableProperty().bind(productTableView.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(productTableView.getSelectionModel().selectedItemProperty().isNull());
        initTableCellValueFactory();
        addProductButton.setOnMouseClicked(this::AddProduct);
        deleteButton.setOnMouseClicked(this::deleteProduct);
        updateButton.setOnMouseClicked(this::updateProduct);
        productTableView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                viewProduct(e);
            }
        });
        try {
            productTableView.itemsProperty().set(ProductRepository.getProductRepository().getProducts());
        } catch (SQLException ex) {
            System.out.println("Cannot link products with repository");
        }
    }

    private void AddProduct(MouseEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/simpleinvoice/view/addProduct.fxml"));
        AnchorPane node = null;
        try {
            node = loader.load();
        } catch (IOException ex) {
            System.out.println("Cannot Load addSubject.fxml file");
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(node));
        stage.show();
    }

    private void deleteProduct(MouseEvent e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do You Want to Delete?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            try {
                ProductRepository.getProductRepository().deleteProduct(productTableView.getSelectionModel().getSelectedItem());
//                TrayNotification n = new TrayNotification("Success", "Course Deleted SuccessFully", NotificationType.SUCCESS);
//                        n.setAnimationType(AnimationType.POPUP);
//                        n.showAndDismiss(Duration.seconds(3));
            } catch (SQLException ex) {
                Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void updateProduct(MouseEvent e) {
        Stage stage = new Stage();
        stage.centerOnScreen();
        AnchorPane pane = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/simpleinvoice/view/addProduct.fxml"));
        try {
            pane = loader.load();
        } catch (IOException ex) {
            System.err.println("Cannot Load Update Product Window");
        }
        AddProductController c = loader.getController();
        c.initProduct(productTableView.getSelectionModel().getSelectedItem());
        stage.setScene(new Scene(pane));
        stage.showAndWait();
        productTableView.getSelectionModel().selectNext();
    }
    private void viewProduct(MouseEvent e) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/simpleinvoice/view/viewProduct.fxml"));
        AnchorPane pane = null;
        try {
            pane = loader.load();
        } catch (IOException ex) {
            System.err.println("Cannot Load View Product Window\n"+ex);
        }
        ViewProductController controller = loader.getController();
        controller.setProduct(productTableView.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        stage.setScene(new Scene(pane));
        stage.showAndWait();
    }
    
    private void initTableCellValueFactory() {
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        qtyColoumn.setCellValueFactory(new PropertyValueFactory<>("qtyAvailable"));
        priceColoumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        gstColoumn.setCellValueFactory(new PropertyValueFactory<>("gstRate"));
        hsnColoumn.setCellValueFactory(new PropertyValueFactory<>("hsnNumber"));
        
    }

    private void search(KeyEvent e) {
        FilteredList<Product> filter;
        filter = new FilteredList<>(productTableView.getItems(), p -> true);
        searchTextField.textProperty().addListener((ob, o, n) -> {
            filter.setPredicate(p -> {
                if (n.isEmpty() || n == null) {
                    return true;
                } else if (String.valueOf(p.getProductID()).contains(n.toLowerCase())) {
                    return true;
                } else if (p.getName().toLowerCase().contains(n.toLowerCase())) {
                    return true;
                } else {
                    return false;
                }
            });
            SortedList sort = new SortedList(filter);
            sort.comparatorProperty().bind(productTableView.comparatorProperty());
            productTableView.setItems(sort);
        });
    }    
    
}
