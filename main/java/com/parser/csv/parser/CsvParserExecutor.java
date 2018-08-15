/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.csv.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.csv.CSVRecord;

import com.parser.csv.operations.CSVSupportable;
import com.parser.csv.vo.CsvVO;
import com.parser.util.Constant;
import com.parser.util.ParserToolkit;
import com.parser.util.exception.CsvParserException;

/**
 * This class for Csv Parser Executor. Process Bean Creation for provided record list. .
 *
 * @author chamly
 */
public class CsvParserExecutor<T extends CSVSupportable> implements Callable<Map<String, List<T>>> {

    private List<CSVRecord> recordList;
    private CsvParserExecutorDataProvider dataProvider;
    private CsvVO csvVO;
    private int executorCount = 0;

    /**
     * @param records - list to process.
     * @param dataProvider - data provider
     */
    public CsvParserExecutor(List<CSVRecord> records, CsvParserExecutorDataProvider dataProvider) {
        this.recordList = records;
        this.dataProvider = dataProvider;
        csvVO = dataProvider.getCsvVo();
    }

    public void setExecutorCount(int executorCount) {
        this.executorCount = executorCount;
    }

    /**
     * Enable callable to use threads.
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, List<T>> call() throws Exception {
        return process();
    }

    /**
     * This method can use direct to process CSVRecords with out threads.
     * @return
     */
    public Map<String, List<T>> process() {
        int actionIndex = csvVO.getActionIndex();
        boolean processOnError = csvVO.isProcessOnError();
        Class<T> clazz = dataProvider.getParserClass();
        Map<Integer, String> headerMap = dataProvider.getHeaderMap();
        List<Field> fields = dataProvider.getFields();

        Map<String, List<T>> result = new HashMap();

        recordList.stream().forEach(record -> {
            String actionCommand = Constant.Symbol.SPACE;
            if (actionIndex > Constant.Values.MINUS_1) {
                actionCommand = record.get(actionIndex);
            }
            if (!csvVO.getExcludeActions().contains(actionCommand)) {
                try {
                    T bean = process(clazz, headerMap, record, processOnError, fields);
                    bean.setParserError(csvVO.getException(record.getRecordNumber()));
                    List<T> processedList = result.get(actionCommand);
                    if (null == processedList) {
                        processedList = new ArrayList<T>();
                        result.put(actionCommand, processedList);
                    }
                    processedList.add(bean);
                } catch (CsvParserException e) {
                    if (processOnError) {
                        csvVO.addException(record.getRecordNumber(), e);
                    } else {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            }
        });
        return result;
    }

    private <T> T process(Class<T> clazz, Map<Integer, String> headerMap, CSVRecord record, boolean processOnError,
                          List<Field> fields) throws CsvParserException {
        Map<String, String> voValueMap = new HashMap<>();
        String value = null;
        String attribute = null;
        try {
            for (Map.Entry<Integer, String> headerEntrySet : headerMap.entrySet()) {
                value = record.get(headerEntrySet.getKey());
                attribute = headerEntrySet.getValue();
                if (!ParserToolkit.isEmpty(attribute)) {
                    voValueMap.put(attribute, value);
                }
            }
        } catch (Exception e) {
            CsvParserException exception =
                    new CsvParserException("While Process Import " + value + ":" + attribute);
            if (processOnError) {
                csvVO.addException(record.getRecordNumber(), exception);
            } else {
                throw exception;
            }
        }

        T bean = ParserToolkit.populateBean(clazz, voValueMap, csvVO, record.getRecordNumber(), fields);
        return bean;
    }
}
