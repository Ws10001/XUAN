package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Driver;

public class Control_Outbound extends Driver {

    private AGV agent;

    public Control_Outbound(AGV agent) {
        super(agent);
        this.agent = agent;
    }

@Override
    public void Bind() {
        condition = this.agent.outbound;
        order    = "outbound";
        info = "通知驱动层启动AGV出库1号路径";
    }
@Override
    public void VariableReset() {
        this.agent.outbound = false;
    }
}
