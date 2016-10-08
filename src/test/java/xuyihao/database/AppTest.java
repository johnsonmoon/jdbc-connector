package xuyihao.database;

import junit.framework.TestCase;

public class AppTest extends TestCase {
		
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testMysqlConnectionTest() {
		App.mysqlConnectionTest();
	}

	public void testOracleConnectionTest() {
		App.oracleConnectionTest();
	}

}
