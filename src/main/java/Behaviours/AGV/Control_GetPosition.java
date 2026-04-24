package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Driver;
import Behaviours.Base.Driver_Once;

public class Control_GetPosition extends Driver_Once {
    private AGV agent;
    public Control_GetPosition(AGV agent){
        super(agent);
        this.agent = agent;
    }
    @Override
    public void Bind() {
        order    = "getposition";  // e.g. "move-1,2,3,4"
        info     = "初始化: 查询AGV位置！";
    }
    @Override
    public void SomeOperations() {
        this.agent.status_position = Integer.parseInt(response);
        this.agent.data_path = response;
        System.out.println("AGV位置初始化结束！");
    }
}
