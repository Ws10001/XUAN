package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Driver;

public class Control_Importcoy extends Driver {

    private AGV agent;

    public Control_Importcoy(AGV agent) {
        super(agent);
        this.agent = agent;
    }

@Override
    public void Bind() {
        condition = this.agent.import_coy;
        order    = "import";
        info = " 通知驱动层启动AGV传送带收货！";
    }
@Override
    public void VariableReset() {
        this.agent.import_coy = false;
    }
}
