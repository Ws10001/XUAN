package Agents;


import Behaviours.Arm.*;
import CommunProtocols.ArmProtocol;
import jade.core.AID;
import jade.core.behaviours.ThreadedBehaviourFactory;
import org.json.JSONObject;
import utilities.Common.DFServiceRegister;

public class Arm extends BaseAgent {
    public ArmProtocol protocol = new ArmProtocol();
    public String Service_B2L;     //buffer to Lathe
    public String Service_L2B;
    public boolean control_tomachine = false;
    public boolean control_tobuffer = false;
    public String control_order = "";
    public String call_lathe_process = "";
    public boolean control_transfer = false;
    public boolean call_machine_process;
    public int target_machine_index;
    public AID provider;
    public int origin_buffer_site;

    public String target_machine;
    public String material_type;
    public boolean call_buffer_process_complete;


    @Override
    protected void setup() {
        JSONObject config = (JSONObject) getArguments()[0];
        System.out.println("Agent " + config.getString("agent_name") + " 正在初始化：");
        name = config.getString("agent_name");
        Service_B2L = config.getJSONObject("DFServices").getString("B2L");
        Service_L2B = config.getJSONObject("DFServices").getString("L2B");
        service_list.add(Service_B2L);
        service_list.add(Service_L2B);
        initial(config.getString("agent_name"), config.getString("ip"), config.getInt("port"));
        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        System.out.println(service_list);
        addBehaviour(tbf.wrap(new DFServiceRegister(this, service_list)));
        addBehaviours(tbf);
    }
    /**
     * 子类通过重写此方法来自定义需要添加的行为组合
     * @param tbf 线程包装工厂
     */
    protected void addBehaviours(ThreadedBehaviourFactory tbf) {
        addBehaviour(tbf.wrap(new Call_Buffer_Process_complete(this)));
        addBehaviour(tbf.wrap(new Call_Machine_Process(this)));
        addBehaviour(tbf.wrap(new Control_Transfer(this)));
        addBehaviour(tbf.wrap(new Listen_Action(this)));
        addBehaviour(tbf.wrap(new Listen_Information(this)));
    }

}
