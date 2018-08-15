/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.util.exception;

/**
 * Created by chamly on 5/29/15.
 */
public class CsvParserException extends BaseParserException {


    public CsvParserException(String message) {
        super(message);
    }

    public CsvParserException(String message, Exception exception) {
        super(message, exception);
    }

}
