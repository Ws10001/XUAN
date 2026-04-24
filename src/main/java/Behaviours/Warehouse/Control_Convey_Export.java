package Behaviours.Warehouse;

import Agents.BaseAgent;
import Agents.Warehouse;
import jade.core.behaviours.CyclicBehaviour;
import Behaviours.Base.Driver;

public class Control_Convey_Export extends Driver {
    Warehouse agent;
    public Control_Convey_Export(Warehouse agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
        condition = this.agent.control_convey_export;
        order = "export";
        info = "驱动传送带进行出货！";
    }

    @Override
    public void VariableReset() {
        this.agent.control_convey_export = false;
    }
}
