package simpleinvoice.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author programmer
 */ 
public class Customer {
    private final IntegerProperty customerID;
    private final StringProperty customerName;
    private final StringProperty contactNumber;
    private final StringProperty address;
    private final StringProperty partyGSTNo;

    public Customer(String customerName, String contactNumber, String address, String partyGSTNo) {
        this.customerID = new SimpleIntegerProperty();
        this.customerName = new SimpleStringProperty(customerName);
        this.contactNumber = new SimpleStringProperty(contactNumber);
        this.address = new SimpleStringProperty(address);
        this.partyGSTNo = new SimpleStringProperty(partyGSTNo);
    }
    
    
    public Customer(Integer customerID, String customerName, String contactNumber, String address, String partyGSTNo) {
        this.customerID = new SimpleIntegerProperty(customerID);
        this.customerName = new SimpleStringProperty(customerName);
        this.contactNumber = new SimpleStringProperty(contactNumber);
        this.address = new SimpleStringProperty(address);
        this.partyGSTNo = new SimpleStringProperty(partyGSTNo);
    }
    
    
    public Integer getCustomerID() {
        return customerID.get();
    }

    public void setCustomerID(Integer customerID) {
        this.customerID.set(customerID);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public String getContactNumber() {
        return contactNumber.get();
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber.set(contactNumber);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPartyGSTNo() {
        return partyGSTNo.get();
    }

    public void setPartyGSTNo(String partyGSTNo) {
        this.partyGSTNo.set(partyGSTNo);
    }
    
}
