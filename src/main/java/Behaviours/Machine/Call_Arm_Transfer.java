package Behaviours.Machine;



import Agents.Machine;
import Behaviours.Base.Communication;
import jade.lang.acl.ACLMessage;

public class Call_Arm_Transfer extends Communication {
    private Machine agent;

    public Call_Arm_Transfer(Machine agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
        condition = this.agent.call_arm_transfer;
        if (this.agent.self_machine_type.equals("lathe")) service_name = "arm_lathe_buffer";
        else if (this.agent.self_machine_type.equals("milling")) service_name = "arm_milling_buffer";
        selector_type = SelectorType.GREEDY;
    }

    @Override
    public void SomeOperations() {
        System.out.println("message:%n"+message);
        Call_Manual(provider,this.agent.protocol.CallArmTransfer(this.agent.machine_index,this.agent.material_origin_site,this.agent.material_type), ACLMessage.REQUEST);
    }
    @Override
    public void VariableReset() {
        this.agent.call_arm_transfer = false;
    }


}

