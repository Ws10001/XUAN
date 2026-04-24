package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Service;

public class Service_ObstacleAvoid extends Service {
    private AGV agent;

    public Service_ObstacleAvoid(AGV agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
       condition     = this.agent.service_obstacleavoid;
       ip = "127.0.0.1";
       port = 22222;
       message = this.agent.protocol.ServiceObstacleAvoid(this.agent.status_position,this.agent.status_direction,this.agent.data_other_path);
       message = message.replace("\n","");
    }

    @Override
    public void SomeOperations() {

        String[] result = response.split("-");
        this.agent.data_path = result[0];
        this.agent.status_direction = Integer.parseInt(result[1]);
    }

    @Override
    public void VariableReset() {
        this.agent.service_obstacleavoid = false;
    }
}
