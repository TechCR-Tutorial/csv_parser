/*
 * ================================================================
 *  Copyright          : Chamly Rathnayaka
 * ================================================================
 */

package com.parser.util.annotate;

import com.parser.util.Constant.Symbol;
import com.parser.util.Constant.Formats;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author chamly
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface  ParserVOA {
    /**
     * Can change header delimiter.
     * @return
     */
    String headerDelimeter() default Symbol.EMPTY_STRING;

    /**
     * group by field.
     * if request map, map will group by values in this column.
     * @return
     */
    String groupBy() default Symbol.EMPTY_STRING;

    /**
     * group by index.
     * if request map, map will group by values in this index column.
     * @return
     */
    int actionIndex() default -1;

    /**
     * default date format
     * @return
     */
    String dateFormat() default Formats.DEFUALT_DATE_PATTERN;

    /**
     * default time foramt.
     * @return
     */
    String dateTimeFormat() default Formats.DEFUALT_DATE_TIME_PATTERN;

    /**
     * if this set to true process not terminate on error.
     * @return
     */
    boolean processOnError() default false;

    /**
     * to indicate how many level should consider when finding fields.
     * @return
     */
    int supperClassLevel() default 0;

    /**
     * this annotation support for exclude action commands.
     * should provide by comma separate string.
     * Ex : A,B,C
     * @return
     */
    String excludeActions() default Symbol.EMPTY_STRING;

    /**
     * no of records per execute.
     * if its 5000 will execute 5000 csv records on single thread.
     * @return
     */
    int noOfRecordsPerExecutor() default 5000;

}