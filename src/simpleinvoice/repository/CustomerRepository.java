package simpleinvoice.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import simpleinvoice.database.Database;
import simpleinvoice.model.Customer;

public class CustomerRepository {

    private final PreparedStatement insertCustomer;
    private final PreparedStatement deleteCustomer;
    private final PreparedStatement updateCustomer;
    private static final ObservableList<Customer> CUSTOMERS = FXCollections.observableArrayList();
    private static final ObservableList<String> CUSTOMER_NAME_LIST = FXCollections.observableArrayList();
    private static CustomerRepository customersrepo;

    public ObservableList<Customer> getCustomers() {
        return CUSTOMERS;
    }

    public ObservableList<String> getCustomerNameList() {
        return CUSTOMER_NAME_LIST;
    }

    private CustomerRepository() throws SQLException {
        initCustomersRepositary();
        this.insertCustomer = Database.getConnection().prepareStatement("INSERT INTO customer VALUES (null,?,?,?,?)");
        this.deleteCustomer = Database.getConnection().prepareStatement("DELETE FROM customer where cust_id = ?");
        this.updateCustomer = Database.getConnection().prepareStatement("UPDATE customer SET cust_name = ? , cust_phone = ?, cust_address = ?,cust_gstn = ? where cust_id = ?");
    }

    public static CustomerRepository getCustomerRepository() throws SQLException {
        if (customersrepo == null) {
            customersrepo = new CustomerRepository();
        }
        return customersrepo;
    }

    public void updateCustomer(Customer old, Customer c) throws SQLException {
        this.updateCustomer.setString(1, c.getCustomerName());
        this.updateCustomer.setString(2, c.getContactNumber());
        this.updateCustomer.setString(3, c.getAddress());
        this.updateCustomer.setString(4, c.getPartyGSTNo());
        this.updateCustomer.setInt(5, old.getCustomerID());
        c.setCustomerID(old.getCustomerID());
        updateCustomer.execute();
        int i = CUSTOMERS.indexOf(old);
        CUSTOMERS.remove(i);
        CUSTOMER_NAME_LIST.removeIf(e -> e.equals(old.getCustomerName()));
        CUSTOMER_NAME_LIST.add(c.getCustomerName());
        CUSTOMERS.add(i, c);
    }

    public void addNewCustomer(Customer c) throws SQLException {
        insertCustomer.setString(1, c.getCustomerName());
        insertCustomer.setString(2, c.getContactNumber());
        insertCustomer.setString(3, c.getAddress());
        insertCustomer.setString(4, c.getPartyGSTNo());
        insertCustomer.execute();
        CUSTOMERS.clear();
        CUSTOMER_NAME_LIST.clear();
        initCustomersRepositary();

    }

    public Customer getCustomer(String customerName) {
        return CUSTOMERS.filtered(p -> p.getCustomerName().equals(customerName)).get(0);
    }

//    public String getCustomerName(int id) throws SQLException {
//        ResultSet rs = Database.executeQuery("select product_name from simpleinvoice.product where product_id=" + id);
//        rs.next();
//        return rs.getString("product_name");
//    }

//    public int getCustomerId(String productName) throws SQLException {
//        String sql = "select product_id from simpleinvoice.product where product_name ='" + productName + "'";
//        Database.getInstance();
//        ResultSet rs = Database.executeQuery(sql);
//        int id = 0;
//        while (rs.next()) {
//            id = rs.getInt("product_id");
//        }
//        return id;
//    }

    public void deleteCustomer(Customer c) throws SQLException {
        deleteCustomer.setInt(1, c.getCustomerID());
        deleteCustomer.execute();
        CUSTOMERS.remove(c);
        CUSTOMER_NAME_LIST.remove(c.getCustomerName());
    }

    private void initCustomersRepositary() throws SQLException {
        Database.getInstance();
        ResultSet rs = Database.executeQuery("select * from customer");
        while (rs.next()) {
            CUSTOMERS.add(new Customer(rs.getInt("cust_id"), rs.getString("cust_name"), rs.getString("cust_phone"), rs.getString("cust_address"), rs.getString("cust_gstn")));
            CUSTOMER_NAME_LIST.add(rs.getString("cust_name"));
        }
    }

    public int getCount() throws SQLException {
        String sql = "select count(*) as count from customer";
        ResultSet rs = Database.executeQuery(sql);
        rs.next();
        return rs.getInt("count");
    }

    public void refresh() throws SQLException {
        CUSTOMERS.clear();
        CUSTOMER_NAME_LIST.clear();
        initCustomersRepositary();
    }
}
