package Behaviours.Buffer;

import Agents.BaseAgent;
import Agents.Buffer;
import Behaviours.Base.Function;

public class Function_Export extends Function{
    Buffer agent;
    public Function_Export(Buffer agent) {
        super(agent);
        this.agent = agent;
    }


    @Override
    protected void onTick() {
        if (this.agent.function_export) {
            System.out.printf("Buffer开始台位%d上的物料进行出库！%n", this.agent.task_export_location);
            ActionUnitWait("call_agv_ready");      //调台AGV过来
            CombinedActionUnitWait("call_agv_import", "control_export");   // 请求AGV打开传送带,并且打开传送带
            this.agent.export_process  = BaseAgent.process.getJSONArray(this.agent.material_type_matrix[this.agent.task_export_location]).getString(this.agent.material_process_index_matrix[this.agent.task_export_location]);
            System.out.printf("next process:%s %n",this.agent.export_process);
            if (this.agent.export_process.equals("warehouse")){
                this.agent.export_target_location = 6;                    //直接写死成品库的入库口点位

            }else{
                ActionUnitWait("inquire_self_free_location");   //查询下一个工序对应的台位
            }
            ActionUnitWait("call_agv_transfer");      //调用AGV进行搬运
            this.agent.function_export = false;
        }
    }
}
