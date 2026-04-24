package Behaviours.Warehouse;

import Agents.Warehouse;
import Behaviours.Base.Function;

public class Function_Export extends Function {
    private Warehouse agent;

    public Function_Export(Warehouse agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void onTick() {
        if (this.agent.function_export) {
            System.out.println("仓库执行出库任务！");
            ActionUnitWait("inquire_buffer_free_location");  //先查一下哪个工位台能用
            ActionUnitWait("call_agv_ready");  // 叫车
            ActionUnitWait("control_move");   // 把货物运到出口处
            CombinedActionUnitWait("call_agv_import", "control_convey_export");   // 请求AGV打开传送带,并且打开仓库传送带
            System.out.println("呼叫AGV到达指定位置!");
            ActionUnitWait("call_agv_transfer");  // 呼叫AGV到达指定位置
            System.out.println("Warehouse出库行为结束！");
            this.agent.function_export = false;
        }
    }
}