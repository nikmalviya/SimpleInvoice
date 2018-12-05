package simpleinvoice.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InvoiceItem {
    private IntegerProperty srno;
    private final StringProperty productName;
    private IntegerProperty hsnNumber;
    private FloatProperty gst;
    private FloatProperty rate;
    private IntegerProperty quantity;
    private FloatProperty cgst;
    private FloatProperty sgstPercent;
    private FloatProperty cgstPercent;
    private FloatProperty sgst;
    private FloatProperty amount;
    private StringProperty cgstWithamount;
    private StringProperty sgstWithamount;

    public InvoiceItem(Product product,Integer srno,Integer quantity) {
        this.srno = new SimpleIntegerProperty(srno);
        this.productName = new SimpleStringProperty(product.getName());
        this.hsnNumber = new SimpleIntegerProperty(product.getHsnNumber());
        this.rate = new SimpleFloatProperty(product.getPrice());
        this.gst = new SimpleFloatProperty(product.getGstRate());
        this.quantity = new SimpleIntegerProperty(quantity);
        this.cgstPercent = new SimpleFloatProperty(product.getGstRate()/2);
        this.sgstPercent = new SimpleFloatProperty(product.getGstRate()/2);
        float price = this.rate.get() * quantity;
        this.cgst = new SimpleFloatProperty((cgstPercent.get()*price)/100);
        this.sgst = new SimpleFloatProperty((sgstPercent.get()*price)/100);
        this.amount = new SimpleFloatProperty(price+cgst.get()+sgst.get());
        this.cgstWithamount = new SimpleStringProperty(String.valueOf(cgst.get())+"\n"+cgstPercent.get()+"%");
        this.sgstWithamount = new SimpleStringProperty(String.valueOf(sgst.get())+"\n"+sgstPercent+"%");
    }

    public String getCgstWithamount() {
        return cgstWithamount.get();
    }

    public String getSgstWithamount() {
        return sgstWithamount.get();
    }
    
    public Integer getSrno() {
        return srno.get();
    }

    public void setSrno(Integer srno) {
        this.srno.set(srno);
    }

    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public Integer getHsnNumber() {
        return hsnNumber.get();
    }

    public void setHsnNumber(Integer hsnNumber) {
        this.hsnNumber.set(hsnNumber);
    }

    public Float getGst() {
        return gst.get();
    }

    public void setGst(Float gst) {
        this.gst.set(gst);
    }

    public Integer getQuantity() {
        return quantity.get();
    }

    public void setQuantity(Integer quantity) {
        this.quantity.set(quantity);
    }

    public Float getCgst() {
        return cgst.get();
    }

    public void setCgst(Float cgst) {
        this.cgst.set(cgst);
    }

    public Float getSgst() {
        return sgst.get();
    }

    public void setSgst(Float sgst) {
        this.sgst.set(sgst);
    }

    public Float getAmount() {
        return amount.get();
    }

    public void setAmount(Float amount) {
        this.amount.set(amount);
    }

    public Float getRate() {
        return rate.get();
    }

    public void setRate(Float rate) {
        this.rate.set(rate);
    }

    public Float getSgstPercent() {
        return sgstPercent.get();
    }

    public void setSgstPercent(Float sgstPercent) {
        this.sgstPercent.set(sgstPercent);
    }

    public Float getCgstPercent() {
        return cgstPercent.get();
    }

    public void setCgstPercent(Float cgstPercent) {
        this.cgstPercent.set(cgstPercent);
    }
}
