
package simpleinvoice.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import simpleinvoice.model.Product;

public class ViewProductController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private Label lblProductID;
    @FXML
    private Label lblProductName;
    @FXML
    private Label lblQty;
    @FXML
    private Label lblPrice;
    @FXML
    private Label lblGst;
    @FXML
    private Label lblHSN;
    @FXML
    private JFXButton btnOk;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnOk.setOnAction(this::okClicked);
    }
    public void setProduct(Product product) {
        lblProductID.setText(String.valueOf(product.getProductID()));
        lblProductName.setText(product.getName());
        lblQty.setText(String.valueOf(product.getQtyAvailable()));
        NumberFormat india = NumberFormat.getCurrencyInstance(new Locale("en","IN"));
        lblPrice.setText(india.format(product.getPrice()));
        lblGst.setText(String.valueOf(product.getGstRate()));
        lblHSN.setText(String.valueOf(product.getHsnNumber()));
    }
    private void okClicked(ActionEvent e){
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
    }
    
    
}
