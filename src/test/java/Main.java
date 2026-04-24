import CommunProtocols.WarehouseProtocol;
import CommunProtocols.AGVProtocol;
import org.json.JSONArray;
import org.json.JSONObject;
import utilities.Common.SocketClient;
import Data.Config;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Config c = new Config();
        String b;
        b = c.Arm_che().getJSONObject("DFServices").getString("B2L");

        System.out.println(b);
    }
}

