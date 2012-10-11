package com.biermacht.brews.xml;

public class OrderDetail {
    
    String orderlineID = null;
    String itemID = null;
    String itemDescription = null;
    String quantity = null;
    String price = null;
    public String getOrderlineID() {
        return orderlineID;
    }
    public void setOrderlineID(String orderlineID) {
        this.orderlineID = orderlineID;
    }
    public String getItemID() {
        return itemID;
    }
    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
    public String getItemDescription() {
        return itemDescription;
    }
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    
    
}