package com.fantasy.util;

import com.fantasy.exception.BizException;

import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBFSqlUtils {
    public static void main(String[] args) throws BizException {
        //查询
//        String sql = "select recno from MPORINTP where recno like ? order by recno desc limit 1";
        String sql = "select * from MATER";
        List<Object> params = new ArrayList<>();
//        params.add("2404%");
        List<Map<String, Object>> maps = executeQuerySqlListResult("D:\\project\\My\\Feeding\\src\\main\\resources\\dbf\\new1", sql, params);
        for (Map<String, Object> map : maps) {
            map.forEach((k,v) -> {
                System.out.print(k + ": "  + v + " ");
            });
            System.out.println();
        }
        //插入
//        List<Object> params = new ArrayList<>();
//        params.add(1);
//        params.add("1");
//        params.add("1");
//        params.add("1");
//        params.add(10);
//        executeInsertSql("D:\\project\\My\\Feeding\\src\\main\\resources\\dbf\\new1", "insert into MATER (MPDNO, UNIT, MPDNE, MPDNE1 ,PARTNO) VALUES (?, ?, ?, ?, ?)", params);

    }

    /**
     * 执行查询语句
     * @param dbfFilePath
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String, Object>> executeQuerySqlListResult(String dbfFilePath,String sql,List<Object> params) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Connection connection = null;
        try {
            // 加载DBF文件的JDBC驱动
            Class.forName("com.hxtt.sql.dbf.DBFDriver");

            // 连接到DBF文件
            connection = DriverManager.getConnection("jdbc:dbf:/" + dbfFilePath);

            // 在这里编写读取和写入DBF的代码
            // 例如，可以使用Statement或PreparedStatement执行SELECT、INSERT、UPDATE等SQL语句
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //封装参数
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    preparedStatement.setObject(i + 1, params.get(i));
                }
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            // 处理结果集
            // 获取 ResultSetMetaData 以获取列信息
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 遍历结果集并打印所有字段的值
            while (resultSet.next()) {
                Map<String, Object> rowMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    rowMap.put(columnName, value);
                }
                resultList.add(rowMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("than 50")){
                throw new RuntimeException("sql执行出现异常");
            }else {
                throw new RuntimeException(e.getMessage());
            }
        } finally {
            try {
                if (connection != null) {
                    // 关闭DBF连接
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    /**
     * 执行查询语句
     * @param dbfFilePath
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String, String>> executeQuerySqlStringResult(String dbfFilePath,String sql,List<Object> params,String charset) {
        List<Map<String, String>> resultList = new ArrayList<>();
        Connection connection = null;
        try {
            // 加载DBF文件的JDBC驱动
            Class.forName("com.hxtt.sql.dbf.DBFDriver");

            // 连接到DBF文件
            connection = DriverManager.getConnection("jdbc:dbf:/" + dbfFilePath);

            // 在这里编写读取和写入DBF的代码
            // 例如，可以使用Statement或PreparedStatement执行SELECT、INSERT、UPDATE等SQL语句
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //封装参数
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    preparedStatement.setObject(i + 1, params.get(i));
                }
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            // 处理结果集
            // 获取 ResultSetMetaData 以获取列信息
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 遍历结果集并打印所有字段的值
            while (resultSet.next()) {
                Map<String, String> rowMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
//                    String value = resultSet.getString(i);
                    byte[] bytes = resultSet.getBytes(i);
                    String value = null;
                    if (bytes != null) {
                        value = new String(bytes, Charset.forName(charset));
                    }
                    rowMap.put(columnName, value);
                }
                resultList.add(rowMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //HXTT DBF Version 7.1 For Evaluation Purpose allows executing not more than 50 queries once
            if (e.getMessage().contains("than 50")){
                throw new RuntimeException("sql执行出现异常");
            }else {
                throw new RuntimeException(e.getMessage());
            }
        } finally {
            try {
                if (connection != null) {
                    // 关闭DBF连接
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    /**
     * 执行插入语句
     * @param dbfFilePath
     * @param sql
     * @param params
     * @return
     */
    public static int executeInsertSql(String dbfFilePath,String sql,List<Object> params) {
        Connection connection = null;
        int rowsAffected = 0;
        try {
            // 加载DBF文件的JDBC驱动
            Class.forName("com.hxtt.sql.dbf.DBFDriver");

            // 连接到DBF文件
            connection = DriverManager.getConnection("jdbc:dbf:/" + dbfFilePath);

            // 在这里编写读取和写入DBF的代码
            // 例如，可以使用Statement或PreparedStatement执行SELECT、INSERT、UPDATE等SQL语句
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //封装参数
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    preparedStatement.setObject(i + 1, params.get(i));
                }
            }

            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("than 50")){
                throw new RuntimeException("sql执行出现异常");
            }else {
                throw new RuntimeException(e.getMessage());
            }
        } finally {
            try {
                if (connection != null) {
                    // 关闭DBF连接
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rowsAffected;
    }

}