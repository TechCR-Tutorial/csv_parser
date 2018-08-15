/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.util.annotate;

import java.util.Map;

public interface ParserValueResolver<T> {

    T resolve(String key, Map<String, Object> valueCache);

}
