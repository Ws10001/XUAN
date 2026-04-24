package utilities.Common;

import Agents.BaseAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class DFServiceSearch extends OneShotBehaviour{
    BaseAgent agent;
    String[] service_list;
    public DFServiceSearch(BaseAgent agent, String[] service_list){
        this.agent = agent;
        this.service_list = service_list;
    }

    @Override
    public void action() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(this.agent.getAID());
        for (String service_name:service_list)
        {
            ServiceDescription service = new ServiceDescription();
            service.setType("compute");
            service.setName(this.agent.getLocalName());
            dfd.addServices(service);
        }
        try {
            DFService.register(this.agent, dfd);
            System.out.println("服务添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
