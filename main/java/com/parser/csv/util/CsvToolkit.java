/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.csv.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang.WordUtils;

import com.parser.csv.operations.CSVSupportable;
import com.parser.csv.vo.CsvVO;
import com.parser.util.Constant;
import com.parser.util.ParserToolkit;
import com.parser.util.annotate.ParserFieldA;
import com.parser.util.exception.CsvParserException;
import com.parser.util.exception.NotPaserVOAException;
import com.parser.util.vo.ParserVO;

/**
 * @author chamly
 */
public class CsvToolkit {

    private static String CLASS_NAME = "CsvToolkit:";

    public static <T extends CSVSupportable> CsvVO processCsvVO(Class<T> clazz) throws NotPaserVOAException {
        ParserVO parserVO = ParserToolkit.processParserVO(clazz);
        CsvVO csvVO = new CsvVO(parserVO);
        return csvVO;
    }

    public static CSVParser getCsvParser(InputStream stream, boolean withHeader) throws CsvParserException {
        try {
            Reader in = new InputStreamReader(stream);
            CSVFormat format = null;
            if (withHeader) {
                format = CSVFormat.EXCEL.withFirstRecordAsHeader();
            }
            return format.parse(in);
        } catch (IOException e) {
            throw new CsvParserException(CLASS_NAME + "Error On Getting Lable Csv Parser");
        }
    }

    public static Map<Integer, String> getHeaderIndexMap(CSVParser csvParser, Map<String, String> headerMap,
                                                         CsvVO vo) throws CsvParserException {
        vo.setHeaderFieldMap(headerMap);
        vo.setHeaderExists(true);
        Map<String, Integer> csvHeaderIndex = csvParser.getHeaderMap();
        Map<Integer, String> indexMap = new TreeMap<Integer, String>();
        for (String header : headerMap.keySet()) {
            try {
                int headerIndex = csvHeaderIndex.getOrDefault(header, -1);
                if (null != vo.getGroupBy() && vo.getActionIndex() == -1 ) {
                    if (vo.getGroupBy().equalsIgnoreCase(header)) {
                        vo.setActionIndex(headerIndex);
                    }
                }
                if (headerIndex > -1) {
                    indexMap.put(headerIndex, headerMap.get(header));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new CsvParserException("Error On processing Header : " + header);
            }
        }
        return indexMap;
    }

    public static <T extends CSVSupportable> Map<String, String> processHeaderMap(CSVParser csvParser)
            throws CsvParserException {
        Map<String, String> labelMap = new HashMap<String, String>();
        Map<String, Integer> labels = csvParser.getHeaderMap();

        if (null != labels) {
            String value = null;
            try {
                for (Map.Entry<String, Integer> headerEntry : labels.entrySet()) {
                    value = headerEntry.getKey();
                    String originalLabel = value;
                    if (value == null) {
                        labelMap.put(originalLabel, Constant.Symbol.EMPTY_STRING);
                        value = Constant.Symbol.EMPTY_STRING;
                    }
                    value = value.toLowerCase();
                    value = value.replaceAll(Constant.Symbol.SPACE, Constant.Symbol.UNDERSCORE);
                    value = WordUtils.capitalizeFully(
                            value, new char[]{Constant.Symbol.UNDERSCORE_CHAR}).replaceAll(
                            Constant.Symbol.UNDERSCORE, Constant.Symbol.EMPTY_STRING);
                    labelMap.put(originalLabel, org.apache.commons.lang.StringUtils.uncapitalize(value));
                }
            } catch (Exception e) {
                throw new CsvParserException(CLASS_NAME + "Error While Processing Header Map Label : " + value);
            }
        }
        return labelMap;
    }

    public static <T extends CSVSupportable> Map<Integer, String> processHeaderMapFromFieldAnnotation(
            Class<T> clazz, CSVParser parser, CsvVO csvVO, Map<String, String> headerMap) throws CsvParserException {

        Map<String, String> processedHeaderMap = processHeaderMap(parser);
        processedHeaderMap.putAll(headerMap);
        Map<Integer, String> processedIndexMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ParserFieldA.class)) {
                ParserFieldA fieldA = field.getAnnotation(ParserFieldA.class);
                int headerIndex = fieldA.fieldIndex();
                if (headerIndex < 0) {
                    processedHeaderMap.put(fieldA.fieldHeader(), field.getName());
                }
                if (headerIndex > -1) {
                    processedIndexMap.put(headerIndex, field.getName());
                }
            }
        }

        Map<Integer, String> headerIndexMap = getHeaderIndexMap(parser, processedHeaderMap, csvVO);
        headerIndexMap.putAll(processedIndexMap);

        return headerIndexMap;
    }
}
