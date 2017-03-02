package xuyihao.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;

import xuyihao.database.jdbc.DBUtils;
import xuyihao.database.jdbc.entity.Row;
import xuyihao.database.jdbc.entity.Table;

/**
 * 
 * @Author Xuyh created at 2016年10月8日 上午10:42:04
 */
public class App {
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	public static void testOracleConnection(){
		Connection connection = DBUtils.createConnection(DBUtils.DBCODE_ORACLE, "10.1.10.114", 1521, "orcl", "system", "system");
		Table table = DBUtils.query(connection, "SELECT  * from v$parameter");
		output(table.toString());
	}

	public static void mysqlConnectionTest() {
		Connection connection = DBUtils.createConnection(DBUtils.DBCODE_MYSQL, "127.0.0.1", 3306, "EBTest", "root",
				"johnsonmoon");
		Table table = DBUtils.query(connection, "show tables");
		output(table.toString());
	}

	public static void oracleConnectionTest() {
		Connection connection = DBUtils.createConnection(DBUtils.DBCODE_ORACLE, "10.1.50.104", 1521, "orcl", "SYSTEM",
				"PassW0rd");
		Table table = DBUtils.query(connection, "SELECT  * from v$parameter");
		output(table.toString());
	}

	public static String getRowValue(Row row, String col){
		String value = null;
		try{
			value = row.get(col).toString();
		}catch (Exception e){

		}
		return value;
	}

	public static void testWuHanOracle(){
		Connection connection = DBUtils.createConnection(DBUtils.DBCODE_ORACLE, "10.1.50.69", 1521, "orcl", "system", "system");
		Table table = DBUtils.query(connection, "select * from carrier.abt_alert where rownum <= " + "10");
		for(Row row : table) {
			output(getRowValue(row, "SITECODE"));
			output(getRowValue(row, "ENTITY_ID"));
			output(getRowValue(row, "ENTITY_NAME"));
			output(getRowValue(row, "ENTITY_ADDR"));
			output(getRowValue(row, "TYPE_CODE"));
			output(getRowValue(row, "DISCRIMINATION"));
			output(getRowValue(row, "SEVERITY"));
			output(getRowValue(row, "COUNT"));
			output(getRowValue(row, "DESCRIPTION"));
			output(getRowValue(row, "RECEIVER_CODE"));
			output(getRowValue(row, "FIRST_OCCURTIME"));
			output(getRowValue(row, "STATUS"));
			output(getRowValue(row, "STATUS_CHANGED_TIME"));
			output(getRowValue(row, "INCIDENT_ID"));
			output(getRowValue(row, "RESOLVE_CODE"));
			output(getRowValue(row, "ACKNOWLEDGED_MESSAGE"));
			output(getRowValue(row, "RESOLVED_MESSAGE"));
			output(getRowValue(row, "ACKNOWLEDGED_USER_ID"));
			output(getRowValue(row, "ACKNOWLEDGED_USER_NAME"));
			output(getRowValue(row, "PROGRESSING_USER_ID"));
			output(getRowValue(row, "PROGRESSING_USER_NAME"));
			output(getRowValue(row, "ENTITY_TYPE"));
			output(getRowValue(row, "ENTITY_TYPENAME"));
			output(getRowValue(row, "MAX_SEVERITY"));
			output(getRowValue(row, "ACKNOWLEDGMENT_TIME"));
			output(getRowValue(row, "DOMAIN_ID"));
		}
	}

	/**
	 * 输入
	 * 
	 * @return
	 */
	public static String input() {
		String back = "";
		try {
			back = reader.readLine();
		} catch (IOException e) {
			output(e.getMessage());
		}
		return back;
	}

	/**
	 * 输出
	 * 
	 * @param message
	 */
	public static void output(String message) {
		System.out.println(message);
	}
}
