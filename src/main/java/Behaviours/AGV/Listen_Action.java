package Behaviours.AGV;
import Agents.AGV;
import Behaviours.Base.Listener_action;
import java.util.Objects;

public class Listen_Action extends Listener_action {

    AGV agent;
    public Listen_Action(AGV agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void ActionSpace() {
        while (!this.agent.status_free) {      //测试阶段使用，用来保证执行行为前AGV必须为空闲，后续验证是否会出现非空闲的问题，如果否，则可以删除这里
            try {
                System.err.println("状态为非空闲，但却收到消息！");
                Thread.sleep(1000);
                break;                        //考虑到多个行为comeon跟import是连续行为，因此，这里仅能提示一下
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("AGV.Listen_act收到消息:"+message);
        ActionByCondition(Objects.equals(mess_act_json.getJSONObject("body").get("command"), "comeon"),this::ComeOn);
        ActionByCondition(Objects.equals(mess_act_json.getJSONObject("body").get("command"), "transfer"),this::Transfer);
        ActionByCondition(Objects.equals(mess_act_json.getJSONObject("body").get("command"), "import"),this::Import);
        ActionByCondition(Objects.equals(mess_act_json.getJSONObject("body").get("command"), "avoid"),this::Avoid);

    }

    private void ComeOn(){
        this.agent.status_free = false;     //"请求过来-收货-送货-出货"之前标志为繁忙，在出货结束时重置为空间
        this.agent.status_target = mess_act_json.getJSONObject("body").getJSONObject("parameters").getInt("target");
        System.out.printf("AGV准备开始移动指令！当前位置:%s   目标位置:%s %n",this.agent.status_position,this.agent.status_target);
        if (this.agent.status_target != this.agent.status_position) {
            System.out.println("目标位置与当前位置不统一，需要进行路径规划，等待service_pathplanning被重置！");
            ActionUnitWait("function_move");
        }else
        {
            System.out.println("目标位置与当前位置统一，不需要进行路径规划！");
        }
        complete = true;   //交互行为，后反馈。
    }
    private void Transfer(){
        complete = true;
        SendMessage();     //非交互行为，前反馈。
        this.agent.status_target = mess_act_json.getJSONObject("body").getJSONObject("parameters").getInt("target");
        System.out.printf("AGV准备开始移动指令！当前位置:%s   目标位置:%s %n",this.agent.status_position,this.agent.status_target);
        ActionUnitWait("function_move");

        this.agent.material_type = mess_act_json.getJSONObject("body").getString("material_type");  //保存工件类型
        this.agent.material_process_index = mess_act_json.getJSONObject("body").getInt("material_process_index");
        //this.agent.export_for = mess_act_json.getJSONObject("body").getString("next_agent_type");   //改为通过AGV当前位置来判定下一智能体对象
        ActionUnitWait("function_export");    //出货
        this.agent.status_free = true;


    }
    private void Import(){   //交互行为，后反馈。
        ActionUnitWait("control_import");
        complete = true;
    }

    private void Avoid(){   //非交互行为，后反馈。
        this.agent.status_free = false;     //"请求过来-收货-送货-出货"之前标志为繁忙，在出货结束时重置为空间
        this.agent.data_other_path = mess_act_json.getJSONObject("body").getJSONObject("parameters").getString("path");
        System.out.printf("AGV准备开始避障指令！%n");
        ActionUnitWait("service_obstacleavoid");
        System.out.println("避障路径规划完成，service_obstacleavoid已经重置！");
        ActionUnitWait("control_move");
        complete = true;
        this.agent.status_free = true;
    }
}
