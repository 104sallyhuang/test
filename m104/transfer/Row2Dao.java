package com.m104.transfer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.commons.beanutils.BeanUtils;

public class Row2Dao<T> {
	private Field[] fields;
	private String[] rows;
	private Class outputClass;

	public Row2Dao(ResultSetMetaData rsmd, Class outputClass) throws SQLException {
		// TODO Auto-generated constructor stub
		this.outputClass = outputClass;
		this.fields = outputClass.getDeclaredFields();
		this.rows = new String[rsmd.getColumnCount()];
		for (int _iterator = 0; _iterator < rsmd.getColumnCount(); _iterator++) {
			rows[_iterator] = rsmd.getColumnLabel(_iterator + 1);
		}

	}
	/*
	 * 將rs 中現在的 row 的值寫到  dao bean 的相同名稱屬性上
	 */
	public T fetch(ResultSet rs) {
		T bean = null;
		try {
			bean = (T) this.outputClass.newInstance();
			for (int i = 0; i < this.rows.length; i++) {
				// getting the SQL column name
				String columnName = this.rows[i];
				// reading the value of the SQL column
				Object columnValue;
				columnValue = rs.getObject(i + 1);
				for (Field field : this.fields) {
					if (field.getName().equalsIgnoreCase(columnName) && columnValue != null) {
						BeanUtils.setProperty(bean, field.getName(), columnValue);
						break;
					}
				}
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;
	}

}
