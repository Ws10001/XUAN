package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Function;

public class Function_Export extends Function {
    private AGV agent;

    public Function_Export(AGV agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void onTick() {
        if (this.agent.function_export) {
            System.out.println("AGV执行出库任务！");
            if (this.agent.status_position == 0)
                CombinedActionUnitWait("call_warehouse_material_import","control_export");
            else if (this.agent.status_position == 6) {
                CombinedActionUnitWait("call_warehouse_product_import","control_export");
            } else CombinedActionUnitWait("call_buffer_import","control_export");
            this.agent.function_export = false;
        }
    }
}