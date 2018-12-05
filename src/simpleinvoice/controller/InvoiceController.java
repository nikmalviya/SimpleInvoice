/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleinvoice.controller;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import simpleinvoice.model.InvoiceItem;
import simpleinvoice.model.Product;
import simpleinvoice.repository.ProductRepository;

/**
 * FXML Controller class
 *
 * @author programmer
 */
public class InvoiceController implements Initializable {

    @FXML
    private DatePicker dpInvoiceDate;
    @FXML
    private TextField tfCustName;
    @FXML
    private TextField tfSubTotal;
    @FXML
    private TextField tfDiscount;
    @FXML
    private TextField tfNetAmount;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnRemove;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private CheckBox cbPrintOnSave;
    @FXML
    private TextField tfProduct;
    @FXML
    private TextField tfRate;
    @FXML
    private TextField tfQty;
    @FXML
    private TextField tfAmt;
    @FXML
    private Button btnAdd;
    @FXML
    private TableView<InvoiceItem> tblvProducts;
    @FXML
    private TableColumn<InvoiceItem,?> tblcSrNo;
    @FXML
    private TableColumn<InvoiceItem,?> tblcProductDes;
    @FXML
    private TableColumn<InvoiceItem,?> tblcHsnNo;
    @FXML
    private TableColumn<InvoiceItem,?> tblcGstRate;
    @FXML
    private TableColumn<InvoiceItem,?> tblcQuantity;
    @FXML
    private TableColumn<InvoiceItem,?> tblcRate;
    @FXML
    private TableColumn<InvoiceItem,?> tblcAmount;
    @FXML
    private TableColumn<InvoiceItem, ?> tblcCgstRate;
    @FXML
    private TableColumn<InvoiceItem, ?> tblcSgstRate;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            TextFields.bindAutoCompletion(tfProduct,ProductRepository.getProductRepository().getProductNameList());
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tfQty.setText("0");
        tfRate.setText("0");
        tfQty.setOnKeyReleased(e-> calculateAmount());
        tfRate.setOnKeyReleased(e-> calculateAmount());
    }
    private void calculateAmount() {
        int rate = Integer.parseInt("".equals(tfRate.getText())?"0":tfRate.getText());
        int qty = Integer.parseInt("".equals(tfQty.getText())?"0":tfQty.getText());
        tfAmt.setText(String.valueOf(rate*qty));
    }
    private void initTableColumns(){
        tblcSrNo.setCellValueFactory(new PropertyValueFactory<>("srno"));
        tblcProductDes.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tblcHsnNo.setCellValueFactory(new PropertyValueFactory<>("hsnNumber"));
        tblcGstRate.setCellValueFactory(new PropertyValueFactory<>("gst"));
        tblcQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblcRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
        tblcCgstRate.setCellValueFactory(new PropertyValueFactory("cgstWithAmount"));
        tblcSgstRate.setCellValueFactory(new PropertyValueFactory<>("sgstWithAmount"));
        tblcAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }
    
    
}
