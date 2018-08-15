/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.util.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chamly on 6/9/15.
 */
public class ExceptionMap {

    private Map<Long, List<ParaserException>> exceptionMap = new HashMap<>();

    public ExceptionMap() {
        this.exceptionMap = new HashMap<Long, List<ParaserException>>();
    }

    public void addException(Long lineNumber, ParaserException exception) {
        List<ParaserException> exceptions = exceptionMap.get(lineNumber);
        if (null == exceptions) {
            exceptions = new ArrayList<ParaserException>();
            exceptionMap.put(lineNumber, exceptions);
        }
        exceptions.add(exception);
    }

    public String getException() {
        StringBuilder error = new StringBuilder();
        for (Long lineNo : exceptionMap.keySet()) {
            error.append("Line No : " + lineNo + "\n");
            error.append(getException(lineNo));
            error.append(" _____________________________________ \n");
            error.append(" \n");
        }
        return error.toString();
    }

    public String getException(long lineNo) {
        List<ParaserException> parserExceptions = exceptionMap.get(lineNo);
        return getLineError(parserExceptions);
    }

    private String getLineError(List<ParaserException> exceptions) {
        StringBuilder error = new StringBuilder();
        if (null != exceptions) {
            for (ParaserException exception : exceptions) {
                error.append(exception.getMessage() + "\n");
            }
        }
        return error.toString();
    }

}
