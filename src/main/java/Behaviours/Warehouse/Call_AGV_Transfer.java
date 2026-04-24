package Behaviours.Warehouse;

import Agents.Warehouse;
import Behaviours.Base.CommunicationSpecial;
import jade.lang.acl.ACLMessage;

public class Call_AGV_Transfer extends CommunicationSpecial {
    private Warehouse agent;

    public Call_AGV_Transfer(Warehouse agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
        condition = this.agent.call_agv_transfer;
    }

    @Override
    public void VariableReset() {
        this.agent.call_agv_transfer = false;
    }

    @Override
    public void SomeOperations() {
        System.out.println("message:%n"+message);
        Call_Special(this.agent.provider,this.agent.protocol.CallAGVTransfer(this.agent.buffer_target_location,this.agent.material_type,this.agent.material_process_index), ACLMessage.REQUEST);
    }
}

