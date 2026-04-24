package Behaviours.Machine;


import Agents.Machine;
import Behaviours.Base.Driver;

public class Control_Chuck extends Driver {

    Machine agent;
    public Control_Chuck(Machine agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
        condition = this.agent.control_chuck;
        if (condition)
        {
            if (this.agent.chuck_grap) order = "chuck-release";
            else order = "chuck-grab";
            this.agent.chuck_grap = !this.agent.chuck_grap;
            info = "驱动底层夹爪进行"+order;
        }


    }

    @Override
    public void VariableReset() {
        this.agent.control_chuck= false;
    }

}
