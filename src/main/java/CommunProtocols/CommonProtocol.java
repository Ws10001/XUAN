package CommunProtocols;


public class CommonProtocol {

    /**
     * 获取AGV的当前位置与状态
     * @return
     */
    public String InquireFreeStatus(){
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
                "command": "get_free_status",
                "parameters": { 
                },
                "operation_List":"",
                "operation_index":"",
              }
            }
            """;
    }

    public String ReturnFreeStatus(boolean status) {
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
                "result": %s,
            }
        }
        """.formatted(String.valueOf(status));
    }

}
