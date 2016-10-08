package xuyihao.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;

import xuyihao.database.jdbc.DBUtils;
import xuyihao.database.jdbc.entity.Table;

/**
 * 
 * @Author Xuyh created at 2016年10月8日 上午10:42:04
 */
public class App {
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

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
