/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.util.annotate;

import java.util.List;
import java.util.Map;

import com.parser.util.exception.BaseParserException;

public interface ParserCustomValueResolver<T> {

    default void addRequestHeaders(List<String> propertyList) {

    }

    default T resolveValueByHeaders(Map<String, String> propertyMap, Map<String, Object> valueCache) throws BaseParserException {
        return null;
    };

    /**
     * If file dose not contain headers and processed through index
     * can provide needed indexes.
     * @param propertyList
     */
    default void addRequestIndexes(List<Integer> propertyList) {

    }

    default T resolveValueByIndexes(Map<Integer, String> propertyMap, Map<String, Object> valueCache) throws BaseParserException {
        return null;
    };
}
