package utilities.Common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class SQLClient {

    private Connection connection;

    // 构造函数，用于初始化数据库连接
    public SQLClient(String url, String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, password);
    }

    // 获取数据库连接
    public Connection getConnection() {
        return connection;
    }

    // 关闭数据库连接
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询方法，返回是否有数据
    public boolean query(String order) {
        Statement statement = null;
        ResultSet resultSet = null;
        boolean hasData = false;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(order);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    hasData = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return hasData;
    }

    // 获取 ID 的方法
    public Integer getIdWithCondition(String condition) {
        Statement statement = null;
        ResultSet resultSet = null;
        Integer id = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(condition);
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    // 执行查询并返回 ResultSet
    public ResultSet executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    // 执行更新（INSERT, UPDATE, DELETE）
    public int executeUpdate(String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(query);
        }
    }
}