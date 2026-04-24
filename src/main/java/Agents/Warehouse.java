package Agents;

import Behaviours.AGV.*;
import Behaviours.Warehouse.*;
import Behaviours.Warehouse.Control_Move;
import Behaviours.Warehouse.Function_Export;
import Behaviours.Warehouse.Listen_Action;
import Behaviours.Warehouse.Listen_Information;
import Behaviours.Warehouse.ManuallyOrder;
import CommunProtocols.WarehouseProtocol;
import jade.core.AID;
import jade.core.behaviours.ThreadedBehaviourFactory;
import org.json.JSONObject;
import utilities.Common.DFServiceRegister;
import utilities.Common.SQLClient;

import java.sql.SQLException;


public class Warehouse extends BaseAgent {
    //public Process process_lib = new Process();
    public int export_site;
    public int import_site;
    public AID provider;

    public WarehouseProtocol protocol = new WarehouseProtocol();
    public String move_from_id = "";
    public String move_to_id = "";
    public int move_type = -1;
    public String material_type;
    public int material_process_index;

    public int buffer_target_location = -1;
    public boolean outbound = true;
    public boolean have_task = false;
    public boolean control_move = false;
    public boolean function_import = false;
    public boolean function_export = false;
    public boolean control_export = false;
    public boolean control_import = false;
    public boolean call_agv_transfer = false;
    public boolean call_agv_ready = false;
    public boolean call_agv_import = false;
    public boolean inquire_buffer_free_location = false;
    public boolean inquire = false;     //测试AID传递用，后续删除
    public boolean control_convey_import = false;
    public boolean control_convey_export = false;
    public int received_product_num = 0;   //已收纳的成品数量，仅暂时作为成品库调试使用

    public boolean isFinishedWarehouse; // 标识是否为成品库

    public SQLClient sqlClient;
    @Override
    protected void setup(){
        JSONObject config = (JSONObject) getArguments()[0];
        name = config.getString("agent_name");
        System.out.println("Agent "+config.getString("agent_name")+" 正在初始化：");
        initial(config.getString("agent_name"), config.getString("ip"),config.getInt("port"));
        this.open_port = config.getInt("open_port");
        this.export_site = config.getInt("export_site");
        this.import_site = config.getInt("import_site");
        this.isFinishedWarehouse = config.getBoolean("is_finished_warehouse"); // 新增：从配置读取
        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        try {
            this.sqlClient  = new SQLClient(
                    "jdbc:mysql://101.132.32.165:7249/MAS_WX",
                    "root",
                    "nuaalabMySQL"
            );
            System.err.println("Agent "+name+" 连接SQL成功！");
        }catch(ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println("Agent "+name+" 连接SQL失败！");
        }
        service_list.add(config.getString("DFService"));
        addBehaviour(tbf.wrap(new DFServiceRegister(this,service_list)));
        addBehaviours(tbf);
    }
  /**
   * 子类通过重写此方法来自定义需要添加的行为组合
   * @param tbf 线程包装工厂
   */
    protected void addBehaviours(ThreadedBehaviourFactory tbf) {
        if (!this.isFinishedWarehouse) {
            addBehaviour(tbf.wrap(new QueryOrderBehaviour(this)));
            System.out.println("Raw Material Warehouse: Added QueryOrderBehaviour");
        } else {
            System.out.println("Finished Warehouse: Skipped QueryOrderBehaviour");
        }
        addBehaviour(tbf.wrap(new Call_AGV_Import(this)));
        addBehaviour(tbf.wrap(new Call_AGV_Ready(this)));
        addBehaviour(tbf.wrap(new Call_AGV_Transfer(this)));
        addBehaviour(tbf.wrap(new Control_Convey_Export(this)));
        addBehaviour(tbf.wrap(new Control_Convey_Import(this)));
        addBehaviour(tbf.wrap(new Control_Move(this)));
        addBehaviour(tbf.wrap(new Function_Export(this)));
        addBehaviour(tbf.wrap(new Inquire_Buffer_Free_Location(this)));
        addBehaviour(tbf.wrap(new Listen_Action(this)));
        addBehaviour(tbf.wrap(new Listen_Information(this)));
        addBehaviour(tbf.wrap(new ManuallyOrder(this)));

    }
 }
