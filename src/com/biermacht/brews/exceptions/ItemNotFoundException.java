package com.biermacht.brews.exceptions;

/**
 * Created by Casey on 8/11/13.
 */
public class ItemNotFoundException extends Exception {
  //Parameterless Constructor
  public ItemNotFoundException() {
  }

  //Constructor that accepts a message
  public ItemNotFoundException(String message) {
    super(message);
  }
}
