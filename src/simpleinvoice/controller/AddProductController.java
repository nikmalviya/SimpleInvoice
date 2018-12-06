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
import simpleinvoice.model.Product;
import simpleinvoice.repository.ProductRepository;

public class AddProductController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private JFXTextArea tfProductName;
    @FXML
    private JFXTextField tfQty;
    @FXML
    private JFXTextField tfGST;
    @FXML
    private JFXTextField tfHSN;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnAddProduct;

    private final BooleanProperty isUpdateMode = new SimpleBooleanProperty(false);
    private Product old;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        isUpdateMode.addListener((ob, o, n) -> {
            if (n) {
                titleLabel.setText("Update Product");
                btnAddProduct.setText("Update Product");
            } else {
                titleLabel.setText("Add Product");
                btnAddProduct.setText("Add Product");
            }
        });
        btnAddProduct.setOnMouseClicked(this::addProduct);
        btnCancel.setOnMouseClicked(this::closeWindow);
    }

    private void closeWindow(MouseEvent e) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void addProduct(MouseEvent e) {
        try {
            if (isUpdateMode.get()) {
                ProductRepository.getProductRepository().updateProduct(old, new Product(
                        tfProductName.getText().trim(),
                        Integer.parseInt(tfQty.getText().trim()),
                        Float.parseFloat(tfGST.getText().trim()),
                        Integer.parseInt(tfHSN.getText().trim())
                ));
            } else {
                ProductRepository.getProductRepository().addNewProduct(new Product(
                        tfProductName.getText().trim(),
                        Integer.parseInt(tfQty.getText().trim()),
                        Float.parseFloat(tfGST.getText().trim()),
                        Integer.parseInt(tfHSN.getText().trim())
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Cannot add or update Subject ...\n"+ex);
        }
        closeWindow(e);
    }

    public void initProduct(Product product) {
        old = product;
        tfProductName.setText(product.getName());
        tfQty.setText(String.valueOf(product.getQtyAvailable()));
        tfGST.setText(String.valueOf(product.getGstRate()));
        tfHSN.setText(String.valueOf(product.getHsnNumber()));
        isUpdateMode.set(true);
    }  
    
}
