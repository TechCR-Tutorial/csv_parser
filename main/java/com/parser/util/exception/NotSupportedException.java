/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.util.exception;

/**
 * Created by chamly on 5/27/15.
 */
public class NotSupportedException extends Exception implements ParaserException{
    static String message = "Not Yes Support";

    public NotSupportedException() {
        super(message);
    }

    @Override
    public String getMessage() {
        return message;
    }
}

