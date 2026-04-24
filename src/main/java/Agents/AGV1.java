package Agents;

import Behaviours.AGV.Control_Importcoy;
import Behaviours.AGV.Control_Outbound;
import Behaviours.AGV.Listen_Information;
import Behaviours.AGV.Listening_Action;
import jade.core.behaviours.ThreadedBehaviourFactory;

public class AGV1 extends AGV {

    @Override
    protected void addBehaviours(ThreadedBehaviourFactory tbf) {

        // 添加Listen_Information行为
        addBehaviour(tbf.wrap(new Listen_Information(this)));

        // 添加Control_Importcoy行为
        addBehaviour(tbf.wrap(new Control_Importcoy(this)));

        // 添加Control_Outbound行为
        addBehaviour(tbf.wrap(new Control_Outbound(this)));

        // 添加Listening_Action行为
        addBehaviour(tbf.wrap(new Listening_Action(this)));

    }

}
