/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.util.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import com.parser.util.Constant;
import com.parser.util.exception.CsvParserException;
import com.parser.util.exception.ExceptionMap;
import com.parser.util.exception.ParaserException;

/**
 * @author chamly
 */
public class ParserVO {

    private String headerDelimeter = Constant.Symbol.SPACE;
    private String groupBy = Constant.Symbol.SPACE;
    private String dateFormat;
    private String dateTimeFormat;
    private Integer actionIndex = Constant.Values.MINUS_1;
    private List<String> excludeActions = new ArrayList<String>();
    private boolean processOnError = false;
    private int supperClassLevel = 0;
    private ExceptionMap exceptionMap = new ExceptionMap();
    private Map<String, String> headerFieldMap = new HashMap<>();
    private Map<Integer, String> headerIndexMap = new HashMap<>();
    private boolean headerExists = false;
    private int noOfRecordsPerExecutor = 5000;

    private Map<String, Object> valueCache = new HashMap<>();

    public ParserVO() {
    }

    public ParserVO(ParserVO parserVO) {
        this.headerDelimeter = parserVO.getHeaderDelimeter();
        this.groupBy = parserVO.getGroupBy();
        this.dateFormat = parserVO.getDateFormat();
        this.dateTimeFormat = parserVO.getDateTimeFormat();
        this.actionIndex = parserVO.getActionIndex();
        this.excludeActions = parserVO.getExcludeActions();
        this.processOnError = parserVO.isProcessOnError();
        this.supperClassLevel = parserVO.getSupperClassLevel();
        this.headerFieldMap = parserVO.getHeaderFieldMap();
        this.headerIndexMap = parserVO.getHeaderIndexMap();
        this.headerExists = parserVO.isHeaderExists();
        this.noOfRecordsPerExecutor = parserVO.getNoOfRecordsPerExecutor();
    }

    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * default date format
     * @return
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    /**
     * default time foramt.
     * @return
     */
    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public String getHeaderDelimeter() {
        return headerDelimeter;
    }

    /**
     * Can change header delimiter.
     * @return
     */
    public void setHeaderDelimeter(String headerDelimeter) {
        this.headerDelimeter = headerDelimeter;
    }

    public String getGroupBy() {
        return groupBy;
    }

    /**
     * group by field.
     * if request map, map will group by values in this column.
     * @return
     */
    public void setGroupBy(String headerAction) {
        this.groupBy = headerAction;
    }

    public Integer getActionIndex() {
        return actionIndex;
    }

    /**
     * group by index.
     * if request map, map will group by values in this index column.
     * @return
     */
    public void setActionIndex(Integer actionIndex) {
        this.actionIndex = actionIndex;
    }

    public List<String> getExcludeActions() {
        return excludeActions;
    }

    /**
     * this annotation support for exclude action commands.
     * should provide by comma separate string.
     * Ex : A,B,C
     * @return
     */
    public void setExcludeAction(List<String> excludeActions) {
        this.excludeActions = excludeActions;
    }

    public void addExcludeAction(String excludeAction) {
        excludeActions.add(excludeAction);
    }

    public boolean isProcessOnError() {
        return processOnError;
    }

    /**
     * if this set to true process not terminate on error.
     * @return
     */
    public void setProcessOnError(boolean processOnError) {
        this.processOnError = processOnError;
    }

    public void addException(Long lineNumber, ParaserException exception) {
        if (null == exceptionMap) {
            exceptionMap = new ExceptionMap();
        }
        exceptionMap.addException(lineNumber, exception);
    }

    public String getException() {
        if (null != exceptionMap) {
            return exceptionMap.getException();
        }
        return "";
    }

    public String getException(long lineNo) {
        return exceptionMap.getException(lineNo);
    }

    public int getSupperClassLevel() {
        return supperClassLevel;
    }

    /**
     * to indicate how many level should consider when finding fields.
     * @return
     */
    public void setSupperClassLevel(int supperClassLevel) {
        this.supperClassLevel = supperClassLevel;
    }

    public Map<String, String> getHeaderFieldMap() {
        return headerFieldMap;
    }

    public void setHeaderFieldMap(Map<String, String> headerFieldMap) {
        this.headerFieldMap = headerFieldMap;
    }

    public String getAttributeFromHeader(String header) {
        return headerFieldMap.get(header);
    }

    public Map<String, String> getAttributeFromHeaderList(List<String> headers) {
        Map<String, String> result = new HashMap<>();
        for (String header : headers) {
            String attribute = getAttributeFromHeader(header);
            if (null != attribute) {
                result.put(header, attribute);
            }
        }
        return result;
    }

    public Map<Integer, String> getHeaderIndexMap() {
        return headerIndexMap;
    }

    public void setHeaderIndexMap(Map<Integer, String> headerIndexMap) {
        this.headerIndexMap = headerIndexMap;
    }

    public String getAttributeFromHeader(int headerIndex) {
        return headerIndexMap.get(headerIndex);
    }

    public Map<Integer, String>  getAttributeFromHeaderIndexList(List<Integer> headerIndexes) {
        Map<Integer, String>  result = new HashMap<>();
        for (Integer headerIndex : headerIndexes) {
            String attribute = getAttributeFromHeader(headerIndex);
            if (null != attribute) {
                result.put(headerIndex, attribute);
            }
        }
        return result;
    }

    public boolean isHeaderExists() {
        return headerExists;
    }

    public void setHeaderExists(boolean headerExists) {
        this.headerExists = headerExists;
    }

    public int getNoOfRecordsPerExecutor() {
        return noOfRecordsPerExecutor;
    }

    /**
     * no of records per execute.
     * if its 5000 will execute 5000 csv records on single thread.
     * @return
     */
    public void setNoOfRecordsPerExecutor(int noOfRecordsPerExecutor) {
        this.noOfRecordsPerExecutor = noOfRecordsPerExecutor;
    }

    public Map<String, Object> getValueCache() {
        return valueCache;
    }

    public void addCacheValue(String property, Object value) {
        valueCache.put(property, value);
    }

}