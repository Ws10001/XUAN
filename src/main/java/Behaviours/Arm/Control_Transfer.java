package Behaviours.Arm;

import Agents.Arm;
import Behaviours.Base.Driver;

public class Control_Transfer extends Driver {

    Arm agent;
    public Control_Transfer(Arm agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
        condition = this.agent.control_transfer;
        if (condition)
        {

            if(this.agent.name.equals("arm_xi"))
                order = "M6-x1-001";
            else
                order = "M1-c1-001";
            info = "驱动Arm的C#进行搬货,目标设备索引：%d   源台位:%d   物料类型:%s".formatted(this.agent.target_machine_index,this.agent.origin_buffer_site,this.agent.material_type);

        }


    }

    @Override
    public void VariableReset() {
        this.agent.control_transfer= false;
    }

}
