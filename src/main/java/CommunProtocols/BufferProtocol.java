package CommunProtocols;


public class BufferProtocol {

    /**
     * 回应获取AGV的当前位置与状态
     * @return
     */
    public String ResponseFreeLocation(int location) {
        return """
        {
            "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "buffer",
                "receiver": "undefined",
                "message_name": "ReturnFreeLocation"
            },
            "body": {
                "result": %d,
            }
        }
        """.formatted(location);
    }

    public String ResponseBufferImport(int location) {
        return """
        {
            "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "buffer",
                "receiver": "undefined",
                "message_name": "ReturnFreeLocation"
            },
            "body": {
                "result": %d,
            }
        }
        """.formatted(location);
    }

    /**
     * 驱动C#进行指定台位进行入库
     * @param location 库位索引
     * @return
     */
    public String DriverImport(int location) {
        return """
        import-%d
        """.formatted(location);
    }

    /**
     * 驱动C#进行指定台位进行出库
     * @param location 库位索引
     * @return
     */
    public String DriverExport(int location) {
        return """
        export-%d
        """.formatted(location);
    }

    public String CallArmTransfer(String machine_name,int origin_site,String material_type,String agent_AID) {
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "Buffer",
                "receiver": "Arm",
                "message_name": "CallArmTransfer"
              },
              "body": { 
                "command": "transfer_machine",
                "operation": "",
                "parameters": { 
                    "machine_name":%s,
                    "machine_name_index":%d,
                    "from":%d,
                },
                "material_type":%s,
                "next_agent_AID":"%s",
              }
            }
            """.formatted(machine_name,Integer.valueOf(machine_name.substring(machine_name.length()-1)),origin_site,material_type,agent_AID);
    }
    /**
     * 请求AGV进行移动
     * @param target 目标点
     * @param next_agent_type         下一智能体类型
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
                "sender": "buffer",
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
                "sender": "buffer",
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

    public String CallAGVImport(){
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "buufer",
                "receiver": "agv",
                "message_name": "CallAGVImport"
              },
              "body": { 
                "command": "import",
              }
            }
            """;
    }
    /**
     * 向自身查询存放类型item_type的可用库位，对应BufferProtocol.ReturnFreeLocation
     * @param machine_type  要查询的设备类型，取值che(车),xi(铣),diao(雕).
     * @return
     */
    public String getSelfFreeLocation(String machine_type){
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "buffer",
                "receiver": "buffer",
                "message_name": "getSelfFreeLocation"
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


}
