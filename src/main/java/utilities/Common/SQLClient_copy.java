package utilities.Common;

import java.sql.*;

public class SQLClient_copy {

    private Connection connection;

    // 构造函数，用于初始化数据库连接
    public SQLClient_copy(String url, String user, String password) throws ClassNotFoundException, SQLException {
        // 注册 JDBC 驱动程序
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 打开连接
        connection = DriverManager.getConnection(url, user, password);
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
            // 执行查询
            statement = connection.createStatement();
            resultSet = statement.executeQuery(order);


            // 处理结果
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    hasData = true;

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return hasData;
    }

    public Integer getIdWithCondition(String condition) {
        Statement statement = null;
        ResultSet resultSet = null;
        Integer id = null;


        try {
            // 执行查询
            statement = connection.createStatement();
            resultSet = statement.executeQuery(condition);


            // 处理结果
            if (resultSet.next()) {
                id = resultSet.getInt("id");  // 获取第一个符合条件的 ID
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }
}


