package Behaviours.Buffer;

import Agents.Buffer;
import Behaviours.Base.CommunicationSpecial;
import jade.lang.acl.ACLMessage;

public class Call_AGV_Import extends CommunicationSpecial {
    private Buffer agent;

    public Call_AGV_Import(Buffer agent){
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
        Call_Special(this.agent.provider_agv,this.agent.protocol.CallAGVImport(), ACLMessage.REQUEST);
    }
}

