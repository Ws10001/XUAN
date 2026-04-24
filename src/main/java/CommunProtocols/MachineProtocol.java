package CommunProtocols;


public class MachineProtocol {


    public String CallArmTransfer(int machine_index,int origin_site,String material_type) {
        return """
            {
              "headers": {
                "protocol_version": "1.0",
                "timestamp": "2024-11-08T10:00:00Z",
                "sender": "Machine",
                "receiver": "Arm",
                "message_name": "CallArmTransfer"
              },
              "body": { 
                "command": "transfer_buffer",
                "operation": "",
                "parameters": { 
                    "machine_index":%d,
                    "origin_site":%d,
                },
                "material_type":%s,
              }
            }
            """.formatted(machine_index,origin_site,material_type);
    }


}
