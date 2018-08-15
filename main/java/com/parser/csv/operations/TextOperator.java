/**
 * 
 */
package com.parser.csv.operations;

import java.util.List;

import com.parser.csv.parser.TextBaseOperator;

/**
 * @author Uditha M. Perera Mar 11, 2014
 * 
 */
public class TextOperator extends TextBaseOperator {

	public <T extends Object> byte[] entityListToText(List<String> fieldList,
			List<T> entityList, String fileHeader, String fileFooter,
			boolean enablePropertyNames) throws Exception {
		return exportFile(fieldList, entityList, fileHeader, fileFooter,
				enablePropertyNames);
	}
}
