package com.biermacht.brews.exceptions;

/**
 * Created by Casey on 8/11/13.
 */
public class RecipeNotFoundException extends Exception
{
    //Parameterless Constructor
    public RecipeNotFoundException() {}

    //Constructor that accepts a message
    public RecipeNotFoundException(String message)
    {
        super(message);
    }
}
