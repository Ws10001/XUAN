package Behaviours.Warehouse;

import Agents.Warehouse;
import Behaviours.Base.Driver;

public class Control_Outbound extends Driver {

    private Warehouse agent;

    public Control_Outbound(Warehouse agent) {
        super(agent);
        this.agent = agent;
    }

@Override
    public void Bind() {
        condition = this.agent.outbound;
        info = " 通知驱动层启动仓库出库进程！";
        order = "outbound";
    }
@Override
    public void VariableReset() {
        this.agent.outbound = false;
        this.agent.have_task = true;
    }
}
