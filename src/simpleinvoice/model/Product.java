package simpleinvoice.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {
    private final IntegerProperty productID;

    private final StringProperty name;
    private final IntegerProperty qtyAvailable;
    private final FloatProperty price;
    private final FloatProperty gstRate;
    private final IntegerProperty hsnNumber;
    
    public Product(){
        productID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        qtyAvailable = new SimpleIntegerProperty();
        price = new SimpleFloatProperty();
        gstRate = new SimpleFloatProperty();
        hsnNumber = new SimpleIntegerProperty();
    }
    public Product(String name,int qty,float price,float gst,int hsn){
        productID = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty(name);
        this.qtyAvailable = new SimpleIntegerProperty(qty);
        this.price = new SimpleFloatProperty(price);
        this.gstRate = new SimpleFloatProperty(gst);
        this.hsnNumber = new SimpleIntegerProperty(hsn);
        
    }
    public Product(int id,String name,int qty,float price,float gst,int hsn){
        productID = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.qtyAvailable = new SimpleIntegerProperty(qty);
        this.price = new SimpleFloatProperty(price);
        this.gstRate = new SimpleFloatProperty(gst);
        this.hsnNumber = new SimpleIntegerProperty(hsn);
        
    }
    public Integer getProductID() {
        return productID.get();
    }
    public void setProductID(Integer id){
        this.productID.set(id);
    }
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Integer getQtyAvailable() {
        return qtyAvailable.get();
    }

    public void setQtyAvailable(Integer qtyAvailable) {
        this.qtyAvailable.get();
    }

    public Float getPrice() {
        return price.get();
    }

    public void setPrice(Float price) {
        this.price.set(price);
    }

    public Float getGstRate() {
        return gstRate.get();
    }

    public void setGstRate(Float gstRate) {
        this.gstRate.set(gstRate);
    }

    public Integer getHsnNumber() {
        return hsnNumber.get();
    }

    public void setHsnNumber(Integer hsnNumber) {
        this.hsnNumber.set(hsnNumber);
    }
}
