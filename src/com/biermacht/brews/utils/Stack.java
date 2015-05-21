package com.biermacht.brews.utils;

import android.util.Log;

import java.util.ArrayList;


public class Stack  {
    private ArrayList<String> list;

    public Stack()
    {
        list = new ArrayList<String>();
    }

    public void push(String s)
    {
        Log.d("Stack", "Pushing " + s + " on the stack");
        list.add(s);
    }

    public String pop()
    {
        String s = list.get(list.size()-1);
        list.remove(s);
        Log.d("Stack", "Popping " + s + " off of the stack");
        return s;
    }

    public String read()
    {
        return list.get(list.size()-1);
    }

    public boolean contains(String s)
    {
        return list.contains(s);
    }
}
