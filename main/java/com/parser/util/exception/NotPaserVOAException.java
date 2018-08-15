/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.util.exception;

/**
 * Created by chamly on 5/27/15.
 */
public class NotPaserVOAException extends Exception implements ParaserException{
    static String message = "Not Parser VOA entity";

    public NotPaserVOAException() {
        super(message);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
