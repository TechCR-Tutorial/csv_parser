/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */
package com.parser.util.annotate;

public interface ParserCustomValueResolverSupplier<T> {
    ParserCustomValueResolver<T> supply();
}
