package com.biermacht.brews.xml;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.biermacht.brews.R;

public class XMLTEST extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmltest);
        
        try {
            FermentableHandler myXMLHandler = new FermentableHandler();
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            
            AssetManager am = getAssets();
            InputStream is = am.open("justafile.xml");
            
            sp.parse(is, myXMLHandler);
 
            ArrayList<Order> orderList = myXMLHandler.getOrderList();
            
            for(int i=0;i<orderList.size();i++){
                Order order = orderList.get(i);
                Log.e("XMLTEST","OrderID: " + order.getOrderID() + " ----------->");
                Log.e("XMLTEST","Type: " + order.getOrderType());
                Log.e("XMLTEST","Date: " + order.getOrderDate());
                Log.e("XMLTEST","Customer: " + order.getOrderType());
                Log.e("XMLTEST","PO: " + order.getCustPONumber());
                Log.e("XMLTEST","Total: " + order.getOrderTotal());
                ArrayList<OrderDetail> orderDetailList = order.getOrderDetail();
                
                for(int j=0;j<orderDetailList.size();j++){
                    OrderDetail orderDetail = orderDetailList.get(j);
                    Log.e("XMLTEST","LineID: " + orderDetail.getOrderlineID() + " ----->");
                    Log.e("XMLTEST","Item: " + orderDetail.getItemID());
                    Log.e("XMLTEST","Description: " + orderDetail.getItemDescription());
                    Log.e("XMLTEST","Price: " + orderDetail.getPrice());
                    Log.e("XMLTEST","Quantity: " + orderDetail.getQuantity());
 
                }
            }
        }
        catch (Exception e) {
            Log.e("XMLTEST",e.toString());
        }
        finally {
 
        }   
        
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_xmltest, menu);
        return true;
    }
}
