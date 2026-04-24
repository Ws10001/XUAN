package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Driver;

public class Control_Import extends Driver {
    private AGV agent;
    public Control_Import (AGV agent){
        super(agent);
        this.agent = agent;
    }
    @Override
    public void Bind() {
        condition = this.agent.control_import;
        order    = this.agent.order_import;
        info     = "驱动AGV进行收货！";
    }
    @Override
    public void VariableReset() {
        this.agent.control_import = false;
        System.out.println("AGV收货结束，重置标志位！");
    }
}
