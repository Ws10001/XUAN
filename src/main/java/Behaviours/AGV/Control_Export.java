package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Driver;

public class Control_Export extends Driver {
    private AGV agent;
    public Control_Export(AGV agent){
        super(agent);
        this.agent = agent;
    }
    @Override
    public void Bind() {
        condition = this.agent.control_export;
        order    = this.agent.order_export;
        info     = "驱动AGV进行出货！";
    }
    @Override
    public void VariableReset() {
        this.agent.control_export = false;
        System.out.println("AGV出货结束，重置标志位！");
    }
}
