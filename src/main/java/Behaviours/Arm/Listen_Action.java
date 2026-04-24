package Behaviours.Arm;


import Agents.Arm;
import Behaviours.Base.Listener_action;
import jade.core.AID;
import org.json.JSONObject;

import java.util.Objects;

public class Listen_Action extends Listener_action {


    JSONObject message_json;
    Arm agent;
    public Listen_Action(Arm agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void ActionSpace() {
        message_json = new JSONObject(message);
        System.out.printf("Arm.listen_ACTION收到message:%n");
        System.out.printf(message);
        ActionByCondition(Objects.equals(message_json.getJSONObject("body").get("command"), "transfer_machine"),this::Transfer_M);
        ActionByCondition(Objects.equals(message_json.getJSONObject("body").get("command"), "transfer_buffer"),this::Transfer_B);

    }

    private void Transfer_M(){
        this.agent.status_free = false;
        complete = true;
        SendMessage();     //非交互行为，前反馈。
        //this.agent.target_machine = message_json.getJSONObject("body").getJSONObject("parameters").getString("machine_name");  //拿到目标机床的名称(这条应该用不上，因为类型已经在Arm的Df服务名称中有区分了)
        this.agent.target_machine_index = message_json.getJSONObject("body").getJSONObject("parameters").getInt("machine_name_index");  //拿到目标机床的名称后的序号1或2
        this.agent.origin_buffer_site = message_json.getJSONObject("body").getJSONObject("parameters").getInt("from");    // 源台位索引
        this.agent.material_type = message_json.getJSONObject("body").getString("material_type");    //物料类型
        this.agent.provider = new AID(message_json.getJSONObject("body").getString("next_agent_AID"));    //加工设备的AID
        ActionUnitWait("control_transfer");
        this.agent.call_machine_process = true;
        this.agent.status_free = true;
    }

    private void Transfer_B(){
        this.agent.status_free = false;
        int machine_index = message_json.getJSONObject("body").getJSONObject("parameters").getInt("machine_index");  //拿到机床的名称后的序号1或2
        this.agent.origin_buffer_site = message_json.getJSONObject("body").getJSONObject("parameters").getInt("origin_site");    // 源台位索引
        String material_type = message_json.getJSONObject("body").getString("material_type");    //物料类型
        ActionUnitWait("control_transfer");
        this.agent.call_buffer_process_complete = true;
        complete = true;
        this.agent.status_free = true;

    }
}
