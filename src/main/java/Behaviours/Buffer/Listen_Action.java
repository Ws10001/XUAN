package Behaviours.Buffer;


import Agents.Buffer;
import Behaviours.Base.Listener_action;
import org.json.JSONObject;

import java.util.Objects;

public class Listen_Action extends Listener_action {


    JSONObject message_json;
    Buffer agent;
    public Listen_Action(Buffer agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void ActionSpace() {
        message_json = new JSONObject(message);
        System.out.printf("buffer.listen_ACTION收到message:%n");
        System.out.printf(message);
        ActionByCondition(Objects.equals(message_json.getJSONObject("body").get("command"), "import"),this::Import);
        ActionByCondition(Objects.equals(message_json.getJSONObject("body").get("command"), "complete"),this::Complete);

    }

    private void Import(){
        this.agent.task_import_location = message_json.getJSONObject("body").getJSONObject("parameters").getInt("target_site");  //拿到AGV的点位索引值
        this.agent.task_import_location = (this.agent.task_import_location -10)-((this.agent.task_import_location -10)/3)-1;        //映射为工位台的台位索引值（C#是从1计数，但在JAVA中，我们统一从0计数）
        this.agent.material_type_matrix[this.agent.task_import_location] = mess_act_json.getJSONObject("body").getString("material_type");
        this.agent.material_process_index_matrix[this.agent.task_import_location] = mess_act_json.getJSONObject("body").getInt("material_process_index");
        ActionUnitWait("control_import");
        complete = true;
    }
    private void Complete(){
        System.err.println("Agent Buffer 接收到 compete 信息: "+message_json);
        int index = message_json.getJSONObject("body").getJSONObject("parameters").getInt("site");
        System.err.println("compete 信息 指定自增状态的库位的编号为: "+ index);
        this.agent.material_process_index_matrix[index] ++;
        this.agent.location_status[index] ++;
        System.out.printf("台位%d上的物料工序加1，当前为:%d ,台位状态:%d  %n  ",index,this.agent.material_process_index_matrix[index],this.agent.location_status[index]);
        complete = true;
    }
}
