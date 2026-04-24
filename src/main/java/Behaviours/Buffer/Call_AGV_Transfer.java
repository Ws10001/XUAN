package Behaviours.Buffer;

import Agents.Buffer;
import Agents.Warehouse;
import Behaviours.Base.CommunicationSpecial;
import jade.lang.acl.ACLMessage;

public class Call_AGV_Transfer extends CommunicationSpecial {
    private Buffer agent;

    public Call_AGV_Transfer(Buffer agent){
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
        System.err.printf("Buffer驱动AGV去位置%d处！%n", this.agent.export_target_location);
        Call_Special(this.agent.provider_agv,this.agent.protocol.CallAGVTransfer(this.agent.export_target_location,this.agent.material_type_matrix[this.agent.task_export_location], this.agent.material_process_index_matrix[this.agent.task_export_location]), ACLMessage.REQUEST);
    }
}

