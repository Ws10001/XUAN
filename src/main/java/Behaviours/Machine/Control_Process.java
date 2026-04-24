package Behaviours.Machine;


import Agents.Machine;
import Behaviours.Base.Driver;

public class Control_Process extends Driver {

    Machine agent;
    public Control_Process(Machine agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
        condition = this.agent.control_process;
        if (condition)
        {

            order = "process-001";
            info = "驱动底层进行加工！加工物料类型:%s".formatted(this.agent.material_type);
        }


    }

    @Override
    public void VariableReset() {
        this.agent.control_process= false;
    }

}
