/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.csv.parser;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.parser.csv.operations.CSVSupportable;
import com.parser.csv.vo.CsvVO;

/**
 * @author chamly
 */
public class CsvParserExecutorDataProviderBuilder<T extends CSVSupportable> {

    private Class<T> clazz;
    private CsvVO csvVo;
    private Map<Integer, String> headerMap;
    private List<Field> fields;

    public CsvParserExecutorDataProviderBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    public CsvParserExecutorDataProviderBuilder<T> addCsvVO(CsvVO csvVO) {
        this.csvVo = csvVO;
        return this;
    }

    public CsvParserExecutorDataProviderBuilder<T> addHeaderMap(Map<Integer, String> headerMap) {
        this.headerMap = headerMap;
        return this;
    }

    public CsvParserExecutorDataProviderBuilder<T> addFields(List<Field> fields) {
        this.fields = fields;
        return this;
    }

    public CsvParserExecutorDataProvider<T> build() {
        return new CsvParserExecutorDataProvider<T>() {
            @Override
            public Class<T> getParserClass() {
                return clazz;
            }

            @Override
            public CsvVO getCsvVo() {
                return csvVo;
            }

            @Override
            public Map<Integer, String> getHeaderMap() {
                return headerMap;
            }

            @Override
            public List<Field> getFields() {
                return fields;
            }
        };
    }
}
