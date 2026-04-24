package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Communication;
import jade.lang.acl.ACLMessage;

import java.util.Objects;

public class Call_Other_Avoid extends Communication {
    private AGV agent;
    public Call_Other_Avoid(AGV agent){
        super(agent);
        this.agent = agent;
    }
    @Override
    public void Bind() {
        condition     = this.agent.call_other_avoid;
        if (Objects.equals(this.agent.getLocalName(), "AGV2"))
            service_name  = "AGV1_Info";
        else service_name = "AGV2_Info";
        message = this.agent.protocol.CallOtherAvoid(this.agent.data_path);
    }
    @Override
    public void SomeOperations() {
        System.out.printf("AGV发送请求避障指令如下:%n");
        System.out.printf(message);
        Call_Manual(provider,message, ACLMessage.REQUEST);
    }
    @Override
    public void VariableReset() {
        this.agent.call_other_avoid = false;
    }
}
