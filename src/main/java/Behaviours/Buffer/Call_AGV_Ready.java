package Behaviours.Buffer;


import Agents.AGV;
import Agents.Buffer;
import Behaviours.Base.Communication;
import jade.lang.acl.ACLMessage;

public class Call_AGV_Ready extends Communication {
    private Buffer agent;

    public Call_AGV_Ready(Buffer agent){
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
        System.err.printf("Buffer驱动AGV来台位%d处！%n", this.agent.location_code_Trans_for_agv[this.agent.task_export_location]);
        Call_Manual(provider,this.agent.protocol.CallAGVComeOn(this.agent.location_code_Trans_for_agv[this.agent.task_export_location]), ACLMessage.REQUEST);
        this.agent.provider_agv = provider;   //保留AGV对象供送货用
    }
    @Override
    public void VariableReset() {
        this.agent.call_agv_ready = false;
    }


}

