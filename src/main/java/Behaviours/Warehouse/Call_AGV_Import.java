package Behaviours.Warehouse;

import Agents.Warehouse;
import Behaviours.Base.CommunicationSpecial;
import jade.lang.acl.ACLMessage;

public class Call_AGV_Import extends CommunicationSpecial {
    private Warehouse agent;

    public Call_AGV_Import(Warehouse agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
        condition = this.agent.call_agv_import;
    }

    @Override
    public void VariableReset() {
        this.agent.call_agv_import = false;
    }

    @Override
    public void SomeOperations() {
        System.out.println("message:%n"+message);
        Call_Special(this.agent.provider,this.agent.protocol.CallAGVImport(), ACLMessage.REQUEST);
    }
}

