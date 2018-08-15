/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.parser.parser.ParserSupportable;
import com.parser.util.annotate.ParserCustomValueProvider;
import com.parser.util.annotate.ParserCustomValueResolver;
import com.parser.util.annotate.ParserCustomValueResolverSupplier;
import com.parser.util.annotate.ParserDateFormat;
import com.parser.util.annotate.ParserDefaultValue;
import com.parser.util.annotate.ParserEmpty;
import com.parser.util.annotate.ParserSkipField;
import com.parser.util.annotate.ParserVOA;
import com.parser.util.annotate.ParserValueProvider;
import com.parser.util.annotate.ParserValueResolver;
import com.parser.util.exception.BaseParserException;
import com.parser.util.exception.CsvParserException;
import com.parser.util.exception.NotPaserVOAException;
import com.parser.util.vo.ParserVO;

/**
 * @author chamly
 */
public class ParserToolkit {
    private static String CLASS_NAME = "ParserToolkit:";

    // -- Start Number Operations --
    public static Integer parseInt(String value) throws CsvParserException {
        if (isEmpty(value)) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new CsvParserException(" Integer Number Format Issue " + value);
        }
    }

    public static Long parseLong(String value) throws CsvParserException {
        if (isEmpty(value)) {
            return 0L;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new CsvParserException(" Long Number Format Issue " + value);
        }
    }

    public static Double parseDouble(String value) throws CsvParserException {
        if (isEmpty(value)) {
            return 0D;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new CsvParserException(" Double Number Format Issue " + value);
        }
    }

    public static Float parseFloat(String value) throws CsvParserException {
        if (isEmpty(value)) {
            return 0F;
        }
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new CsvParserException(" Float Number Format Issue " + value);
        }
    }

    public static Date parseDate(String value, String format) throws CsvParserException {
        if (ParserToolkit.isEmpty(value)) {
            return null;
        }
        if (null == format) {
            format = Constant.Formats.DEFUALT_DATE_PATTERN;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(value);
        } catch (ParseException e) {
            throw new CsvParserException(CLASS_NAME + "Date Parse Issue " + value + ":" + dateFormat);
        }
    }

    public static String formatDate(Date _date, String _format) {

        SimpleDateFormat sdf = new SimpleDateFormat(_format);
        return sdf.format(_date);
    }

    // -- End Number Operations --
    // -- Start String Operations --
    public static boolean isEmpty(String value) {
        return (value == null || value.trim().length() == 0);
    }

    // -- End String Operations --
    // -- Start Entity Operations --
    public static <T> T populateBean(Class<T> clazz, Map<String, String> valueMap, ParserVO vo, long lineNo, List<Field> fields)
            throws CsvParserException {
        T bean = null;
        boolean processOnError = vo.isProcessOnError();
        try {
            bean = clazz.newInstance();
        } catch (Exception e) {
            throw new CsvParserException(CLASS_NAME + "Error On Create New Instance " + clazz);
        }

        for (Field field : fields) {
            String property = field.getName();
            String valueStr = valueMap.get(property);
            if (field.isAnnotationPresent(ParserSkipField.class)) {
                valueStr = null;
            } else if (field.isAnnotationPresent(ParserDefaultValue.class)) {
                valueStr = field.getAnnotation(ParserDefaultValue.class).defaultValue();
            }
            if (null != valueStr || field.isAnnotationPresent(ParserCustomValueProvider.class)) {
                field.setAccessible(true);
                Object value = null;

                if (field.isAnnotationPresent(ParserValueProvider.class)) {
                    Class<? extends ParserValueResolver> resolverClass = field.getAnnotation(ParserValueProvider.class).resolver();
                    try {
                        ParserValueResolver resolver = resolverClass.newInstance();
                        value = resolver.resolve(valueStr, vo.getValueCache());
                    } catch (Exception e) {
                        processError(lineNo, processOnError, vo, new CsvParserException(e.getMessage(), e));
                    }
                } else if (field.getType() == Integer.class) {
                    try {
                        value = parseInt(valueStr);
                    } catch (CsvParserException e) {
                        e.setFieldName(property);
                        if (processOnError) {
                            vo.addException(lineNo, e);
                        } else {
                            throw e;
                        }
                    }
                } else if (field.getType() == Long.class) {
                    try {
                        value = parseLong(valueStr);
                    } catch (CsvParserException e) {
                        e.setFieldName(property);
                        if (processOnError) {
                            vo.addException(lineNo, e);
                        } else {
                            throw e;
                        }
                    }
                } else if (field.getType() == Double.class) {
                    try {
                        value = parseDouble(valueStr);
                    } catch (CsvParserException e) {
                        e.setFieldName(property);
                        if (processOnError) {
                            vo.addException(lineNo, e);
                        } else {
                            throw e;
                        }
                    }
                } else if (field.getType() == Float.class) {
                    try {
                        value = parseFloat(valueStr);
                    } catch (CsvParserException e) {
                        e.setFieldName(property);
                        if (processOnError) {
                            vo.addException(lineNo, e);
                        } else {
                            throw e;
                        }
                    }
                } else if (field.getType() == Date.class) {
                    try {
                        String dateFormat = vo.getDateTimeFormat();
                        if (field.isAnnotationPresent(ParserDateFormat.class)) {
                            dateFormat = field.getAnnotation(ParserDateFormat.class).format();
                        }
                        value = parseDate(valueStr, dateFormat);
                    } catch (CsvParserException e) {
                        try {
                            value = parseDate(valueStr, vo.getDateFormat());
                        } catch (CsvParserException e1) {
                            CsvParserException exception = new CsvParserException("Error While Parse Date " +
                                    valueStr + ":" + property, e);
                            if (processOnError) {
                                vo.addException(lineNo, exception);
                            } else {
                                throw exception;
                            }
                        }
                    }
                } else if (field.getType() == String.class) {
                    value = valueStr;
                } else if (field.getType() != null && field.getType().isEnum()) {
                    Class<? extends Enum> enumClass = (Class<? extends Enum>) field.getType();
                    try {
                        value = Enum.valueOf(enumClass, valueStr);
                    } catch (Exception ex) {
                        Object object = enumClass.getEnumConstants()[0];
                        if (object instanceof ParserValueResolver) {
                            ParserValueResolver resolver = (ParserValueResolver) object;
                            value = resolver.resolve(valueStr, vo.getValueCache());
                        } else {
                            processError(lineNo, processOnError, vo,
                                    new CsvParserException(enumClass.getName() + " Should implement ParserValueResolver" + valueStr));
                        }
                    }
                } else if (field.isAnnotationPresent(ParserCustomValueProvider.class)) {
                    ParserCustomValueProvider valueProvider = field.getAnnotation(ParserCustomValueProvider.class);


                    Class<? extends ParserCustomValueResolver> resolverClass = valueProvider.resolver();
                    Class<? extends ParserCustomValueResolverSupplier> supplierClass = valueProvider.supplier();

                    try {
                        ParserCustomValueResolver resolver = null;
                        if (resolverClass == ParserEmpty.class) {
                            resolver = supplierClass.newInstance().supply();
                        } else {
                            resolver = resolverClass.newInstance();
                        }

                        if (vo.isHeaderExists()) {
                            List<String> requestHeaders = new ArrayList<>();
                            resolver.addRequestHeaders(requestHeaders);
                            Map<String, String> requestProperties = vo.getAttributeFromHeaderList(requestHeaders);
                            Map<String, String> propertyMap = new HashMap<>();
                            for (Map.Entry<String, String> propertyEntry : requestProperties.entrySet()) {
                                propertyMap.put(propertyEntry.getKey(), valueMap.get(propertyEntry.getValue()));
                            }
                            try {
                                value = resolver.resolveValueByHeaders(propertyMap, vo.getValueCache());
                            } catch (BaseParserException e) {
                                processError(lineNo, processOnError, vo, new CsvParserException(e.getMessage(), e));
                            }
                        } else {
                            List<Integer> requestIndexes = new ArrayList<>();
                            resolver.addRequestIndexes(requestIndexes);
                            Map<Integer, String> requestProperties = vo.getAttributeFromHeaderIndexList(requestIndexes);
                            Map<Integer, String> indexMap = new HashMap<>();
                            for (Map.Entry<Integer, String> propertyEntry : requestProperties.entrySet()) {
                                indexMap.put(propertyEntry.getKey(), valueMap.get(propertyEntry.getValue()));
                            }
                            try {
                                value = resolver.resolveValueByIndexes(indexMap, vo.getValueCache());
                            } catch (BaseParserException e) {
                                processError(lineNo, processOnError, vo, new CsvParserException(e.getMessage(), e));
                            }
                        }
                    } catch (Exception e) {
                        throw new CsvParserException("Customer Parser Exception " + resolverClass.getName() + " : " +
                                supplierClass.getName(), e);
                    }
                }
                try {
                    if (null != value) {
                        BeanUtils.setProperty(bean, property, value);
                    }
                } catch (Exception e) {
                    CsvParserException exception = new CsvParserException(CLASS_NAME + "Error While set property" +
                            valueStr + ":" + property);
                    if (processOnError) {
                        vo.addException(lineNo, exception);
                    } else {
                        throw exception;
                    }
                }
            }
        }
        return bean;
    }

    private static void processError(long lineNo, boolean processOnError, ParserVO vo, CsvParserException exception)
            throws CsvParserException {
        if (processOnError) {
            vo.addException(lineNo, exception);
        } else {
            throw exception;
        }
    }

    public static <T extends ParserSupportable> ParserVO processParserVO(
            Class<T> clazz) throws NotPaserVOAException {
        ParserVO parserVO = new ParserVO();
        ParserVOA voA = clazz.getAnnotation(ParserVOA.class);
        if (null == voA) {
            throw new NotPaserVOAException();
        }
        parserVO.setDateFormat(voA.dateFormat());
        parserVO.setDateTimeFormat(voA.dateTimeFormat());
        parserVO.setGroupBy(voA.groupBy());
        parserVO.setHeaderDelimeter(voA.headerDelimeter());
        parserVO.setActionIndex(voA.actionIndex());
        parserVO.setExcludeAction(getStringAsList(voA.excludeActions()));
        parserVO.setProcessOnError(voA.processOnError());
        parserVO.setSupperClassLevel(voA.supperClassLevel());
        parserVO.setNoOfRecordsPerExecutor(voA.noOfRecordsPerExecutor());
        return parserVO;
    }

    // -- End Entity Operations --
    public static Method getDeclaredMethod(Object obj, String name,
                                           Class[] params) {
        Method method = null;
        if (obj != null) {
            try {
                method = obj.getClass().getDeclaredMethod(
                        "get" + StringUtils.capitalize(name), params);
            } catch (NoSuchMethodException e) {
                try {
                    method = getDeclaredMethod(obj.getClass().getSuperclass()
                            .newInstance(), name, params);
                } catch (InstantiationException e1) {
                    // NOP
                } catch (IllegalAccessException e1) {
                    // NOP
                }
            }
        }
        return method;
    }

    public static Field getDeclaredField(Class clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (Exception e) {
            try {
                field = clazz.getSuperclass().getDeclaredField(fieldName);
            } catch (Exception excetion) {
            }
        }
        return field;
    }

    public static List<Field> getDeclaredFields(Class clazz, int supperClassLevel) {
        List<Field> result = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ParserSkipField.class)) {
                result.add(field);
            }
        }
        if (null != clazz && supperClassLevel-- > 0) {
            result.addAll(getDeclaredFields(clazz.getSuperclass(), supperClassLevel));
        }
        return result;
    }

    public static InputStream getFileStream(File file) {
        InputStream reader = null;
        try {
            reader = new FileInputStream(file);
        } catch (Exception ex) {
        }
        return reader;
    }

    // -- Start List Operations --

    /**
     * this imagine delimeter is comma.
     * example string is A,N,B
     *
     * @param value
     * @return
     */
    public static List<String> getStringAsList(String value) {
        return getStringAsList(value, Constant.Symbol.COMMA);
    }

    public static List<String> getStringAsList(String value, String delimiter) {
        delimiter = isEmpty(delimiter) ? Constant.Symbol.COMMA : delimiter;
        if (!isEmpty(value)) {
            return Arrays.asList(value.split(delimiter));
        }
        return Collections.EMPTY_LIST;
    }

    public static <E> List<List<E>> getSplitList(List<E> list, int subListSize) {
        List<List<E>> result = new ArrayList<List<E>>();
        if (null == list && list.isEmpty()) {
            return result;
        }
        int loopNo = 0;
        int resultSubListSize = subListSize < 1 ? 1 : subListSize;
        while ((loopNo * resultSubListSize) < list.size()) {
            int toIndex = ((loopNo * resultSubListSize) + resultSubListSize) > list.size() ? list.size() :
                    ((loopNo * resultSubListSize) + resultSubListSize);
            List<E> subList = list.subList(loopNo * resultSubListSize, toIndex);
            result.add(subList);
            loopNo++;
        }
        return result;
    }

    // -- End List Operations --
}