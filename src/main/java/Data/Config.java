package Data;

import org.json.JSONObject;

public class Config {
    JSONObject json;
    public JSONObject Warehouse_Material(){
        String params = """
                {
                "agent_name":"warehouse_material",
                "ip":"127.0.0.1",
                "port":10402,
                "open_port":11402,
                "import_site":0,
                "export_site":2,
                "DFService":"warehouse_material",
                "is_finished_warehouse":false,
                }
                """;
        return new JSONObject(params);
    }

    public JSONObject Warehouse_Product(){
        String params = """
                {
                "agent_name":"warehouse_product",
                "ip":"127.0.0.1",
                "port":10401,
                "open_port":11401,
                "import_site":6,
                "export_site":7,
                "DFService":"warehouse_product",
                "is_finished_warehouse":true,
                }
                """;
        return new JSONObject(params);
    }

    public JSONObject AGV2(int direction){
        String params = """
                {
                "agent_name":"AGV2",
                "ip":"127.0.0.1",
                "port":10302,
                "open_port":11302,
                "direction":%d,
                "InfoService":"AGV2_Info",
                }
                """.formatted(direction);
        return new JSONObject(params);
    }
    public JSONObject AGV1(int direction){
        String params = """
                {
                "agent_name":"AGV1",
                "ip":"127.0.0.1",
                "port":10301,
                "open_port":11301,
                "direction":%d,
                "InfoService":"AGV1_Info",
                }
                """.formatted(direction);
        return new JSONObject(params);
    }

    public JSONObject Buffer(){
        String params = """
                {
                "agent_name":"buffer",
                "ip":"127.0.0.1",
                "port":10201,
                "open_port":11201,
                }
                """;
        return new JSONObject(params);
    }

    public JSONObject Arm_che(){
        String params = """
                {
                "agent_name":"arm_che",
                "ip":"127.0.0.1",
                "port":10102,
                "DFServices":{
                    "B2L":"arm_buffer_lathe",
                    "L2B":"arm_lathe_buffer",
                },
        }
        """;
        return new JSONObject(params);
    }
    public JSONObject Arm_xi(){
        String params = """
                {
                "agent_name":"arm_xi",
                "ip":"127.0.0.1",
                "port":10105,
                "DFServices":{
                    "B2L":"arm_buffer_milling",
                    "L2B":"arm_milling_buffer",
                },
                }
                """;
        return new JSONObject(params);
    }
    public JSONObject Arm_diao(){
        String params = """
                {
                "agent_name":"arm_diao",
                "ip":"127.0.0.1",
                "port":10108,
                "DFServices":{
                    "B2L":"arm_buffer_engrave",
                    "L2B":"arm_engrave_buffer"
                },
                }
                """;
        return new JSONObject(params);
    }

    public JSONObject Lathe1(){
        String params = """
                {
                "agent_name":"lathe1",
                "ip":"127.0.0.1",
                "port":10101,
                "type":"lathe",
                "index":1,
                "DFServices":{
                    "process":"lathe_process",
                },
                }
                """;
        return new JSONObject(params);
    }
    public JSONObject Lathe2(){
        String params = """
                {
                "agent_name":"lathe2",
                "ip":"127.0.0.1",
                "port":10103,
                "type":"lathe",
                "index":2,
                "DFServices":{
                    "process":"lathe_process",
                },
                }
                """;
        return new JSONObject(params);
    }
    public JSONObject Milling1(){
        String params = """
                {
                "agent_name":"milling1",
                "ip":"127.0.0.1",
                "port":10104,
                "type":"milling",
                "index":1,
                "DFServices":{
                    "process":"milling_process",
                },
                }
                """;
        return new JSONObject(params);

    }
    public JSONObject Milling2() {
        String params = """
                {
                "agent_name":"milling2",
                "ip":"127.0.0.1",
                "port":10106,
                "type":"milling",
                "index":2,
                "DFServices":{
                    "process":"milling_process",
                },
                }
                """;
        return new JSONObject(params);
    }
}

