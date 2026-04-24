package Behaviours.Machine;


import Agents.Buffer;
import Agents.Machine;
import Behaviours.Base.Listener_action;
import org.json.JSONObject;

import java.util.Objects;

public class Listen_Action extends Listener_action {


    JSONObject message_json;
    Machine agent;
    public Listen_Action(Machine agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void ActionSpace() {
        message_json = new JSONObject(message);
        System.out.printf("Machine.listen_ACTION收到message:%n");
        System.out.printf(message);
        ActionByCondition(Objects.equals(message_json.getJSONObject("body").get("command"), "process"),this::Process);
        //ActionByCondition(Objects.equals(message_json.getJSONObject("body").get("command"), "chuck"),this::Chuck);

    }

    private void Process(){
        this.agent.status_free = false;
        complete = true;
        SendMessage();
        this.agent.material_type = mess_act_json.getJSONObject("body").getJSONObject("parameters").getString("material_type");
        this.agent.material_origin_site = mess_act_json.getJSONObject("body").getJSONObject("parameters").getInt("origin_buffer_site");
        if (this.agent.self_machine_type.equals("lathe")){
            ActionUnitWait("control_chuck");
            ActionUnitWait("control_process");
            ActionUnitWait("control_chuck");
        } else if (this.agent.self_machine_type.equals("milling")) {
            ActionUnitWait("control_process");
        }
        this.agent.call_arm_transfer = true;
        this.agent.status_free = true;


    }
}
