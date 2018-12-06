package simpleinvoice.model;

import java.text.NumberFormat;
import java.util.Locale;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InvoiceItem {
    private final IntegerProperty srno;
    private final StringProperty productName;
    private final IntegerProperty hsnNumber;
    private final FloatProperty gst;
    private final FloatProperty rate;
    private final IntegerProperty quantity;
    private final FloatProperty cgst;
    private final FloatProperty sgstPercent;
    private final FloatProperty cgstPercent;
    private final FloatProperty sgst;
    private final FloatProperty amount;
    private final StringProperty cgstWithamount;
    private final StringProperty sgstWithamount;
    private boolean isGSTIncluded = false;
    private final NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en","IN"));

    public InvoiceItem(Product product,Integer srno,Integer quantity,Float price,boolean includesGST) {
        this.srno = new SimpleIntegerProperty(srno);
        this.productName = new SimpleStringProperty(product.getName());
        this.hsnNumber = new SimpleIntegerProperty(product.getHsnNumber());
        this.gst = new SimpleFloatProperty(product.getGstRate());
        this.quantity = new SimpleIntegerProperty(quantity);
        this.cgstPercent = new SimpleFloatProperty(product.getGstRate()/2);
        this.sgstPercent = new SimpleFloatProperty(product.getGstRate()/2);
        float x = (100+gst.get())/100;
        this.isGSTIncluded = includesGST;
        this.rate = new SimpleFloatProperty(includesGST?price/x:price);
        float p = this.rate.get() * quantity;
        this.cgst = new SimpleFloatProperty((cgstPercent.get()*p)/100);
        this.sgst = new SimpleFloatProperty((sgstPercent.get()*p)/100);
        this.amount = new SimpleFloatProperty(p+cgst.get()+sgst.get());
        this.cgstWithamount = new SimpleStringProperty(format.format(cgst.get())+"\n   ("+cgstPercent.get()+"%)");
        this.sgstWithamount = new SimpleStringProperty(format.format(sgst.get())+"\n   ("+sgstPercent.get()+"%)");
    }

    public boolean isGSTIncluded() {
        return isGSTIncluded;
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
    public String getGstString(){
        return gst.get()+"%";
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
    public String getFormattedAmount() {
        return format.format(amount.get());
    }

    public void setAmount(Float amount) {
        this.amount.set(amount);
    }

    public Float getRate() {
        return rate.get();
    }
    
    public String getFormattedRate() {
        return format.format(rate.get());
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
