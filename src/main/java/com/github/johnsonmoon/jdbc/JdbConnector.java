package com.github.johnsonmoon.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.johnsonmoon.jdbc.entity.Table;

/**
 * 数据库工具
 * 
 * @Author Xuyh created at 2016年10月8日 下午4:03:13
 */
public class JdbConnector {
	/**
	 * 数据库类型识别码
	 */
	public static final int DBCODE_ORACLE = 0;
	public static final int DBCODE_DB2 = 1;
	public static final int DBCODE_SQLSERVER = 2;
	public static final int DBCODE_MYSQL = 3;
	public static final int DBCODE_SYBASE = 4;
	public static final int DBCODE_INFORMIX = 5;
	public static final int DBCODE_POSTGRESQL = 6;

	/**
	 * 各种数据库驱动
	 */
	private static final String DRIVER_ORACLE = "oracle.jdbc.OracleDriver";
	private static final String DRIVER_DB2 = "com.ibm.db2.jcc.DB2Driver";
	private static final String DRIVER_SQLSERVER = "net.sourceforge.jtds.jdbc.Driver";
	private static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
	private static final String DRIVER_SYBASE = "com.sybase.jdbc2.jdbc.SybDriver";
	private static final String DRIVER_INFORMIX = "com.informix.jdbc.IfxDriver";
	private static final String DRIVER_POSTGRESQL = "org.postgresql.Driver";

	private static final DBInfo DBINFO_ORACLE = new DBInfo("jdbc:oracle:thin:@//%s:%d/%s");
	private static final DBInfo DBINFO_POSTGRESQL = new DBInfo("jdbc:postgresql://%s:%d/%s");
	private static final DBInfo DBINFO_DB2 = new DBInfo("jdbc:db2://%s:%d/%s");
	private static final DBInfo DBINFO_SQLSERVER = new DBInfo("jdbc:jtds:sqlserver://%s:%d/%s");
	private static final DBInfo DBINFO_MYSQL = new DBInfo("jdbc:mysql://%s:%d/%s");
	private static final DBInfo DBINFO_SYBASE = new DBInfo("jdbc:sybase:Tds:%s:%d/%s");
	private static final DBInfo DBINFO_INFORMIX = new DBInfo("jdbc:informix-sqli://%s:%d/sysuser:INFORMIXSERVER=%s");

	private static String getURL(int code, String host, int port, String name) {
		switch (code) {
		case DBCODE_ORACLE:
			return DBINFO_ORACLE.createUrl(host, port, name);
		case DBCODE_DB2:
			return DBINFO_DB2.createUrl(host, port, name);
		case DBCODE_SQLSERVER:
			return DBINFO_SQLSERVER.createUrl(host, port, name);
		case DBCODE_MYSQL:
			return DBINFO_MYSQL.createUrl(host, port, name);
		case DBCODE_SYBASE:
			return DBINFO_SYBASE.createUrl(host, port, name);
		case DBCODE_INFORMIX:
			return DBINFO_INFORMIX.createUrl(host, port, name);
		case DBCODE_POSTGRESQL:
			return DBINFO_POSTGRESQL.createUrl(host, port, name);
		default:
			return "";
		}
	}

	private static String getDriver(int code) {
		switch (code) {
		case DBCODE_ORACLE:
			return DRIVER_ORACLE;
		case DBCODE_DB2:
			return DRIVER_DB2;
		case DBCODE_SQLSERVER:
			return DRIVER_SQLSERVER;
		case DBCODE_MYSQL:
			return DRIVER_MYSQL;
		case DBCODE_SYBASE:
			return DRIVER_SYBASE;
		case DBCODE_INFORMIX:
			return DRIVER_INFORMIX;
		case DBCODE_POSTGRESQL:
			return DRIVER_POSTGRESQL;
		default:
			return "";
		}
	}

	/**
	 * 建立一个数据库连接
	 * 
	 * @param dbCode DBUtil中的DBCODE
	 * @param host 主机
	 * @param port 端口
	 * @param instanceName 连接数据库实例名
	 * @param username 用户
	 * @param password 密码
	 * @return
	 */
	public static Connection createConnection(int dbCode, String host, int port, String instanceName, String username,
			String password) {
		try {
			Class.forName(getDriver(dbCode));
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}

		try {
			return DriverManager.getConnection(getURL(dbCode, host, port, instanceName), username, password);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	private static class DBInfo {
		private String url;

		public DBInfo(String url) {
			this.url = url;
		}

		public String createUrl(String host, int port, String name) {
			return String.format(url, host, port, name);
		}
	}

	/**
	 * 执行一个数据库查询，返回一个二维表格
	 * 
	 * @param conn
	 * @param sql
	 * @return
	 */
	public static Table query(Connection conn, String sql) {
		ResultSet rs = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			return new Table(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close(statement, rs);
		}
	}

	public static Object queryForValue(Connection conn, String sql) {
		ResultSet rs = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			if (rs.next())
				return rs.getObject(1);
			else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			close(statement, rs);
		}
	}

	/**
	 * 执行一个更新语句
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return 返回更新语句影响的记录数
	 */
	public static int execute(Connection conn, String sql) {
		Statement statement = null;
		try {
			statement = conn.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			close(statement);
		}
	}

	/**
	 * 关闭各种SQL执行资源
	 * 
	 * @param objects
	 */
	public static void close(Object... objects) {
		try {
			for (Object obj : objects) {
				if (obj != null) {
					if (obj instanceof ResultSet)
						((ResultSet) obj).close();
					else if (obj instanceof Connection)
						((Connection) obj).close();
					else if (obj instanceof Statement)
						((Statement) obj).close();
					else
						throw new IllegalArgumentException("无法识别的DB资源：" + obj);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
