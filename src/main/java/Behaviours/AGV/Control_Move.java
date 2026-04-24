package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Driver;

import java.util.Arrays;

public class Control_Move extends Driver {
    private AGV agent;
    public Control_Move(AGV agent){
        super(agent);
        this.agent = agent;
    }
    @Override
    public void Bind() {
        condition = this.agent.control_move;
        order    = "move-"+this.agent.data_path;  // e.g. "move-1,2,3,4"
        info     = "驱动AGV进行移动！";
    }
    @Override
    public void VariableReset() {
        int[] path = Arrays.stream(this.agent.data_path.split(",")) // 按逗号分割
                .map(String::trim) // 去除每个部分的空格
                .mapToInt(Integer::parseInt) // 转为整数
                .toArray();
        this.agent.status_position = path[path.length-1];
        this.agent.data_path = String.valueOf(this.agent.status_position);
        this.agent.control_move = false;
        System.out.println("AGV移动结束，重置标志位！");
    }
}
