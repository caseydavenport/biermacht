package com.biermacht.brews.xml;

import java.util.ArrayList;


public class Order {
   
   String orderType = null;
   String orderID = null;
   String orderDate = null;
   String custNumber = null;
   String custPONumber = null;
   String orderTotal = null;
   ArrayList<OrderDetail> orderDetail = new ArrayList<OrderDetail>();
   
   public String getOrderType() {
       return orderType;
   }
   public void setOrderType(String orderType) {
       this.orderType = orderType;
   }
   public String getOrderID() {
       return orderID;
   }
   public void setOrderID(String orderID) {
       this.orderID = orderID;
   }
   public String getOrderDate() {
       return orderDate;
   }
   public void setOrderDate(String orderDate) {
       this.orderDate = orderDate;
   }
   public String getCustNumber() {
       return custNumber;
   }
   public void setCustNumber(String custNumber) {
       this.custNumber = custNumber;
   }
   public String getCustPONumber() {
       return custPONumber;
   }
   public void setCustPONumber(String custPONumber) {
       this.custPONumber = custPONumber;
   }
   public String getOrderTotal() {
       return orderTotal;
   }
   public void setOrderTotal(String orderTotal) {
       this.orderTotal = orderTotal;
   }
   public ArrayList<OrderDetail> getOrderDetail() {
       return orderDetail;
   }
   public void setOrderDetail(ArrayList<OrderDetail> orderDetail) {
       this.orderDetail = orderDetail;
   }
   
   

}