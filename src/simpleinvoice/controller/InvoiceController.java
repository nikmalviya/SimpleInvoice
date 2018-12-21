/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleinvoice.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import simpleinvoice.model.Customer;
import simpleinvoice.model.InvoiceItem;
import simpleinvoice.model.Product;
import simpleinvoice.repository.CustomerRepository;
import simpleinvoice.repository.ProductRepository;
import simpleinvoice.utility.NumberToWords;

/**
 * FXML Controller class
 *
 * @author programmer
 */
public class InvoiceController implements Initializable {

    @FXML
    private DatePicker dpInvoiceDate;
    @FXML
    private TextField tfSubTotal;
    @FXML
    private TextField tfDiscount;
    @FXML
    private TextField tfNetAmount;
    @FXML
    private JFXButton btnEdit;
    @FXML
    private JFXButton btnRemove;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private TextField tfRate;
    @FXML
    private TextField tfQty;
    @FXML
    private TextField tfAmt;
    @FXML
    private JFXButton btnAdd;
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
    private TextField tfSearchProduct;
    @FXML
    private MenuItem miProductTable;
    @FXML
    private TableColumn<Product, String> tblcProducDesc;
    @FXML
    private TableColumn<Product, Integer> tblcQty;
    @FXML
    private TableView<Product> tblvProduct;
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
    private Product selectedProduct = null;
    private Customer selectedCustomer = null;
    private ObservableList<InvoiceItem> invoiceItems;
    private int srnoCounter = 1;
    @FXML
    private JFXCheckBox cboxIncludeGST;
    @FXML
    private JFXButton btnAddProduct;
    @FXML
    private TextField tfInvoiceNo;
    @FXML
    private JFXButton btnAddCust;
    @FXML
    private Label lblGST;
    @FXML
    private Label lblContact;
    @FXML
    private Label lblAddress;
    @FXML
    private MenuItem miSearchFielddCust;
    @FXML
    private TextField tfSearchCust;
    @FXML
    private MenuItem miCustTable;
    @FXML
    private TableView<Customer> tblvCustomer;
    @FXML
    private TableColumn<Customer,String> tblcCustName;
    @FXML
    private MenuButton mbCustomer;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnSave.setOnAction(this::previewInvoice);
        tblvInvoiceItems.setPlaceholder(new Label("No Products Added.."));
        LocalDate date = LocalDate.now();
        dpInvoiceDate.setValue(date);
        btnAddProduct.setOnAction(this::addProduct);
        btnAddCust.setOnAction(this::addCustomer);
        btnRemove.setOnAction(this::removeInvoiceItem);
        btnEdit.setOnAction(this::editInvoiceItem);
        initTableColumns();
        btnAdd.setOnAction(this::addInvoiceItem);
        btnEdit.disableProperty().bind(tblvInvoiceItems.getSelectionModel().selectedItemProperty().isNull());
        btnRemove.disableProperty().bind(tblvInvoiceItems.getSelectionModel().selectedItemProperty().isNull());
        invoiceItems = FXCollections.observableArrayList();
        tblvInvoiceItems.setItems(invoiceItems);
        tblvProduct.setOnMouseClicked(this::selectProduct);
        tblvCustomer.setOnMouseClicked(this::selectCustomer);
        tfSearchProduct.setOnKeyReleased(this::search);
        tfSearchCust.setOnKeyReleased(this::searchCustomers);
        try {
            initProductsTable();
            initCustomersTable();
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tfQty.setText("0");
        tfAmt.setText("0");
        tfRate.setText("0");
        tfSubTotal.setText("Rs. 0");
        tfDiscount.setText("0");
        tfDiscount.setOnKeyReleased(this::subtractDiscount);
        tfNetAmount.setText("Rs. 0");
        tfQty.setOnKeyReleased(e -> calculateAmount());
        tfRate.setOnKeyReleased(e -> calculateAmount());
    }

