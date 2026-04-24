package Behaviours.Warehouse;

import Agents.AGV;
import Agents.Warehouse;
import Behaviours.Base.Communication;
import jade.lang.acl.ACLMessage;

public class Call_AGV_Ready extends Communication {
    private Warehouse agent;

    public Call_AGV_Ready(Warehouse agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
        condition = this.agent.call_agv_ready;
        service_name = AGV.Service_TRANSFER;
        selector_type = SelectorType.RANDOM;
    }

    @Override
    public void SomeOperations() {
        System.out.println("message:%n"+message);
        Call_Manual(provider,this.agent.protocol.CallAGVComeOn(this.agent.export_site), ACLMessage.REQUEST);
        this.agent.provider = provider;   //保留AGV对象供送货用
    }
    @Override
    public void VariableReset() {
        this.agent.call_agv_ready = false;
    }


}

