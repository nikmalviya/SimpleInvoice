/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleinvoice.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.textfield.CustomTextField;
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
    private TextField tfRate;
    @FXML
    private TextField tfQty;
    @FXML
    private TextField tfAmt;
    @FXML
    private Button btnAdd;
    @FXML
    private TableView<InvoiceItem> tblvInvoiceItems;
    @FXML
    private TableColumn<InvoiceItem, Integer> tblcSrNo;
    @FXML
    private TableColumn<InvoiceItem, String> tblcProductDes;
    @FXML
    private TableColumn<InvoiceItem, Integer> tblcHsnNo;
    @FXML
    private TableColumn<InvoiceItem, Float> tblcGstRate;
    @FXML
    private TableColumn<InvoiceItem, Integer> tblcQuantity;
    @FXML
    private TableColumn<InvoiceItem, Float> tblcRate;
    @FXML
    private TableColumn<InvoiceItem, Float> tblcAmount;
    @FXML
    private TableColumn<InvoiceItem, Float> tblcCgstRate;
    @FXML
    private TableColumn<InvoiceItem, Float> tblcSgstRate;
    @FXML
    private MenuButton mbProduct;
    @FXML
    private MenuItem miSearchFieldd;
    @FXML
    private CustomTextField tfSearchProduct;
    @FXML
    private MenuItem miProductTable;
    @FXML
    private TableColumn<Product, String> tblcProducDesc;
    @FXML
    private TableColumn<Product, Integer> tblcQty;
    @FXML
    private TableView<Product> tblvProduct;
    private Product selectedProduct = null;
    private ObservableList<InvoiceItem> invoiceItems;
    private int srnoCounter = 1;
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableColumns();
        btnAdd.setOnAction(this::addInvoiceItem);
        invoiceItems = FXCollections.observableArrayList();
        tblvInvoiceItems.setItems(invoiceItems);
        tblvProduct.setOnMouseClicked(this::selectProduct);
        tfSearchProduct.setOnKeyReleased(this::search);
        try {
            initProductsTable();
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tfQty.setText("0");
        tfRate.setText("0");
        tfQty.setOnKeyReleased(e -> calculateAmount());
        tfRate.setOnKeyReleased(e -> calculateAmount());
    }

    private void calculateAmount() {
        float rate = Float.parseFloat("".equals(tfRate.getText()) ? "0" : tfRate.getText());
        int qty = Integer.parseInt("".equals(tfQty.getText()) ? "0" : tfQty.getText());
        tfAmt.setText(String.valueOf(rate * qty));
    }

    private void initTableColumns() {
        tblcSrNo.setCellValueFactory(new PropertyValueFactory<>("srno"));
        tblcProductDes.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tblcHsnNo.setCellValueFactory(new PropertyValueFactory<>("hsnNumber"));
        tblcGstRate.setCellValueFactory(new PropertyValueFactory<>("gstString"));
        tblcQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblcRate.setCellValueFactory(new PropertyValueFactory<>("formattedRate"));
        tblcCgstRate.setCellValueFactory(new PropertyValueFactory("cgstWithamount"));
        tblcSgstRate.setCellValueFactory(new PropertyValueFactory<>("sgstWithamount"));
        tblcAmount.setCellValueFactory(new PropertyValueFactory<>("formattedAmount"));
    }

    private void initProductsTable() throws SQLException {
        tblcProducDesc.setCellValueFactory(new PropertyValueFactory<>("name"));
        tblcQty.setCellValueFactory(new PropertyValueFactory<>("qtyAvailable"));
        tblvProduct.setItems(ProductRepository.getProductRepository().getProducts());
    }

    private void search(KeyEvent e) {
        FilteredList<Product> filter;
        filter = new FilteredList<>(tblvProduct.getItems(), p -> true);
        tfSearchProduct.textProperty().addListener((ob, o, n) -> {
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
            sort.comparatorProperty().bind(tblvProduct.comparatorProperty());
            tblvProduct.setItems(sort);
        });
    }

    private void selectProduct(MouseEvent e) {
        if (!tblvProduct.getItems().isEmpty()) {
            selectedProduct = tblvProduct.getSelectionModel().getSelectedItem();
            mbProduct.setText(tblvProduct.getSelectionModel().getSelectedItem().getName());
            tfRate.setText(String.valueOf(selectedProduct.getPrice()));
            mbProduct.hide();
            tfQty.requestFocus();
        }
    }
    private void addInvoiceItem(ActionEvent e){
        invoiceItems.add(new InvoiceItem(selectedProduct,srnoCounter++, Integer.parseInt(tfQty.getText())));
    }

}
