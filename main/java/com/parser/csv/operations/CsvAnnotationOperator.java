/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package com.parser.csv.operations;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVParser;

import com.parser.csv.parser.CsvBaseOperator;
import com.parser.csv.util.CsvToolkit;
import com.parser.csv.vo.CsvVO;
import com.parser.util.Constant;
import com.parser.util.exception.CsvParserException;
import com.parser.util.exception.NotPaserVOAException;
import com.parser.util.exception.NotSupportedException;

/**
 *
 * @author chamly
 */
public class CsvAnnotationOperator extends CsvBaseOperator {

    Class clazz;
    public <T extends CSVSupportable> CsvAnnotationOperator(CsvVO csvVO, Class<T> clazz) {
        super(csvVO);
        this.clazz = clazz;
    }

    public <T extends CSVSupportable> CsvAnnotationOperator(Class<T> clazz) throws NotPaserVOAException {
        super(clazz);
        this.clazz = clazz;
    }
    
    /**
     * Process CSV and create entity and group by action if mention. 
     * Headers and entity attributes map according to headers. 
     * Also Annotated attribute will merge. 
     * Ex : Header - STUDENT NAME then attribute match for studentName
     * @param <T>       CSVSupportable Type.
     * @param clazz     CSVSupportable Type class
     * @param stream    Input Stream to Process
     * @return          Map<String, List<T>> List of T type entities group by mention action. 
     * @throws Exception, InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException
     */
    public <T extends CSVSupportable> Map<String, List<T>> csvToEntityMap(Class<T> clazz, InputStream stream)
            throws CsvParserException {
        return csvToEntityMap(clazz, stream, new HashMap<>());
    }

    /**
     * Process CSV and create entity and group by action if mention.
     * Headers and entity attributes map according to headers.
     * Also Annotated attribute will merge.
     *  @param <T>       CSVSupportable Type.
     * @param clazz     CSVSupportable Type class
     * @param stream    Input Stream to Process
     * @param additionalHeaderMap
     * @return          Map<String, List<T>> List of T type entities group by mention action.
     * @throws Exception, InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException
     */
    public <T extends CSVSupportable> Map<String, List<T>> csvToEntityMap(Class<T> clazz, InputStream stream,
            Map<String, String> additionalHeaderMap)
            throws CsvParserException {
        CSVParser parser = getCSVParserWithHeader(stream);
        Map<Integer, String> headerMap = CsvToolkit.processHeaderMapFromFieldAnnotation(clazz, parser, getCsvVO(),
                additionalHeaderMap);
        return importIndexHeadersFile(clazz, headerMap, parser);
    }


    /**
     * Process CSV which haven't headers and create entity and group by action if mention. 
     * Header Index and attribute index mapping should provided by client. 
     * @param <T>       CSVSupportable Type.
     * @param clazz     CSVSupportable Type class
     * @param stream    Input Stream to Process
     * @return          Map<String, List<T>> List of T type entities group by mention action.
     * @throws Exception, InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException
     */
    public <T extends CSVSupportable> Map<String, List<T>> noHeadersCsvToEntityMap(Class<T> clazz, InputStream stream)
            throws NotSupportedException {
        throw new NotSupportedException();
    }
    
    /**
     * Process CSV and create entity list.
     * Ex : Header - STUDENT NAME then attribute match for studentName
     * @param <T>       CSVSupportable Type.
     * @param clazz     CSVSupportable Type class
     * @param stream    Input Stream to Process
     * @return          T type of entity list. 
     * @throws Exception, InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException
     */
    public <T extends CSVSupportable> List<T> csvToEntityList(Class<T> clazz, InputStream stream) throws CsvParserException {
        return csvToEntityMap(clazz, stream).get(Constant.Symbol.SPACE);
    }

    /**
     * Process CSV and create entity list.
     * Ex : Header - STUDENT NAME then attribute match for studentName
     * @param <T>       CSVSupportable Type.
     * @param clazz     CSVSupportable Type class
     * @param stream    Input Stream to Process
     * @return          T type of entity list.
     * @throws Exception, InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException
     */
    public <T extends CSVSupportable> List<T> csvToEntityList(Class<T> clazz, InputStream stream,
            Map<String, String> additionalHeaderMap) throws CsvParserException {
        return csvToEntityMap(clazz, stream, additionalHeaderMap).get(Constant.Symbol.SPACE);
    }

    /**
     * Process CSV which haven't headers and create entity list. 
     * Header Index and attribute index mapping should provided by client. 
     * @param <T>       CSVSupportable Type.
     * @param clazz     CSVSupportable Type class
     * @param stream    Input Stream to Process
     * @return          T type of entity list.
     * @throws Exception, InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException
     */
    public <T extends CSVSupportable> List<T> noHeadersCsvToEntityList(Class<T> clazz, InputStream stream) throws Exception {
        return noHeadersCsvToEntityMap(clazz, stream).get(Constant.Symbol.SPACE);
    }

}