package com.github.johnsonmoon.jdbc.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 二维表，用于保存查询结果
 */
public class Table implements Iterable<Row> {
	private Map<String, Integer> columns = new LinkedHashMap<String, Integer>();
	private List<Row> rows = new ArrayList<Row>();

	/**
	 * 从jdbc resultset中读取数据构建table
	 * 
	 * @param rs
	 */
	public Table(ResultSet rs) {
		try {
			int columnCount = rs.getMetaData().getColumnCount();
			for (int i = 0; i < columnCount; i++)
				columns.put(rs.getMetaData().getColumnName(i + 1).toLowerCase(), i);

			while (rs.next()) {
				Object[] row = new Object[columnCount];
				for (int i = 0; i < columnCount; i++) {
					row[i] = rs.getObject(i + 1);
				}
				rows.add(new Row(columns, row));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(columns.keySet()).append("\n");
		for (int i = 0; i < rows.size(); i++)
			sb.append(i + 1).append(".").append(rows.get(i).toString()).append("\n");
		return sb.toString();
	}

	/**
	 * 返回指定行指定列的数据
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public Object get(int row, int col) {
		return rows.get(row).get(col);
	}

	/**
	 * 返回指定行指定列的数据
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public Object get(int row, String col) {
		return rows.get(row).get(col);
	}

	/**
	 * 返回数据行数
	 * 
	 * @return
	 */
	public int getRowCount() {
		return rows.size();
	}

	public Row getRow(int row) {
		return rows.get(row);
	}

	/**
	 * 返回数据列数
	 * 
	 * @return
	 */
	public int getColCount() {
		return columns.size();
	}

	public Iterator<Row> iterator() {
		return rows.iterator();
	}
}
