package CommunProtocols;


public class AGVProtocol {

    /**
     * 对请求AGV进行移动行为的回应
     * @return
     */
    public String ResponseAGVTransfer(boolean result) {       //对应AGVProtocol.ResponseStatus
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "warehouse",
                "receiver": "agv",
                "message_name": "CallAGVTransfer"
              },
              "result":%b,
            }
            """.formatted(result);
    }
    /**
     * 对请求AGV进行移动行为的回应
     * @return
     */
    public String ResponseAGVComeOn(boolean result) {       //对应AGVProtocol.ResponseStatus
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "warehouse",
                "receiver": "agv",
                "message_name": "CallAGVTransfer"
              },
              "result":%b,
            }
            """.formatted(result);
    }



    public String CallBufferImport(int target_site,String material_type,int material_process_index){
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "warehouse",
                "receiver": "buffer",
                "message_name": "CallBufferImport"
              },
              "body": { 
                "command": "import",
                "parameters": {
                    "target_site":%d
                },
                "material_type":%s,
                "material_process_index":%d,
              }
            }
            """.formatted(target_site,material_type,material_process_index);
    }
    public String CallProductWarehouseImport(String material_type){
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "warehouse",
                "receiver": "buffer",
                "message_name": "CallBufferImport"
              },
              "body": { 
                "command": "import",
                "parameters": {    
                },
                "material_type":%s,
              }
            }
            """.formatted(material_type);
    }

    public String InquirePathInfo() {
        return """
        {
            "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "AGV",
                "receiver": "undefined",
                "message_name": "InquirePathInfo"
            },
            "body": { 
                "command": "get_path_info",
            }
        }
        """;
    }


    /**
     * 回复其他智能体的有关路径信息的查询指令
     * @param path AGV当前的路径信息
     * @param position AGV记录的当前坐标节点(静态的，非实时)
     * @param free_status AGV的空闲状态
     * @return
     */
    public String ReturnPathInfo(String path, int position, boolean free_status) {
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
                "path": "%s",
                "position":%d,
                "free_status":%b,
            }
        }
        """.formatted(path,position,free_status);
    }

    public String CallOtherAvoid(String path){
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "AGV",
                "receiver": "AGV",
                "message_name": "CallOtherAvoid"
              },
              "body": { 
                "command": "avoid",
                "parameters": {
                    "path":"%s",
                },
              }
            }
            """.formatted(path);
    }

    public String ServiceObstacleAvoid(int position,int vector,String path){
        return """
            {
              "position":%d,
              "vector":%d,
              "path":"%s",
            }
            """.formatted(position,vector,path);
    }


}
