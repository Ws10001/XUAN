package utilities.Common;
import jade.core.behaviours.OneShotBehaviour;
import Agents.BaseAgent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.List;

public class DFServiceRegister extends OneShotBehaviour{
    BaseAgent agent;
    List<String> service_list;
    public DFServiceRegister(BaseAgent agent, List<String> service_list){
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
            service.setType(service_name);
            service.setName(this.agent.getLocalName());
            dfd.addServices(service);
        }
        try {
            DFService.register(this.agent, dfd);
            String servicesString = String.join(", ", service_list);
            System.out.println("Agent "+this.agent.getLocalName()+" 注册 "+servicesString+" 服务成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
