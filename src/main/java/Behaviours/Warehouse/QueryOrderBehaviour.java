package Behaviours.Warehouse;

import Agents.Warehouse;
import jade.core.behaviours.TickerBehaviour;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryOrderBehaviour extends TickerBehaviour {
    private final Warehouse agent;

    public QueryOrderBehaviour(Warehouse agent) {
        super(agent, 1000); // 每秒查询一次
        this.agent = agent;
    }

    @Override
    public void onTick() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // 获取 SQLClient 的 Connection 对象
            conn = agent.sqlClient.getConnection();
            String query = "SELECT OrderID, MaterialType, ProcessParams FROM MAS_WX.vw_ProductOrders WHERE StatusCode = 0 LIMIT 1";
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // 获取订单信息
                int orderId = rs.getInt("OrderID");
                int materialType = rs.getInt("MaterialType");
                String processParams = rs.getString("ProcessParams");

                // 设置 agent 属性
                setAgentProperties(materialType, processParams);

                // 更新订单状态为“加工中”（StatusCode = 1）
                String updateQuery = "UPDATE MAS_WX.ProductOrder SET StatusCode = 1 WHERE OrderID = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, orderId);
                updateStmt.executeUpdate();
                updateStmt.close();

                // 设置导出标志
                agent.function_export = true;
                System.out.println("Order found: OrderID=" + orderId + ", MaterialType=" + materialType + ", ProcessParams=" + processParams);
                System.out.println("设置标志位 agent.function_export = true！");
            } else {
                //  System.out.println("No pending orders found in MAS_WX.vw_ProductOrders.");    //频繁展示没必要
            }

        } catch (SQLException e) {
            System.err.println("Database query error: " + e.getMessage());
        } finally {
            // 关闭资源
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                // 不关闭 conn，因为 SQLClient 管理其生命周期
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    private void setAgentProperties(int materialType, String processParams) {
        // 根据 MaterialType 设置 move_type 和 material_type
        switch (materialType) {
            case 1:
                agent.move_type = 1;
                agent.material_type = "falan";
                agent.material_process_index = 0;
                break;
            case 3:
                agent.move_type = 3;
                agent.material_type = "zhou";
                agent.material_process_index = 0;
                break;
            default:
                agent.move_type = 2;
                agent.material_type = "ban";
                agent.material_process_index = 0;
                break;
        }

        // 解析 ProcessParams（JSON 格式）
        try {
            JSONObject params = new JSONObject(processParams);
            if (params.has("diameter")) {
                System.out.println("ProcessParams diameter: " + params.getInt("diameter"));
            }
        } catch (Exception e) {
            System.err.println("Error parsing ProcessParams: " + e.getMessage());
        }
    }
}