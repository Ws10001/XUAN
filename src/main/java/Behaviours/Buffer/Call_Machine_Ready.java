package Behaviours.Buffer;

import Agents.Machine;
import Agents.Buffer;
import Behaviours.Base.Communication;
import jade.lang.acl.ACLMessage;


public class Call_Machine_Ready extends Communication {
    private Buffer agent;

    public Call_Machine_Ready(Buffer agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
        condition = this.agent.call_machine_ready;
        if (condition) {
            if (this.agent.call_machine_type.equals(Buffer.TASK_TYPE.CHE)) service_name = Machine.Service_PROCESS_LATHE;
            else if (this.agent.call_machine_type.equals(Buffer.TASK_TYPE.XI))
                service_name = Machine.Service_PROCESS_MILLING;
            else service_name = Machine.Service_PROCESS_ENGRAVE;
        }
        selector_type = SelectorType.RANDOM;
    }

    @Override
    public void SomeOperations() {
        if (this.agent.call_machine_type.equals(Buffer.TASK_TYPE.CHE)) this.agent.provider_che = provider;   //保留车床对象供加工用
        else if (this.agent.call_machine_type.equals(Buffer.TASK_TYPE.XI)) this.agent.provider_xi = provider;   //保留铣床对象供加工用
        else this.agent.provider_diao = provider;   //保留雕刻机对象供加工用
    }
    @Override
    public void VariableReset() {
        this.agent.call_machine_ready = false;
    }


}

