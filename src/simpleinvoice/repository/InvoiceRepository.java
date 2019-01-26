package simpleinvoice.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import simpleinvoice.database.Database;
import simpleinvoice.model.Invoice;

/**
 * @author programmer
 */
public class InvoiceRepository {

    private final PreparedStatement insertInvoice;
    private final PreparedStatement insertInvoiceItems;
    private final PreparedStatement deleteInvoice;
    private final PreparedStatement updateInvoice;
    private static final ObservableList<Invoice> INVOICES = FXCollections.observableArrayList();
    private static final ObservableList<String> INVOICE_NAME_LIST = FXCollections.observableArrayList();
    private static InvoiceRepository invoicessrepo;

    public ObservableList<Invoice> getInvoices() {
        return INVOICES;
    }

    public ObservableList<String> getInvoiceNameList() {
        return INVOICE_NAME_LIST;
    }

    private InvoiceRepository() throws SQLException {
        initInvoicesRepositary();
        this.insertInvoice = Database.getConnection().prepareStatement("INSERT INTO invoice  VALUES (null,?,?,?,?,?)");
        this.insertInvoiceItems = Database.getConnection().prepareStatement("INSERT INTO invoiceitem VALUES (null,?,?,?,?,?,?)");
        this.deleteInvoice = Database.getConnection().prepareStatement("DELETE FROM product where product_id = ?");
        this.updateInvoice = Database.getConnection().prepareStatement("UPDATE product SET product_name = ? , product_qty = ?, product_gst=?,product_hsn=? where product_id = ?");
    }

    public static InvoiceRepository getInvoiceRepository() throws SQLException {
        if (invoicessrepo == null) {
            invoicessrepo = new InvoiceRepository();
        }
        return invoicessrepo;
    }

    public void updateInvoice(Invoice old, Invoice p) throws SQLException {
//        this.updateInvoice.setString(1, p.getName());
//        this.updateInvoice.setInt(2, p.getQtyAvailable());
//        this.updateInvoice.setFloat(3, p.getGstRate());
//        this.updateInvoice.setFloat(4, p.getHsnNumber());
//        this.updateInvoice.setInt(5, old.getInvoiceID());
//        p.setInvoiceID(old.getInvoiceID());
//        updateInvoice.execute();
//        int i = INVOICES.indexOf(old);
//        INVOICES.remove(i);
//        INVOICE_NAME_LIST.removeIf(e -> e.equals(old.getName()));
//        INVOICE_NAME_LIST.add(p.getName());
//        INVOICES.add(i, p);
    }

    public void addNewInvoice(Invoice i) throws SQLException {
        insertInvoice.setInt(1, i.getInvoice_no());
        insertInvoice.setString(2, i.getInvoice_date().toString());
        insertInvoice.setInt(3, i.getCustomer().getCustomerID());
        insertInvoice.setFloat(4, i.getNet_amount());
        insertInvoice.setFloat(5, i.getDiscount());
        insertInvoice.execute();
        INVOICES.clear();
        INVOICE_NAME_LIST.clear();
        initInvoicesRepositary();
        i.getItems().forEach( item -> {
            try {
                insertInvoiceItems.setInt(1,ProductRepository.getProductRepository().getProductId(item.getProductName()));
                insertInvoiceItems.setInt(2,item.getQuantity());
                insertInvoiceItems.setInt(3,this.getInvoiceID(i.getInvoice_no()));
                insertInvoiceItems.setFloat(4,item.getCgst());
                insertInvoiceItems.setFloat(5,item.getSgst());
                insertInvoiceItems.setFloat(6, item.getAmount());
                insertInvoiceItems.execute();
                
            } catch (SQLException ex) {
                Logger.getLogger(InvoiceRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
            
                    
        });
    }
    

//    public Invoice getInvoice(String productName) {
////        return INVOICES.filtered(p -> p.getName().equals(productName)).get(0);
//    }
//    public String getInvoiceName(int id) throws SQLException {
////        ResultSet rs = Database.executeQuery("select product_name from product where product_id=" + id);
////        rs.next();
////        return rs.getString("product_name");
//    }
//    public int getInvoiceId(Student c) throws SQLException {
//        String sql = "select course_id from cms.course where course_name='" + c.getInvoice() + "'";
//        Database.getInstance();
//        ResultSet rs = Database.executeQuery(sql);
//        rs.next();
//        return rs.getInt("course_id");
//    }
//    public int getInvoiceId(String productName) throws SQLException {
//        String sql = "select product_id from product where product_name ='" + productName + "'";
//        Database.getInstance();
//        ResultSet rs = Database.executeQuery(sql);
//        int id = 0;
//        while (rs.next()) {
//            id = rs.getInt("product_id");
//        }
//        return id;
//    }
//    public void deleteInvoice(Invoice p) throws SQLException {
//        deleteInvoice.setInt(1, p.getInvoiceID());
//        deleteInvoice.execute();
//        INVOICES.remove(p);
//        INVOICE_NAME_LIST.remove(p.getName());
//    }
    private void initInvoicesRepositary() throws SQLException {
//        Database.getInstance();
//        ResultSet rs = Database.executeQuery("select * from invoice");
//        while (rs.next()) {
//            INVOICES.add(new Invoice(rs.getInt("product_id"),rs.getString("product_name"),rs.getInt("product_qty"),rs.getFloat("product_gst"),rs.getInt("product_hsn")));
//            INVOICE_NAME_LIST.add(rs.getString("product_name"));
//        }
    }

    public int getCount() throws SQLException {
        String sql = "select count(*) as count from invoice";
        ResultSet rs = Database.executeQuery(sql);
        rs.next();
        return rs.getInt("count");
    }

    public void refresh() throws SQLException {
        INVOICES.clear();
        INVOICE_NAME_LIST.clear();
        initInvoicesRepositary();
    }

    public int getInvoiceCount() throws SQLException {
        String sql = "select count from invoice_no_counter";
        ResultSet rs = Database.executeQuery(sql);
        rs.next();
        return rs.getInt("count");
    }
    public int getInvoiceID(int invoice_no) throws SQLException{
        String sql = "select invoice_id from invoice where invoice_no = "+invoice_no;
        ResultSet rs = Database.executeQuery(sql);
        rs.next();
        return rs.getInt("invoice_id");
    }
}
