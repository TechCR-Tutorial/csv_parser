/**
 * 
 */
package com.parser.csv.parser;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.parser.util.Constant.Formats;
import com.parser.util.Constant.Values;
import com.parser.util.ParserToolkit;

/**
 * @author Uditha M. Perera Mar 11, 2014
 * 
 */
public class TextBaseOperator {
	private final static String JAVA_DATE_TYPE = "java.util.Date";

	protected <T extends Object> byte[] exportFile(List<String> fieldList,
			List<T> _list, String fileHeader, String fileFooter,
			boolean enablePropertyNames) throws IOException,
			InstantiationException, IllegalAccessException,
			NoSuchFieldException, ParseException {
		StringBuilder buffer = new StringBuilder();
		Class params[] = {};
		Object[] paramsObj = {};
		int i = 0;

		// Print Header
		if (fileHeader != null && !fileHeader.equals("")
				&& !fileHeader.equals(" ")) {
			buffer.append(fileHeader);
			buffer.append("\r\n");
		}
		for (T obj : _list) {
			i = 0;
			Object returnValue;
			for (String key : fieldList) {
				String value = null;
				try {
					Method method = ParserToolkit.getDeclaredMethod(obj,
							StringUtils.capitalize(key), params);
					if (method != null) {
						method.setAccessible(true);
						returnValue = method.invoke(obj, paramsObj);
						if (returnValue != null) {
							if (method.getReturnType().getName()
									.equals(JAVA_DATE_TYPE)) {

								value = ParserToolkit.formatDate(
										(Date) returnValue,
										Formats.DEFUALT_DATE_PATTERN);
							} else {
								value = returnValue.toString();
							}
						} else {
							value = Values.EMPTY_STRING;
						}
					}
				} catch (Exception ex) {
					value = Values.EMPTY_STRING;
				}
				if (i > 0) {
					buffer.append(",");
				}

				if (enablePropertyNames)
					buffer.append(key.toUpperCase() + "=" + value);
				else
					buffer.append(value);
				i++;
			}
			buffer.append("\r\n");
		}
		// Print Footer
		if (fileFooter != null && !fileFooter.equals("")
				&& !fileFooter.equals(" ")) {
			buffer.append(fileFooter);
			buffer.append("\r\n");
		}
		return buffer.toString().getBytes();
	}

}
