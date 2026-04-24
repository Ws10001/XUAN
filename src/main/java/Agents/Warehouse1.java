package Agents;

import jade.core.AID;
import jade.core.behaviours.*;
import org.json.JSONObject;
import utilities.Common.DFServiceRegister;
import Behaviours.Warehouse.*;
import CommunProtocols.WarehouseProtocol;

public class Warehouse1 extends Warehouse {

    @Override
    protected void addBehaviours(ThreadedBehaviourFactory tbf) {

        // 添加Control_Outbound行为
        addBehaviour(tbf.wrap(new Control_Outbound(this)));

        // 添加Call_AGV_TransferNow行为
        addBehaviour(tbf.wrap(new Call_AGV_TransferNow(this)));

    }

}
