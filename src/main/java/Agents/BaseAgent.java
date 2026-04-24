package Agents;
import jade.core.Agent;
import org.json.JSONArray;
import org.json.JSONObject;
import utilities.Common.SocketClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class BaseAgent extends Agent{
    public static final JSONObject process;

    // 静态代码块初始化 process
    static {
        process = new JSONObject();
        process.put("falan", new JSONArray(new String[]{"che","xi","warehouse"})); // 法兰的工序
        process.put("zhou", new JSONArray(new String[]{"xi","warehouse"})); // 轴的工序
    }

    public boolean status_free = true;
    public SocketClient client;
    public String name;
    public int open_port;           // 手动指令监听端口，测试阶段使用，正式阶段按需取消

    List<String> service_list = new ArrayList<>();        //黄页服务列表
    public void initial(String name,String ip,int port){
        DeviceInfo device = new DeviceInfo();
        device.name = name;
        device.ip = ip;
        device.port = port;
        try{
            client = new SocketClient(device.ip, device.port);
            System.out.println("Agent "+name+" 已连接对应的驱动层！");
        }
        catch (IOException e) {
            System.err.println("Agent "+name+" 无法连接对应的驱动层！");
            System.out.println("请按任意键退出:");
            new Scanner(System.in).nextLine();
            System.exit(0);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {  //自动释放socket资源
            @Override
            public void run() {
                client.Close();
            }
        }));
    }

    public class DeviceInfo {
        public String name;
        public String ip;
        public int port;
    }

    // 下面几个颜色print方法仅方便调试一些信息，不影响MAS任何功能
    public void printBlue(String msg) {
        final String BLUE = "\u001B[34m";
        final String RESET = "\u001B[0m";
        System.out.println(BLUE + msg + RESET);
    }

    public void printRed(String msg) {
        final String RED = "\u001B[31m";
        final String RESET = "\u001B[0m";
        System.out.println(RED + msg + RESET);
    }

    public void printGreen(String msg) {
        final String GREEN = "\u001B[32m";
        final String RESET = "\u001B[0m";
        System.out.println(GREEN + msg + RESET);
    }

}
