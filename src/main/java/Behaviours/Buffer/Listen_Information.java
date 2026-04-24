package Behaviours.Buffer;

import Agents.Buffer;
import Behaviours.Base.Listener_Information;

public class Listen_Information extends Listener_Information{
    Buffer agent;

    public Listen_Information(Buffer agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void ActionSpace() {
        if (mess_info_json.getJSONObject("body").get("command").equals("free_location"))   //"command": "free_location",
        {
            int item_type = -1;
            int free_location;
            String machine_type = mess_info_json.getJSONObject("body").getJSONObject("parameters").getString("machine_type");
            switch (machine_type){
                case ("che"):{
                    item_type = 1;
                    break;
                }
                case("xi"):{
                    item_type = 2;
                    break;
                }
                case ("diao"):{
                    item_type = 3;
                    break;
                }
                default:System.err.printf("machine_type无法解析，当前值为%s%n",machine_type);
            }
            System.out.printf("buffer接受到的item_type为 %d！",item_type);
            for (free_location = (item_type - 1) * 4; free_location < item_type * 4; free_location++)
                if (this.agent.location_status[free_location] == 0)
                    break;
            System.out.printf("Buffer找到合适的库位:%d %n",free_location);
            this.agent.location_status[free_location] ++;      //更新台位的状态为锁定待入库
            SendMessage(this.agent.protocol.ResponseFreeLocation(this.agent.location_code_Trans_for_agv[free_location]));
        } else if (mess_info_json.getJSONObject("body").get("command").equals("get_free_status")) {
            SendMessage(protocol.ReturnFreeStatus(true));
        }
    }
}
