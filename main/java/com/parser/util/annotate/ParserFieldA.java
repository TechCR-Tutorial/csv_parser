/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package com.parser.util.annotate;

import com.parser.util.Constant;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author chamly
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ParserFieldA {
    public int fieldIndex() default -1;
    public String fieldHeader() default Constant.Symbol.EMPTY_STRING;

}