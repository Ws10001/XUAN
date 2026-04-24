package GUI1;
import org.json.JSONObject;
public class ConfigData {

    JSONObject json;

    
    public JSONObject AGV1() {
        String params = """
                {
                "agent_name":"AGV",
                    "ip":"127.0.0.1",
                    "port":10002,
                    "open_port":11302,
                    "direction":1,
                    "InfoService":"AGV_Info",
                }
                """;
        return new JSONObject(params);
    }
    public JSONObject Warehouse1() {
        String params = """
                {
                "agent_name":"warehouse",
                    "ip":"127.0.0.1",
                    "port":10001,
                    "open_port":11402,
                    "import_site":0,
                    "export_site":2,
                    "DFService":"warehouse",
                    "is_finished_warehouse":false,
                }
                """;
        return new JSONObject(params);
    }
}