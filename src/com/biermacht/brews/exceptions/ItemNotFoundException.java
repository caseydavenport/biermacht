package com.biermacht.brews.exceptions;

public class ItemNotFoundException extends Exception {
  public ItemNotFoundException() {
  }

  //Constructor that accepts a message
  public ItemNotFoundException(String message) {
    super(message);
  }
}
