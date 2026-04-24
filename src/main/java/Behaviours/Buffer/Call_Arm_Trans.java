package Behaviours.Buffer;

import Agents.Buffer;
import Behaviours.Base.Communication;
import jade.lang.acl.ACLMessage;

public class Call_Arm_Trans extends Communication {
    Buffer agent;


    public Call_Arm_Trans(Buffer agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
        condition = this.agent.call_arm_trans;
        if (condition) {
            String machine_name;
            int site;
            String item_type;
            String agent_AID;
            if (this.agent.call_machine_type.equals(Buffer.TASK_TYPE.CHE)) {

                service_name = "arm_buffer_lathe";
                machine_name = this.agent.provider_che.getLocalName();
                System.out.println("machine_name:"+machine_name);
                site = this.agent.task_che_location;
                System.out.println("site:"+site);
                item_type = this.agent.material_type_matrix[this.agent.task_che_location];
                agent_AID = this.agent.provider_che.getName();
            } else if (this.agent.call_machine_type.equals(Buffer.TASK_TYPE.XI)) {
                service_name = "arm_buffer_milling";
                machine_name = this.agent.provider_xi.getLocalName();
                site = this.agent.task_xi_location;
                item_type = this.agent.material_type_matrix[this.agent.task_xi_location];
                agent_AID = this.agent.provider_xi.getName();
            } else {
                service_name = "arm_buffer_engrave";
                machine_name = this.agent.provider_diao.getLocalName();
                site = this.agent.task_diao_location;
                item_type = this.agent.material_type_matrix[this.agent.task_diao_location];
                agent_AID = this.agent.provider_diao.getName();
            }
            message = this.agent.protocol.CallArmTransfer(machine_name, site, item_type,agent_AID);
            selector_type = SelectorType.GREEDY;
        }
    }

    @Override
    public void SomeOperations() {
        Call_Manual(provider,message, ACLMessage.REQUEST);
    }

    @Override
    public void VariableReset() {
        this.agent.call_arm_trans = false;
    }
}
