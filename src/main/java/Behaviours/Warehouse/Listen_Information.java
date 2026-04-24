package Behaviours.Warehouse;

import Agents.Buffer;
import Agents.Warehouse;
import Behaviours.Base.Listener_Information;

public class Listen_Information extends Listener_Information{
    Warehouse agent;

    public Listen_Information(Warehouse agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void ActionSpace() {

        if (mess_info_json.getJSONObject("body").get("command").equals("get_free_status")) {
            SendMessage(protocol.ReturnFreeStatus(true));
        }
    }
}
