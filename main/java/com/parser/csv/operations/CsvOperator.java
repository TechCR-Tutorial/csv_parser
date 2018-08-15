/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.csv.operations;

import com.parser.csv.parser.CsvBaseOperator;
import com.parser.csv.vo.CsvVO;
import com.parser.util.Constant;
import com.parser.util.exception.CsvParserException;
import com.parser.util.exception.NotPaserVOAException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * This Operator class distributed for client perform CSV import/export
 * @author chamly
 */
public class CsvOperator extends CsvBaseOperator{

    
    public CsvOperator(CsvVO csvVO) {
        super(csvVO);
    }

    public <T extends CSVSupportable> CsvOperator(Class<T> clazz) throws NotPaserVOAException {
        super(clazz);
    }
    
    /**
     * Process CSV and create entity and group by action if mention. 
     * Headers and entity attributes map according to headers. 
     * Ex : Header - STUDENT NAME then attribute match for studentName
     * @param <T>       CSVSupportable Type.
     * @param clazz     CSVSupportable Type class
     * @param stream    Input Stream to Process
     * @return          Map<String, List<T>> List of T type entities group by mention action. 
     * @throws IOException, InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException
     */
    public <T extends CSVSupportable> Map<String, List<T>> csvToEntityMap(Class<T> clazz, InputStream stream) 
            throws CsvParserException{
        return importFile(clazz, stream);
    }
    
    /**
     * Process CSV and create entity and group by action if mention. 
     * Headers and entity attribute mapping should provide by client. 
     * header map entry should like < CSV Header Name, Entity Attribute Name>
     * @param <T>       CSVSupportable Type.
     * @param clazz     CSVSupportable Type class
     * @param stream    Input Stream to Process
     * @param headerMap Mapping of Header Name's and attribute names. < CSV Header Name, Entity Attribute Name>
     * @return          Map<String, List<T>> List of T type entities group by mention action. 
     * @throws IOException, InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException 
     */
    public <T extends CSVSupportable> Map<String, List<T>> csvToEntityMap(Class<T> clazz, InputStream stream, 
            Map<String, String> headerMap) throws CsvParserException{
        return importFile(clazz, stream, headerMap);
    }
    
    /**
     * Process CSV which haven't headers and create entity and group by action if mention. 
     * Header Index and attribute index mapping should provided by client. 
     * @param <T>       CSVSupportable Type.
     * @param clazz     CSVSupportable Type class
     * @param stream    Input Stream to Process
     * @param headerMap Mapping of Header Name's and attribute names. < Header Index, Entity Attribute Name>
     * @return          Map<String, List<T>> List of T type entities group by mention action. 
     * @throws IOException, InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException
     */
    public <T extends CSVSupportable> Map<String, List<T>> noHeadersCsvToEntityMap(Class<T> clazz, InputStream stream, 
            Map<Integer, String> headerMap) throws CsvParserException{
        return importIndexHeadersFile(clazz, stream, headerMap);
    }
    
    /**
     * Process CSV and create entity list.
     * Ex : Header - STUDENT NAME then attribute match for studentName
     * @param <T>       CSVSupportable Type.
     * @param clazz     CSVSupportable Type class
     * @param stream    Input Stream to Process
     * @return          T type of entity list. 
     * @throws IOException, InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException
     */
    public <T extends CSVSupportable> List<T> csvToEntityList(Class<T> clazz, InputStream stream) throws CsvParserException{
        return csvToEntityMap(clazz, stream).get(Constant.Symbol.SPACE);
    }
    
    /**
     * Process CSV and create entity list. 
     * Headers and entity attribute mapping should provide by client. 
     * header map entry should like < CSV Header Name, Entity Attribute Name>
     * @param <T>       CSVSupportable Type.
     * @param clazz     CSVSupportable Type class
     * @param stream    Input Stream to Process
     * @param headerMap Mapping of Header Name's and attribute names. < CSV Header Name, Entity Attribute Name>
     * @return          T type of entity list. 
     * @throws IOException, InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException 
     */
    public <T extends CSVSupportable> List<T> csvToEntityList(Class<T> clazz, InputStream stream, 
            Map<String, String> headerMap) throws CsvParserException{
        return csvToEntityMap(clazz, stream, headerMap).get(Constant.Symbol.SPACE);
    }
    
    /**
     * Process CSV which haven't headers and create entity list. 
     * Header Index and attribute index mapping should provided by client. 
     * @param <T>       CSVSupportable Type.
     * @param clazz     CSVSupportable Type class
     * @param stream    Input Stream to Process
     * @param headerMap Mapping of Header Name's and attribute names. < Header Index, Entity Attribute Name>
     * @return          T type of entity list. 
     * @throws IOException, InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException
     */
    public <T extends CSVSupportable> List<T> noHeadersCsvToEntityList(Class<T> clazz, InputStream stream, 
            Map<Integer, String> headerMap) throws CsvParserException {
        return noHeadersCsvToEntityMap(clazz, stream, headerMap).get(Constant.Symbol.SPACE);
    }
}