package utilities.Common;

import Agents.BaseAgent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class PrintString extends WakerBehaviour{
    BaseAgent agent;
    public PrintString(BaseAgent agent,int delay){
        super(agent,delay);
        this.agent = agent;
    }

//    @Override
    public void handleElapsedTimeout() {
        System.out.println("Agent "+this.agent.getLocalName()+" 初始化完成！");
    }
}