    private void subtractDiscount(KeyEvent e) {
        float discount = (tfDiscount.getText().equals(""))?0f:Float.parseFloat(tfDiscount.getText());
        float amount = 0;
        try {
            amount = formatter.parse(tfSubTotal.getText()).floatValue();
        } catch (ParseException ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        amount -= discount;
        tfNetAmount.setText(formatter.format(amount));
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
    private void initCustomersTable() throws SQLException {
        tblcCustName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tblvCustomer.setItems(CustomerRepository.getCustomerRepository().getCustomers());
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
    private void searchCustomers(KeyEvent e) {
        FilteredList<Customer> filter;
        filter = new FilteredList<>(tblvCustomer.getItems(), p -> true);
        tfSearchCust.textProperty().addListener((ob, o, n) -> {
            filter.setPredicate(c -> {
                if (n.isEmpty() || n == null) {
                    return true;
                } else if (c.getCustomerName().toLowerCase().contains(n.toLowerCase())) {
                    return true;
                } else if (String.valueOf(c.getCustomerID()).toLowerCase().contains(n.toLowerCase())) {
                    return true;
                } else {
                    return false;
                }
            });
            SortedList sort = new SortedList(filter);
            sort.comparatorProperty().bind(tblvCustomer.comparatorProperty());
            tblvCustomer.setItems(sort);
        });
    }

    private void selectProduct(MouseEvent e) {
        if (!tblvProduct.getItems().isEmpty()) {
            selectedProduct = tblvProduct.getSelectionModel().getSelectedItem();
            mbProduct.setText(tblvProduct.getSelectionModel().getSelectedItem().getName());
            mbProduct.hide();
            tfRate.requestFocus();
        }
    }
    private void selectCustomer(MouseEvent e) {
        if (!tblvCustomer.getItems().isEmpty()) {
            selectedCustomer = tblvCustomer.getSelectionModel().getSelectedItem();
            mbCustomer.setText(tblvCustomer.getSelectionModel().getSelectedItem().getCustomerName());
            lblContact.setText(selectedCustomer.getContactNumber());
            lblAddress.setText(selectedCustomer.getAddress());
            lblGST.setText(selectedCustomer.getPartyGSTNo());
            mbCustomer.hide();            
        }
    }

    private void addInvoiceItem(ActionEvent e) {
        invoiceItems.add(new InvoiceItem(selectedProduct, srnoCounter++, Integer.parseInt(tfQty.getText()),Float.parseFloat(tfRate.getText()),cboxIncludeGST.isSelected()));
        mbProduct.setText("Select Product");
        tfRate.setText("0");
        tfQty.setText("0");
        tfAmt.setText("0");
        if (!tblvInvoiceItems.getItems().isEmpty()) {
            float sum = 0;
            for (InvoiceItem invoiceItem : invoiceItems) {
                sum += invoiceItem.getAmount();
            }
            tfSubTotal.setText(formatter.format(sum));
            tfNetAmount.setText(formatter.format(sum));
        }
        tfSearchProduct.clear();
        cboxIncludeGST.selectedProperty().set(false);
    }
    private void addProduct(ActionEvent e){
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("/simpleinvoice/view/addProduct.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(pane));
        stage.show();
    }
    private void addCustomer(ActionEvent e){
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("/simpleinvoice/view/addCustomer.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(pane));
        stage.show();
    }
    private void editInvoiceItem(ActionEvent e){
        InvoiceItem item = tblvInvoiceItems.getSelectionModel().getSelectedItem();
        invoiceItems.remove(item);
        srnoCounter--;
        mbProduct.setText(item.getProductName());
        tfRate.setText(new DecimalFormat("#0.00").format(item.isGSTIncluded()?item.getAmount()/item.getQuantity():item.getRate()));
        tfQty.setText(String.valueOf(item.getQuantity()));
        tfAmt.setText(new DecimalFormat("#0.00").format(item.isGSTIncluded()?item.getAmount():item.getRate()*item.getQuantity()));
        cboxIncludeGST.selectedProperty().set(item.isGSTIncluded());
    }
    private void removeInvoiceItem(ActionEvent e){
        InvoiceItem item = tblvInvoiceItems.getSelectionModel().getSelectedItem();
        invoiceItems.remove(item);
        srnoCounter--;
    }
    private void previewInvoice(ActionEvent e){
        Map map = getInvoiceParameters();
        String reportPath = "/simpleinvoice/reports/invoice.jasper";
        JasperPrint print = null;
        List<InvoiceItem> list = FXCollections.observableArrayList(tblvInvoiceItems.getItems());
        int size = list.size();
        for (int i = 0; i < 7 - size; i++) {
            list.add(new InvoiceItem(new Product("", 0, 0, 0), 0, 0, 0f, true));
        }
        try(InputStream in = getClass().getResourceAsStream(reportPath)){
            print = JasperFillManager.fillReport(in, map,new JRBeanCollectionDataSource(list,true));
        } catch(Exception ex){
            System.out.println(ex);
            ex.printStackTrace();
            return;
        }
        JasperViewer viewer = new JasperViewer(print,false);
        viewer.setTitle("INVOICE");
        viewer.setVisible(true);
        
        
    }
    private Map getInvoiceParameters(){
        HashMap map = new HashMap(6);
        map.put("customerName",mbCustomer.getText().trim());
        map.put("contactNumber",lblContact.getText().trim());
        map.put("address", lblAddress.getText().trim());
        map.put("invoiceNo", tfInvoiceNo.getText().trim());
        map.put("invoiceDate", dpInvoiceDate.getValue().toString());
        map.put("partyGSTN", lblGST.getText().trim());
        map.put("subTotal", tfSubTotal.getText());
        map.put("discount", Float.parseFloat(tfDiscount.getText()));
        map.put("netAmount",tfNetAmount.getText());
        try {
            map.put("amountInWords", NumberToWords.convertNumberToWords(new BigDecimal(formatter.parse(tfNetAmount.getText()).toString()), true, true));
        } catch (ParseException ex) {
            Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

}
