package com.github.johnsonmoon.jdbc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by xuyh at 2019/9/19 15:49.
 */
public class JdbcUtils {
    private static Logger logger = LoggerFactory.getLogger(JdbcUtils.class);

    /**
     * Execute query sql.
     *
     * @param connection {@link Connection}
     * @param sql        Query sql statement
     * @return rows [{}, {}, {}, {}]
     */
    public static List<Map<String, Object>> query(Connection connection, String sql) {
        if (connection == null) {
            return null;
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        ResultSet resultSet = null;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            List<String> columns = new ArrayList<>();
            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                columns.add(resultSet.getMetaData().getColumnName(i + 1));
            }
            while (resultSet.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (String column : columns) {
                    row.put(column, resultSet.getObject(column));
                }
                dataList.add(row);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        return dataList;
    }

    /**
     * Execute update sql statement with params.
     *
     * @param connection {@link Connection}
     * @param sql        SQL statement
     * @param params     SQL parameters
     * @return affected rows
     */
    public static int update(Connection connection, String sql, Object... params) {
        if (connection == null) {
            return 0;
        }
        int result = 0;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
            }
            result = statement.executeUpdate();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        return result;
    }

    /**
     * Execute update sql transaction.
     *
     * @param connection  {@link Connection}
     * @param sqlParamMap SQL statement with its parameters. {"SQL1":["obj1", "obj2"], "SQL2":["obj1", "obj2", "obj3"]}
     * @return true/false
     */
    public static boolean transaction(Connection connection, LinkedHashMap<String, List<Object>> sqlParamMap) {
        if (connection == null || sqlParamMap == null || sqlParamMap.isEmpty()) {
            return false;
        }
        List<PreparedStatement> statements = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            for (Map.Entry<String, List<Object>> entry : sqlParamMap.entrySet()) {
                String sql = entry.getKey();
                List<Object> params = entry.getValue();
                PreparedStatement statement = connection.prepareStatement(sql);
                if (params != null) {
                    for (int i = 0; i < params.size(); i++) {
                        statement.setObject(i + 1, params.get(i));
                    }
                }
                statements.add(statement);
                if (statement.executeUpdate() <= 0) {
                    throw new RuntimeException("Rollback.");
                }
            }
            connection.commit();
            return true;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            try {
                connection.rollback();
            } catch (Exception ex) {
                logger.warn(ex.getMessage(), ex);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
            for (PreparedStatement statement : statements) {
                try {
                    statement.close();
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        return false;
    }
}
