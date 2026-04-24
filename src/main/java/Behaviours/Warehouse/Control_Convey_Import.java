package Behaviours.Warehouse;

import Agents.Warehouse;
import Behaviours.Base.Driver;

public class Control_Convey_Import extends Driver {
    Warehouse agent;
    public Control_Convey_Import(Warehouse agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
        condition = this.agent.control_convey_import;
        order = "import";
        info = "驱动传送带进行收货！";
    }

    @Override
    public void VariableReset() {
        this.agent.control_convey_import = false;
    }
}
