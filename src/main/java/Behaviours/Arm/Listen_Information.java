package Behaviours.Arm;


import Agents.Arm;
import Behaviours.Base.Listener_Information;

import java.util.Objects;

public class Listen_Information extends Listener_Information{
    Arm agent;
    public Listen_Information(Arm agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void ActionSpace() {
        System.out.println("Arm.Listen_info收到消息:"+message);
        if (Objects.equals(mess_info_json.getJSONObject("body").getString("command"), "get_free_status"))
            SendMessage(protocol.ReturnFreeStatus(this.agent.status_free));
    }
}
