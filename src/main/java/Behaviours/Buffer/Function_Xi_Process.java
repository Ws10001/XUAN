package Behaviours.Buffer;

import Agents.Buffer;
import Behaviours.Base.Function;

public class Function_Xi_Process extends Function {
    private Buffer agent;

    public Function_Xi_Process(Buffer agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void onTick() {
        if (this.agent.function_xi_process) {
            System.out.printf("Buffer开始台位%d上的物料进行铣床加工！%n", this.agent.task_xi_location);
            ActionProtection("call_machine_ready");         //预防查询设备行为正在运行
            this.agent.call_machine_type = Buffer.TASK_TYPE.XI;
            ActionUnitWait("call_machine_ready");  //先查一下哪个设备能用
            ActionUnitWait("call_arm_trans");      //调用Arm进行搬运

            this.agent.function_xi_process = false;
        }
    }
}