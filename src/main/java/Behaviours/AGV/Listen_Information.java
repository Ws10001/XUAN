package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Listener_Information;

import java.util.Objects;

public class Listen_Information extends Listener_Information{
    AGV agent;
    public Listen_Information(AGV agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void ActionSpace() {
        System.out.println("AGV.Listen_info收到消息:"+message);
        if (Objects.equals(mess_info_json.getJSONObject("body").getString("command"), "get_free_status"))
            SendMessage(protocol.ReturnFreeStatus(this.agent.status_free));
        if (Objects.equals(mess_info_json.getJSONObject("body").getString("command"), "get_path_info"))
            SendMessage(this.agent.protocol.ReturnPathInfo(this.agent.data_path,this.agent.status_position,this.agent.status_free));
    }
}
