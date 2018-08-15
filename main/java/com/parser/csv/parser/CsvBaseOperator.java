/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.csv.parser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.parser.csv.operations.CSVSupportable;
import com.parser.csv.util.CsvToolkit;
import com.parser.csv.vo.CsvVO;
import com.parser.parser.BaseOperator;
import com.parser.util.ParserToolkit;
import com.parser.util.exception.CsvParserException;
import com.parser.util.exception.NotPaserVOAException;

/**
 * This class is Base CSV operator for import/export
 *
 * @author chamly
 */
public class CsvBaseOperator extends BaseOperator {

    private static String CLASS_NAME = "CsvBaseOperator:";

    protected CsvBaseOperator(CsvVO csvVO) {
        super(csvVO);
    }

    protected <T extends CSVSupportable> CsvBaseOperator(Class<T> clazz) throws NotPaserVOAException {
        super(CsvToolkit.processCsvVO(clazz));
    }

    /**
     * import csv file
     *
     * @param <T>
     * @param clazz
     * @param stream
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws ParseException
     */
    protected <T extends CSVSupportable> Map<String, List<T>> importFile(Class<T> clazz, InputStream stream)
            throws CsvParserException {
        CSVParser parser = getCSVParserWithHeader(stream);
        Map<String, String> headerLableMap = CsvToolkit.processHeaderMap(parser);
        Map<Integer, String> headerMap = CsvToolkit.getHeaderIndexMap(parser, headerLableMap, getCsvVO());
        return processImport(clazz, parser, headerMap);
    }

    protected <T extends CSVSupportable> Map<String, List<T>> importFile(Class<T> clazz, InputStream stream,
                                                                         Map<String, String> headerMap)
            throws CsvParserException {
        CSVParser parser = getCSVParserWithHeader(stream);
        Map<String, String> headerLableMap = CsvToolkit.processHeaderMap(parser);
        if (null != headerMap) {
            headerLableMap.putAll(headerMap);
        }
        Map<Integer, String> headerIndexMap = CsvToolkit.getHeaderIndexMap(parser, headerLableMap, getCsvVO());
        return processImport(clazz, parser, headerIndexMap);
    }

    /**
     * this time parser should be brand new one.
     *
     * @param clazz
     * @param stream
     * @param headerMap
     * @param <T>
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws ParseException
     * @throws InvocationTargetException
     */
    protected <T extends CSVSupportable> Map<String, List<T>> importIndexHeadersFile(Class<T> clazz, InputStream stream,
                                                                                     Map<Integer, String> headerMap)
            throws CsvParserException {
        CSVParser parser = getCSVParserWithOutHeader(stream);
        return importIndexHeadersFile(clazz, headerMap, parser);
    }

    protected <T extends CSVSupportable> Map<String, List<T>> importIndexHeadersFile(Class<T> clazz, Map<Integer, String> headerMap,
                                                                                     CSVParser parser)
            throws CsvParserException {
        return processImport(clazz, parser, headerMap);
    }

    private <T extends CSVSupportable> Map<String, List<T>> processImport(Class<T> clazz, CSVParser parser,
                                                                          Map<Integer, String> headerMap) throws CsvParserException {
        Map<String, List<T>> result = new HashMap<>();
        getCsvVO().setHeaderIndexMap(headerMap);
        Long startTime = Calendar.getInstance().getTimeInMillis();
        List<CSVRecord> recordList;
        try {
            recordList = parser.getRecords();
        } catch (IOException e) {
            throw new CsvParserException("IO Exception", e);
        }

        List<Field> fields = ParserToolkit.getDeclaredFields(clazz, getCsvVO().getSupperClassLevel());

        CsvParserExecutorDataProviderBuilder<T> dataProviderBuilder = new CsvParserExecutorDataProviderBuilder<T>(clazz).
                addCsvVO(getCsvVO()).addHeaderMap(headerMap).addFields(fields);

        List<List<CSVRecord>> splitList = ParserToolkit.getSplitList(recordList, getCsvVO().getNoOfRecordsPerExecutor());

        List<CsvParserExecutor<T>> executors = new ArrayList<>();
        int i = 0;
        for (List<CSVRecord> records : splitList) {
            CsvParserExecutor<T> executor = new CsvParserExecutor<>(records, dataProviderBuilder.build());
            executor.setExecutorCount(++i);
            executors.add(executor);
        }

        ExecutorService executor = Executors.newWorkStealingPool();

        try {
            executor.invokeAll(executors).stream().forEach(mapFuture -> {
                try {
                    mergMap(result, mapFuture.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        getCsvVO().getValueCache().clear();

        long endTime = Calendar.getInstance().getTimeInMillis();

        System.out.println("Time : " + (endTime - startTime));

        return result;
    }

    private <T> void mergMap(Map<String, List<T>> finalMap, Map<String, List<T>> resultMap) {
        for (Map.Entry<String, List<T>> entrySet : resultMap.entrySet()) {
            List<T> resultList = finalMap.get(entrySet.getKey());
            if (resultList == null) {
                resultList = new ArrayList<>();
                finalMap.put(entrySet.getKey(), resultList);
            }
            resultList.addAll(entrySet.getValue());
        }
    }

    protected CsvVO getCsvVO() {
        return (CsvVO) getParserVO();
    }

    protected final CSVParser getCSVParserWithHeader(InputStream stream) throws CsvParserException {
        return CsvToolkit.getCsvParser(stream, true);
    }

    protected final CSVParser getCSVParserWithOutHeader(InputStream stream) throws CsvParserException {
        return CsvToolkit.getCsvParser(stream, false);
    }

    public String getExceptions() {
        return getCsvVO().getException();
    }

    public void setNoOfRecordsPerExecutor(int noOfRecordsPerExecutor) {
        getCsvVO().setNoOfRecordsPerExecutor(noOfRecordsPerExecutor);
    }
}