package Agents;

import Behaviours.Buffer.*;
import CommunProtocols.BufferProtocol;
import jade.core.AID;
import jade.core.behaviours.ThreadedBehaviourFactory;
import org.json.JSONObject;
import utilities.Common.DFServiceRegister;

public class Buffer extends BaseAgent{

    public boolean function_export = false;
    public int task_export_location;
    public boolean call_agv_ready = false;
    public boolean call_agv_import = false;
    public boolean call_agv_transfer = false;
    public boolean inquire_self_free_location = false;
    public int export_target_location;        //物料出台时提供给AGV的目标位置
    public String export_process;             //物料出台时下一步要执行的工序
    public enum TASK_TYPE  {CHE,XI,DIAO};
    public static final String Service_IMPORT = "buffer_import";
    public static final String Service_EXPORT = "buffer_export";
    public BufferProtocol protocol = new BufferProtocol();
    public int[] location_status = {-1,0,0,-1,-1,0,0,-1,-1,0,0,-1,};   // 0为空闲， 1为锁定待入库，2为锁定已入库，3为锁定正加工，4为锁定待出库，-1为禁用

    public int[] location_code_Trans_for_agv = {11,12,14,15,17,18,20,21,23,24,26,27};
    public String[] material_type_matrix = new String[12];  //12个台位各自都要保存工序数据
    public Integer[] material_process_index_matrix = new Integer[12];  //12个台位各自都要保存当前待执行工序的索引
    public boolean control_import = false;
    public boolean control_export = false;
    public int task_import_location = -1;    // 要出入库的台位的索引，取值范围[0-11]



    public boolean call_arm_trans = false;

    public int task_che_location = -1;    // 要进行车床加工的台位的索引，取值范围[0-11]
    public int task_xi_location = -1;    // 要进行铣床加工的台位的索引，取值范围[0-11]
    public int task_diao_location = -1;    // 要进行雕刻加工的台位的索引，取值范围[0-11]
    public boolean function_che_process = false;   //执行车床加工行为的开关
    public boolean function_xi_process = false;
    public boolean function_diao_process = false;
    public boolean call_machine_ready = false;    //搜索机器行为的开关
    public TASK_TYPE call_machine_type;         // 搜索机器类型
    public AID provider_che;                       // 搜索到的车床服务agent的AID
    public AID provider_xi;
    public AID provider_diao;

    public AID provider_agv;


    @Override
    protected void setup(){
        JSONObject config = (JSONObject) getArguments()[0];
        System.out.println("Agent "+config.getString("agent_name")+" 正在初始化：");
        name = config.getString("agent_name");
        this.open_port = config.getInt("open_port");
        initial(config.getString("agent_name"), config.getString("ip"),config.getInt("port"));
        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        service_list.add(Service_EXPORT);
        service_list.add(Service_IMPORT);
        System.out.println(service_list);
        addBehaviour(tbf.wrap(new DFServiceRegister(this,service_list)));
        addBehaviours(tbf);
    }
    /**
     * 子类通过重写此方法来自定义需要添加的行为组合
     * @param tbf 线程包装工厂
     */
    protected void addBehaviours(ThreadedBehaviourFactory tbf) {

        addBehaviour(tbf.wrap(new Call_AGV_Import(this)));
        addBehaviour(tbf.wrap(new Call_AGV_Ready(this)));
        addBehaviour(tbf.wrap(new Call_AGV_Transfer(this)));
        addBehaviour(tbf.wrap(new Call_Arm_Trans(this)));
        addBehaviour(tbf.wrap(new Call_Machine_Ready(this)));
        addBehaviour(tbf.wrap(new Control_Export(this)));
        addBehaviour(tbf.wrap(new Control_Import(this)));
        addBehaviour(tbf.wrap(new Function_Che_Process(this)));
        addBehaviour(tbf.wrap(new Function_CheckTask(this)));
        addBehaviour(tbf.wrap(new Function_Export(this)));
        addBehaviour(tbf.wrap(new Function_Xi_Process(this)));
        addBehaviour(tbf.wrap(new Inquire_Self_Free_Location(this)));
        addBehaviour(tbf.wrap(new Listen_Action(this)));
        addBehaviour(tbf.wrap(new Listen_Information(this)));
        addBehaviour(tbf.wrap(new ManuallyOrder(this)));

    }
}

