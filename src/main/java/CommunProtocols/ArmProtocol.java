package CommunProtocols;


public class ArmProtocol {


    public String CallMachineProcess(String material_type,int origin_buffer_site){
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "Arm",
                "receiver": "Machine",
                "message_name": "CallMachineProcess"
              },
              "body": { 
                "command": "process",
                "parameters": {
                    "material_type":%s,
                    "origin_buffer_site":%d,
                },
                
              }
            }
            """.formatted(material_type,origin_buffer_site);
    }

    public String CallBufferProcessComplete(int origin_buffer_site){
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "Arm",
                "receiver": "buffer",
                "message_name": "CallBufferProcessComplete"
              },
              "body": { 
                "command": "complete",
                "parameters": {
                    "site":%d,
                },
                
              }
            }
            """.formatted(origin_buffer_site);
    }




}
