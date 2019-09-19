package com.github.johnsonmoon;

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

	public void test114OracleConnection(){
		App.testOracleConnection();
	}

	public void testWuHanOracleQuery(){
		App.testWuHanOracle();
	}
}
