package Behaviours.Arm;

import Agents.Arm;
import Agents.Buffer;
import Behaviours.Base.Communication;
import jade.lang.acl.ACLMessage;

public class Call_Buffer_Process_complete extends Communication {
    private Arm agent;

    public Call_Buffer_Process_complete(Arm agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
        condition = this.agent.call_buffer_process_complete;
        service_name = Buffer.Service_IMPORT;
        selector_type = SelectorType.GREEDY;
    }

    @Override
    public void VariableReset() {
        this.agent.call_buffer_process_complete = false;
    }

    @Override
    public void SomeOperations() {
        Call_Manual(provider,this.agent.protocol.CallBufferProcessComplete(this.agent.origin_buffer_site), ACLMessage.REQUEST);
    }
}

