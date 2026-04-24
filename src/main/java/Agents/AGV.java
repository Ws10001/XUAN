package Agents;
import Behaviours.AGV.*;
import jade.core.AID;
import jade.core.behaviours.*;
import org.json.JSONObject;
import utilities.Common.DFServiceRegister;
import CommunProtocols.AGVProtocol;


public class AGV extends BaseAgent{
    public static String Service_TRANSFER = "agv_transfer";
    public String Service_Info ;          // 提供信息查询服务
    public AGVProtocol protocol = new AGVProtocol();
    public String pre_buffer_position = "";
    public AID provider;
    public int status_direction = -1;
    public int status_position  = -1;
    public int status_target    = -1;
    public boolean import_coy = false;
    public boolean outbound = false;
    public boolean control_import = false;
    public boolean control_export = false;
    public boolean control_move = false;          //控制进行移动
    public boolean service_pathplanning = false;  // 控制调用路径规划服务
    public boolean service_obstacleavoid = false;  // 控制调用路径规划服务
    public boolean call_buffer_import = false;
    public boolean inquire_other_path_info = false;
    public boolean call_warehouse_import = false;
    public boolean call_other_avoid = false;
    public boolean function_export = false;
    public boolean function_move = false;
    public boolean function_obs_avoid = false;
    public String export_for;
    public String order_import = "import";
    public String order_export = "export";
    public String order_buffer_import = "import";
    public String data_path = "";   // e.g. "1,2,3,4"
    public int[] data_path_vector;   // 路径节点匹配的转向数组，用来配合切割路径时定方向的
    public String data_avoid_path = "";   // e.g. "1,2,3,4"
    public String data_other_path;   // 另一台AGV的路径
    public boolean data_other_free_status;   //另一台AGV的空闲状态
    public String material_type;
    public int material_process_index;
    public boolean call_warehouse_product_import = false;
    public boolean site_ware_IO = true;
    @Override
    protected void setup(){

        JSONObject config = (JSONObject) getArguments()[0];
        System.out.println("Agent "+config.getString("agent_name")+" 正在初始化：");
        name = config.getString("agent_name");
        Service_Info = config.getString("InfoService");      //查询信息服务
        System.out.printf("Agent %s 的 Service_Info为 %s %n",name,Service_Info);
        initial(config.getString("agent_name"), config.getString("ip"),config.getInt("port"));
        this.status_direction = config.getInt("direction");
        this.open_port = config.getInt("open_port");
        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        service_list.add(Service_TRANSFER);
        service_list.add(Service_Info);
        service_list.add("transfer");
        addBehaviour(tbf.wrap(new DFServiceRegister(this,service_list)));
        // 调用可被子类重写的行为添加方法
        addBehaviours(tbf);
    }
    /**
     * 子类通过重写此方法来自定义需要添加的行为组合
     * @param tbf 线程包装工厂
     */
    protected void addBehaviours(ThreadedBehaviourFactory tbf) {
        addBehaviour(tbf.wrap(new Call_Buffer_Import(this)));
        addBehaviour(tbf.wrap(new Call_Other_Avoid(this)));
        addBehaviour(tbf.wrap(new Call_Product_Warehouse_Import(this)));
        addBehaviour(tbf.wrap(new Control_Export(this)));
        addBehaviour(tbf.wrap(new Control_GetPosition(this)));
        addBehaviour(tbf.wrap(new Control_Import(this)));
        addBehaviour(tbf.wrap(new Control_Move(this)));
        addBehaviour(tbf.wrap(new Function_Export(this)));
        addBehaviour(tbf.wrap(new Function_Move(this)));
        addBehaviour(tbf.wrap(new Function_Obs_avoid(this)));
        addBehaviour(tbf.wrap(new Inquire_Other_Path_Info(this)));
        addBehaviour(tbf.wrap(new Listen_Action(this)));
        addBehaviour(tbf.wrap(new Listen_Information(this)));
        addBehaviour(tbf.wrap(new ManuallyOrder(this)));
        addBehaviour(tbf.wrap(new Service_PathPlanning(this)));
        addBehaviour(tbf.wrap(new Service_ObstacleAvoid(this)));

//        addBehaviour(tbf.wrap(new Listener(this)));
//        addBehaviour(tbf.wrap(new Control_Import(this,1000)));
//        addBehaviour(tbf.wrap(new Control_Export(this,1000)));
//        addBehaviour(tbf.wrap(new Control_Move(this,1000)));
//        addBehaviour(tbf.wrap(new Call_BufferImport(this)));
//        addBehaviour(tbf.wrap(new Search_Lathe(this)));
//        addBehaviour(tbf.wrap(new Search_Mill(this)));
//        addBehaviour(tbf.wrap(new Search_Engrave(this)));
//        addBehaviour(tbf.wrap(new Control_GetPosition(this,1000)));
//        addBehaviour(tbf.wrap(new Call_WarehouseInbound(this)));
//        addBehaviour(tbf.wrap(new Call_PathPlan(this)));
    }
}
