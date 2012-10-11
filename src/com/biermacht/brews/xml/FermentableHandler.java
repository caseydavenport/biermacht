package com.biermacht.brews.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class FermentableHandler extends DefaultHandler {
 
    boolean currentElement = false;
    String currentValue = null;
    Order order = null;
    OrderDetail orderDetail = null;
    ArrayList<OrderDetail> orderDetailList;
    ArrayList<Order> orderList = new ArrayList<Order>();
 
    public ArrayList<Order> getOrderList() {
        return orderList;
    }
 
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
 
        currentElement = true;
 
        if (qName.equals("Order")){
            order = new Order();
            orderDetailList = new ArrayList<OrderDetail>();
            order.setOrderType(attributes.getValue("Type"));
        } else if (qName.equals("OrderLine")) {
            orderDetail = new OrderDetail();
        }
 
    }
 
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
 
        currentElement = false;
 
        if (qName.equalsIgnoreCase("OrderID"))
            order.setOrderID(currentValue);
        else if (qName.equalsIgnoreCase("CustNumber"))
            order.setCustNumber(currentValue);
        else if (qName.equalsIgnoreCase("OrderDate"))
            order.setOrderDate(currentValue);
        else if (qName.equalsIgnoreCase("CustPONumber"))
            order.setCustPONumber(currentValue);
        else if (qName.equalsIgnoreCase("OrderTotal"))
            order.setOrderTotal(currentValue);
        else if (qName.equalsIgnoreCase("OrderlineID"))
            orderDetail.setOrderlineID(currentValue);
        else if (qName.equalsIgnoreCase("ItemID"))
            orderDetail.setItemID(currentValue);
        else if (qName.equalsIgnoreCase("ItemDescription"))
            orderDetail.setItemDescription(currentValue);
        else if (qName.equalsIgnoreCase("Quantity"))
            orderDetail.setQuantity(currentValue);
        else if (qName.equalsIgnoreCase("Price"))
            orderDetail.setPrice(currentValue);
        else if (qName.equalsIgnoreCase("Order")){
            order.setOrderDetail(orderDetailList);
            orderList.add(order);
        }
        else if (qName.equalsIgnoreCase("OrderLine"))
            orderDetailList.add(orderDetail);
    }
 
    @Override
    public void characters(char[] ch, int start, int length)
    throws SAXException {
 
        if (currentElement) {
            currentValue = new String(ch, start, length);
            currentElement = false;
        }
 
    }
 
}