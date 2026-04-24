package Agents;

import Behaviours.Machine.*;
import CommunProtocols.MachineProtocol;
import jade.core.behaviours.ThreadedBehaviourFactory;
import org.json.JSONObject;
import utilities.Common.DFServiceRegister;
//import BehavioursText.Warehouse.Outbound;


public class Machine extends BaseAgent{
    public MachineProtocol protocol = new MachineProtocol();
    public static String Service_PROCESS_LATHE = "lathe_process";     //车床
    public static String Service_PROCESS_MILLING = "milling_process";  // 铣床
    public static String Service_PROCESS_ENGRAVE = "engrave_process";  //雕刻机
    public boolean control_process = false;
    public String material_type;
    public boolean chuck_grap = false;
    public boolean control_chuck = false;
    public int material_origin_site;
    public boolean call_arm_transfer;
    public int machine_index;    //机床自身的索引
    public String self_machine_type;   //机床本身的类别
    @Override
    protected void setup(){
        JSONObject config = (JSONObject) getArguments()[0];
        System.out.println("Agent "+config.getString("agent_name")+" 正在初始化：");
        name = config.getString("agent_name");
        machine_index= config.getInt("index");
        self_machine_type = config.getString("type");
        initial(config.getString("agent_name"), config.getString("ip"),config.getInt("port"));
        if(self_machine_type.equals("lathe")) service_list.add(Service_PROCESS_LATHE);
        else if (self_machine_type.equals("milling")) service_list.add(Service_PROCESS_MILLING);
        else service_list.add(Service_PROCESS_ENGRAVE);
        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        addBehaviour(tbf.wrap(new DFServiceRegister(this,service_list)));
        addBehaviours(tbf);
    }
    /**
     * 子类通过重写此方法来自定义需要添加的行为组合
     * @param tbf 线程包装工厂
     */
    protected void addBehaviours(ThreadedBehaviourFactory tbf) {

        addBehaviour(tbf.wrap(new Call_Arm_Transfer(this)));
        addBehaviour(tbf.wrap(new Control_Chuck(this)));
        addBehaviour(tbf.wrap(new Control_Process(this)));
        addBehaviour(tbf.wrap(new Listen_Action(this)));
        addBehaviour(tbf.wrap(new Listen_Information(this)));

    }
}



