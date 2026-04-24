package Behaviours.Buffer;

import Agents.Buffer;
import Behaviours.Base.Driver;

public class Control_Export extends Driver {
    private Buffer agent;
    public Control_Export(Buffer agent){
        super(agent);
        this.agent = agent;
    }
    @Override
    public void Bind() {
        condition = this.agent.control_export;
        order    = this.agent.protocol.DriverExport(this.agent.task_export_location+1);   //对C#层的驱动补偿加1
        info     = "驱动Buffer的%d台位进行出货！".formatted(this.agent.task_export_location);
    }
    @Override
    public void VariableReset() {
        this.agent.location_status[this.agent.task_export_location] = 0;  //更新台位的状态为空闲
        this.agent.control_export = false;
        System.out.println("Buffer出货结束，重置标志位！");
//        System.out.println("该工件加工信息:"+this.agent.material_type_matrix[this.agent.task_import_location]);       // 出库后这两行应该不用初始化，先划掉，有需要再写重置的代码
//        System.out.println("该工件加工索引:"+this.agent.material_process_index_matrix[this.agent.task_import_location]);
    }

}
