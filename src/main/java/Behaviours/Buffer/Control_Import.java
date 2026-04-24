package Behaviours.Buffer;

import Agents.Buffer;
import Behaviours.Base.Driver;

public class Control_Import extends Driver {
    private Buffer agent;
    public Control_Import(Buffer agent){
        super(agent);
        this.agent = agent;
    }
    @Override
    public void Bind() {
        condition = this.agent.control_import;
        order    = this.agent.protocol.DriverImport(this.agent.task_import_location+1);   //对C#层的驱动补偿加1
        info     = "驱动Buffer的%d台位进行收货！".formatted(this.agent.task_import_location);
    }
    @Override
    public void VariableReset() {
        this.agent.location_status[this.agent.task_import_location]++;  //更新台位的状态为锁定待加工
        this.agent.control_import = false;
        System.out.println("Buffer收货结束，重置标志位！");
        System.out.println("该工件加工信息:"+this.agent.material_type_matrix[this.agent.task_import_location]);
        System.out.println("该工件加工索引:"+this.agent.material_process_index_matrix[this.agent.task_import_location]);
    }

}
