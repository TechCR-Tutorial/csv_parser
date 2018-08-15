/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.util.exception;

public class BaseParserException extends Exception implements ParaserException {

    private String message;
    private String fieldName;
    private Exception exception;

    public BaseParserException(String message) {
        super(message);
        this.message = message;
    }

    public BaseParserException(String message, Exception exception) {
        super(message);
        this.message = message;
        this.exception = exception;
    }

    @Override
    public String getMessage() {
        return (null != exception ? exception.getMessage() + " \n" : "") + message +
                (null == fieldName || "".equals(fieldName.trim()) ? "" : " : " + fieldName);
    }


    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
