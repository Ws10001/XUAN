package Behaviours.Arm;

import Agents.Arm;
import Behaviours.Base.CommunicationSpecial;
import jade.lang.acl.ACLMessage;

public class Call_Machine_Process extends CommunicationSpecial {
    private Arm agent;

    public Call_Machine_Process(Arm agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
        condition = this.agent.call_machine_process;
    }

    @Override
    public void VariableReset() {
        this.agent.call_machine_process = false;
    }

    @Override
    public void SomeOperations() {
        Call_Special(this.agent.provider,this.agent.protocol.CallMachineProcess(this.agent.material_type,this.agent.origin_buffer_site), ACLMessage.REQUEST);
    }
}

