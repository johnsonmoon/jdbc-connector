package xuyihao.database.jdbc.entity;

import java.util.Arrays;
import java.util.Map;

public class Row {
	private Map<String, Integer> columns;
	private Object[] values;
	
	public Row(Map<String, Integer> columns, Object[] values) {
		this.columns = columns;
		this.values = values;
	}

	public Object get(int col) {
		return values[col];
	}
	
	public Object get(String col) {
		return get(checkColumn(col));
	}
	
	private int checkColumn(String col) {
		Integer result = columns.get(col.toLowerCase());
		if (result == null)
			throw new IllegalArgumentException("不存在的数据列：" + col);
		return result;
	}

	@Override
	public String toString() {
		return Arrays.toString(values);
	}	
}
