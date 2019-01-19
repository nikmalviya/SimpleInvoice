package simpleinvoice.database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Database {

    private static Connection con;
    private static final String sqlQuery = "BEGIN TRANSACTION;"
            + "CREATE TABLE IF NOT EXISTS `product` ("
            + "	`product_id`	INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "	`product_name`	TEXT NOT NULL UNIQUE,"
            + "	`product_qty`	INTEGER,"
            + "	`product_gst`	REAL,"
            + "	`product_hsn`	TEXT"
            + ");"
            + "CREATE TABLE IF NOT EXISTS `customer` ("
            + "	`cust_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + "	`cust_name`	TEXT NOT NULL,"
            + "	`cust_phone`	TEXT NOT NULL,"
            + "	`cust_address`	TEXT,"
            + "	`cust_gstn`	TEXT"
            + ");"
            + "COMMIT;";
    private static Database instance;

    public static Connection getConnection() {
        return con;
    }

    public static ResultSet executeQuery(String sql) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        return res;
    }

    public static Boolean executeUpdate(String sql) throws SQLException {
        Boolean res;
        Statement stmt = con.createStatement();
        res = stmt.execute(sql);
        return res;
    }

    public static Database getInstance() {
        if (instance == null) {
            init();
        }
        return instance;
    }

    public static void init() {
        System.out.println();
        try {
            con = DriverManager.getConnection("jdbc:sqlite:simpleinvoice.db");
        } catch (SQLException ex) {
            System.out.println(ex);
            Alert al = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            al.setHeaderText(String.valueOf(ex.getErrorCode()));
            al.showAndWait();
            System.exit(0);
        }
        try {
            Statement stmt = con.createStatement();
            for (String query : sqlQuery.split(";")) {
                stmt.addBatch(query);
            }
            stmt.executeBatch();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        instance = new Database();
    }
}
