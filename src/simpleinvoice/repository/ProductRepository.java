package simpleinvoice.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import simpleinvoice.database.Database;
import simpleinvoice.model.Product;

/**
 * @author programmer
 */
public class ProductRepository {
    private final PreparedStatement insertProduct;
    private final PreparedStatement deleteProduct;
    private final PreparedStatement updateProduct;
    private static final ObservableList<Product> PRODUCTS = FXCollections.observableArrayList();
    private static final ObservableList<String> PRODUCT_NAME_LIST = FXCollections.observableArrayList();
    private static ProductRepository productsrepo;

    public ObservableList<Product> getProducts() {
        return PRODUCTS;
    }

    public ObservableList<String> getProductNameList() {
        return PRODUCT_NAME_LIST;
    }

    private ProductRepository() throws SQLException {
        initProductsRepositary();
        this.insertProduct = Database.getConnection().prepareStatement("INSERT INTO product (product_name,product_qty,product_gst,product_hsn) VALUES (?,?,?,?)");
        this.deleteProduct = Database.getConnection().prepareStatement("DELETE FROM product where product_id = ?");
        this.updateProduct = Database.getConnection().prepareStatement("UPDATE product SET product_name = ? , product_qty = ?, product_gst=?,product_hsn=? where product_id = ?");
    }

    public static ProductRepository getProductRepository() throws SQLException {
        if (productsrepo == null) {
            productsrepo = new ProductRepository();
        }
        return productsrepo;
    }

    public void updateProduct(Product old, Product p) throws SQLException {
        this.updateProduct.setString(1, p.getName());
        this.updateProduct.setInt(2, p.getQtyAvailable());
        this.updateProduct.setFloat(3, p.getGstRate());
        this.updateProduct.setFloat(4, p.getHsnNumber());
        this.updateProduct.setInt(5, old.getProductID());
        p.setProductID(old.getProductID());
        updateProduct.execute();
        int i = PRODUCTS.indexOf(old);
        PRODUCTS.remove(i);
        PRODUCT_NAME_LIST.removeIf(e -> e.equals(old.getName()));
        PRODUCT_NAME_LIST.add(p.getName());
        PRODUCTS.add(i, p);
    }

    public void addNewProduct(Product p) throws SQLException {
        insertProduct.setString(1, p.getName());
        insertProduct.setInt(2, p.getQtyAvailable());        
        insertProduct.setFloat(3, p.getGstRate());
        insertProduct.setInt(4, p.getHsnNumber());
        insertProduct.execute();
        PRODUCTS.clear();
        PRODUCT_NAME_LIST.clear();
        initProductsRepositary();

    }

    public Product getProduct(String productName) {
        return PRODUCTS.filtered(p -> p.getName().equals(productName)).get(0);
    }

    public String getProductName(int id) throws SQLException {
        ResultSet rs = Database.executeQuery("select product_name from product where product_id=" + id);
        rs.next();
        return rs.getString("product_name");
    }

//    public int getProductId(Student c) throws SQLException {
//        String sql = "select course_id from cms.course where course_name='" + c.getProduct() + "'";
//        Database.getInstance();
//        ResultSet rs = Database.executeQuery(sql);
//        rs.next();
//        return rs.getInt("course_id");
//    }

    public int getProductId(String productName) throws SQLException {
        String sql = "select product_id from product where product_name ='" + productName + "'";
        Database.getInstance();
        ResultSet rs = Database.executeQuery(sql);
        int id = 0;
        while (rs.next()) {
            id = rs.getInt("product_id");
        }
        return id;
    }

    public void deleteProduct(Product p) throws SQLException {
        deleteProduct.setInt(1, p.getProductID());
        deleteProduct.execute();
        PRODUCTS.remove(p);
        PRODUCT_NAME_LIST.remove(p.getName());
    }

    private void initProductsRepositary() throws SQLException {
        Database.getInstance();
        ResultSet rs = Database.executeQuery("select * from product");
        while (rs.next()) {
            PRODUCTS.add(new Product(rs.getInt("product_id"),rs.getString("product_name"),rs.getInt("product_qty"),rs.getFloat("product_gst"),rs.getInt("product_hsn")));
            PRODUCT_NAME_LIST.add(rs.getString("product_name"));
        }
    }

    public int getCount() throws SQLException {
        String sql = "select count(*) as count from product";
        ResultSet rs = Database.executeQuery(sql);
        rs.next();
        return rs.getInt("count");
    }

    public void refresh() throws SQLException {
        PRODUCTS.clear();
        PRODUCT_NAME_LIST.clear();
        initProductsRepositary();
    } 
}
