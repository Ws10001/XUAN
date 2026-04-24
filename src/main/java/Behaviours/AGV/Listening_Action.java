package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Listener_action;

import java.util.Objects;
import java.util.Scanner;

public class Listening_Action extends Listener_action {

    private AGV agent;

    public Listening_Action(AGV agent) {
        super(agent);
        this.agent = agent;
    }

@Override
    public void ActionSpace() {
         System.out.println("AGV.Listen_act收到消息:"+message);
         ActionByCondition(Objects.equals(mess_act_json.getJSONObject("body").get("command"), "transfer"),this::Transfer);
    }
    private void Transfer() {
        if (!this.agent.site_ware_IO) {
            System.err.println("错误:AGV需要移动到仓库出库口！");
            new Scanner(System.in).nextLine();
        }
        ActionUnitWait("import_coy");
        complete = true;
        ActionUnitWait("outbound");
        complete = true;
    }
}
