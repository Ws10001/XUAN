package Behaviours.Buffer;

import Agents.Buffer;
import Behaviours.Base.Function;

public class Function_Che_Process extends Function {
    private Buffer agent;

    public Function_Che_Process(Buffer agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void onTick() {
        if (this.agent.function_che_process) {
            System.out.printf("Buffer开始台位%d上的物料进行车床加工！%n", this.agent.task_che_location);
            ActionProtection("call_machine_ready");         //预防查询设备行为正在运行
            this.agent.call_machine_type = Buffer.TASK_TYPE.CHE;
            ActionUnitWait("call_machine_ready");  //先查一下哪个设备能用
            ActionUnitWait("call_arm_trans");      //调用Arm进行搬运

            this.agent.function_che_process = false;
        }
    }
}