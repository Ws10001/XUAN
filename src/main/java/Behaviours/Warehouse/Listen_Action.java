package Behaviours.Warehouse;

import Agents.Warehouse;
import Behaviours.Base.Listener_action;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Listen_Action extends Listener_action {
    JSONObject message_json;
    Warehouse agent;

    public Listen_Action(Warehouse agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void ActionSpace() {
        message_json = new JSONObject(message);
        System.out.println("warehouse.listen_ACTION收到message:");
        System.out.println(message);
        ActionByCondition(Objects.equals(message_json.getJSONObject("body").get("command"), "import"), this::Import);
    }

    private void Import() {
        this.agent.status_free = false;
        ActionUnitWait("control_convey_import");
        complete = true;
        SendMessage();
        this.agent.material_type = message_json.getJSONObject("body").getString("material_type");

        // 设置库位
        if (Objects.equals(this.agent.material_type, "falan")) {
            this.agent.move_from_id = "1";
            this.agent.move_to_id = "9";
            this.agent.received_product_num++;
            this.agent.printBlue("接收到一个法兰成品件！，当前累计成品数量:" + this.agent.received_product_num);
        } else if (Objects.equals(this.agent.material_type, "zhou")) {
            this.agent.move_from_id = "1";
            this.agent.move_to_id = "8";
            this.agent.received_product_num++;
            this.agent.printBlue("接收到一个轴成品件！，当前累计成品数量:" + this.agent.received_product_num);
        }

        // 如果是成品库，更新订单状态
        if (this.agent.isFinishedWarehouse) {
            updateOrderStatus(this.agent.material_type);
        }

        ActionUnitWait("control_move");
        this.agent.status_free = true;
    }

    private void updateOrderStatus(String materialType) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // 获取数据库连接
            conn = agent.sqlClient.getConnection();
            // 查询最近的加工中订单（StatusCode = 1）
            String query = "SELECT OrderID FROM MAS_WX.ProductOrder WHERE MaterialType = ? AND StatusCode = 1 LIMIT 1";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, materialType.equals("falan") ? 1 : materialType.equals("zhou") ? 3 : 2);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int orderId = rs.getInt("OrderID");
                // 更新订单状态为“加工完成”（StatusCode = 3）
                String updateQuery = "UPDATE MAS_WX.ProductOrder SET StatusCode = 3 WHERE OrderID = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, orderId);
                updateStmt.executeUpdate();
                updateStmt.close();
                System.out.println("Updated order status to 3 (加工完成) for OrderID: " + orderId);
            } else {
                System.out.println("No matching order found for material_type: " + materialType);
            }
        } catch (SQLException e) {
            System.err.println("Error updating order status: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                // 不关闭 conn，由 SQLClient 管理
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}