package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Communication;
import jade.lang.acl.ACLMessage;

public class Call_Buffer_Import extends Communication {
    private AGV agent;
    public Call_Buffer_Import(AGV agent){
        super(agent);
        this.agent = agent;
    }
    @Override
    public void Bind() {
       condition     = this.agent.call_buffer_import;
       service_name  = "buffer_import";
       message = this.agent.protocol.CallBufferImport(this.agent.status_position,this.agent.material_type,this.agent.material_process_index);
    }
    @Override
    public void SomeOperations() {
        System.out.printf("AGV发送buffer驱动传送带，消息如下:%n");
        System.out.printf(message);
        Call_Manual(provider,message, ACLMessage.REQUEST);
    }
    @Override
    public void VariableReset() {
        this.agent.call_buffer_import = false;
    }
}
