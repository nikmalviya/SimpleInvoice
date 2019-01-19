package simpleinvoice.model;

import java.time.LocalDate;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

public class Invoice {
    private IntegerProperty invoice_id;
    private IntegerProperty invoice_no;
    private ObjectProperty<LocalDate> invoice_date;
    private ObjectProperty<Customer> customer;
    private FloatProperty net_amount;
    private FloatProperty discount;
    private ListProperty<InvoiceItem> items;
    private  NumberBinding subtotal;
    
    public Invoice(int invoice_id,int invoice_no,LocalDate date,Customer customer,float net_amount,float discount,ObservableList<InvoiceItem> items){
        this.invoice_id = new SimpleIntegerProperty(invoice_id);
        this.invoice_no = new SimpleIntegerProperty(invoice_no);
        this.invoice_date = new SimpleObjectProperty<>(date);
        this.customer = new SimpleObjectProperty<>(customer);
        this.net_amount = new SimpleFloatProperty(net_amount);
        this.discount = new SimpleFloatProperty(discount);
        this.items = new SimpleListProperty<>(items);
        this.subtotal = this.net_amount.subtract(this.discount);
        
    }
    public Invoice(int invoice_no,LocalDate date,Customer customer,float net_amount,float discount,ObservableList<InvoiceItem> items){
        this(0,invoice_no,date,customer,net_amount,discount,items);
    }
    public float getSubtotal(){
        return subtotal.floatValue();
    }   
    public Integer getInvoice_id() {
        return invoice_id.get();
    }

    public void setInvoice_id(int invoice_id) {
        this.invoice_id.set(invoice_id);
    }

    public int getInvoice_no() {
        return invoice_no.get();
    }

    public void setInvoice_no(int invoice_no) {
        this.invoice_no.set(invoice_no);
    }

    public LocalDate getInvoice_date() {
        return invoice_date.get();
    }

    public void setInvoice_date(LocalDate invoice_date) {
        this.invoice_date.get();
    }

    public Customer getCustomer() {
        return customer.get();
    }

    public void setCustomer(Customer customer) {
        this.customer.set(customer);
    }

    public float getNet_amount() {
        return net_amount.get();
    }

    public void setNet_amount(float net_amount) {
        this.net_amount.set(net_amount);
    }

    public float getDiscount() {
        return discount.get();
    }

    public void setDiscount(float discount) {
        this.discount.set(discount);
    }
    
}
