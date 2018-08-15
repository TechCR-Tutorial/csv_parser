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
 *@author chamly
 */
public interface CsvParserExecutorDataProvider<T extends CSVSupportable> {

    Class<T> getParserClass();
    CsvVO getCsvVo();
    Map<Integer, String> getHeaderMap();
    List<Field> getFields();
}
