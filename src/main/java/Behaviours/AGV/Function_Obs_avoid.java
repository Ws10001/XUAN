package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Function;

public class Function_Obs_avoid extends Function {
    private AGV agent;

    public Function_Obs_avoid(AGV agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void onTick() {
        if (this.agent.function_obs_avoid) {
            System.out.println("AGV执行避障功能！");
            while (this.agent.status_position != this.agent.status_target){
                ActionUnitWait("service_pathplanning");    // 生成理论路径
                ActionUnitWait("inquire_other_path_info");    // 查询另一台AGV的状态
                System.out.println("路径规划完成，service_pathplanning已经重置！");
                ActionUnitWait("control_move");
            }
            this.agent.function_obs_avoid = false;
        }
    }
}