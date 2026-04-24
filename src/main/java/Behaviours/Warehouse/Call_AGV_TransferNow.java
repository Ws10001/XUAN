package Behaviours.Warehouse;

import Agents.Warehouse;
import Behaviours.Base.Communication;
import jade.lang.acl.ACLMessage;

public class Call_AGV_TransferNow extends Communication {

    private Warehouse agent;

    public Call_AGV_TransferNow(Warehouse agent) {
        super(agent);
        this.agent = agent;
    }

@Override
    public void Bind() {
        condition = this.agent.have_task;
        service_name = "transfer";
        selector_type = SelectorType.RANDOM;
    }
@Override
    public void SomeOperations() {
         this.message = """
    {
      "body": {
        "command": "transfer"
      }
    }
    """;
        System.out.println("message:%n"+message);
        Call_Manual(provider,message, ACLMessage.REQUEST);
        this.agent.provider = provider;   //保留AGV对象供送货用
    }
@Override
    public void VariableReset() {
        this.agent.have_task = false;
    }
}

