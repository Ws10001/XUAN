package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Service;

import java.util.Arrays;

public class Service_PathPlanning extends Service {
    private AGV agent;

    public Service_PathPlanning(AGV agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
       condition     = this.agent.service_pathplanning;
       ip = "127.0.0.1";
       port = 12345;
       message = String.format("%d,%d,%d",this.agent.status_position,this.agent.status_direction,this.agent.status_target);
    }

    @Override
    public void SomeOperations() {

        String[] result = response.split("-");
        this.agent.data_path = result[0];
        this.agent.data_path_vector = Arrays.stream(result[1].split(",")) // 按逗号分割
                .mapToInt(Integer::parseInt) // 转换为int
                .toArray(); // 转为int数组
        this.agent.status_direction = this.agent.data_path_vector[this.agent.data_path_vector.length-1];
    }

    @Override
    public void VariableReset() {
        this.agent.service_pathplanning = false;
    }
}
