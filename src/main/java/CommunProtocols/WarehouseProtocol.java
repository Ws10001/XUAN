package CommunProtocols;


public class WarehouseProtocol {

    /**
     * 请求AGV进行移动
     * @param target 目标点
     * @param material_type           物料类型
     * @param material_process_index  物料工序索引
     * @return
     */
    public String CallAGVTransfer(int target, String material_type,int material_process_index ) {
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "warehouse",
                "receiver": "agv",
                "message_name": "CallAGVTransfer"
              },
              "body": { 
                "command": "transfer",
                "operation": "",
                "parameters": { 
                    "target":%d,
                },
                "material_type":%s,
                "material_process_index":%d,
              }
            }
            """.formatted(target,material_type,material_process_index);
    }

    public String CallAGVComeOn(int target) {
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "warehouse",
                "receiver": "agv",
                "message_name": "CallAGVComeOn"
              },
              "body": { 
                "command": "comeon",
                "operation": "",
                "parameters": { 
                    "target":%d,
                },
              }
            }
            """.formatted(target);
    }

    /**
     * 查询物料数据库中特定类型物料的库位
     * @param item_type 物料类型
     * @return
     */
    public String getMaterialIdWithCondition(int item_type){   //查询SQL存放类型item_type的库位
        return """
               SELECT id FROM MaterialWarehouse WHERE item_type = %d AND status = 1
               """.formatted(item_type);
    }

    /**
     * 查询Buffer存放类型item_type的可用库位，对应BufferProtocol.ReturnFreeLocation
     * @param machine_type  要查询的设备类型，取值che(车),xi(铣),diao(雕).
     * @return
     */
    public String getBufferFreeLocation(String machine_type){
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "warehouse",
                "receiver": "buffer",
                "message_name": "getBufferFreeLocation"
              },
              "body": { 
                "command": "free_location",
                "parameters": { 
                    "machine_type":%s
                },
              }
            }
            """.formatted(String.valueOf(machine_type));
    }

    public String CallAGVImport(){
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "warehouse",
                "receiver": "agv",
                "message_name": "CallAGVImport"
              },
              "body": { 
                "command": "import",
              }
            }
            """;
    }

}
