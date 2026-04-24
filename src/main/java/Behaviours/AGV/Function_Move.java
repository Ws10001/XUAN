package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Function;

import java.util.Arrays;

public class Function_Move extends Function {
    private AGV agent;

    public Function_Move(AGV agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void onTick() {
        if (this.agent.function_move) {
            System.out.println("AGV执行移动功能！");
            while (this.agent.status_position != this.agent.status_target){
                ActionUnitWait("service_pathplanning");    // 生成理论路径
                System.out.println("开始查询其他agv路径信息状态:");
                ActionUnitWait("inquire_other_path_info");    // 查询另一台AGV的状态
                System.out.println("其他agv路径信息状态查询结束！");
                int[] this_agent_path = Arrays.stream(this.agent.data_path.split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                int[]other_agent_path = Arrays.stream(this.agent.data_other_path.split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                boolean need_avoid = false;
                int index_1 = 0;
                for (;index_1<this_agent_path.length;index_1++){
                    int finalIndex_ = index_1;       //这里问问gpt为啥不能直接用
                    if (Arrays.stream(other_agent_path).anyMatch(num -> num == this_agent_path[finalIndex_])) //如果存在碰撞
                    {
                        need_avoid = true;
                        break;
                    }
                }
                int new_path_len = 0;
                if(need_avoid){
                    System.out.println("检测到路径碰撞！");
                    if (this.agent.data_other_free_status){    //如果另一AGV空闲
                        System.out.println("检测到其他AGV处于空闲，将请求避障！");
                        ActionUnitWait("call_other_avoid");  //请求避障
                        ActionUnitWait("control_move");
                    }else{
                        System.out.println("检测到其他AGV处于繁忙，将进行任务切割！");
                        System.out.printf("原路径: %s ,其在第%d位存在冲突，考虑非法节点后的切割路径为:%n", this.agent.data_path,index_1);
                        int[] invalidsite = {1,3,5,8,10,11,13,14,16,17,19,20,22,23,25,26};   //切割后的终点不能在交叉口以及每个左边台位(距离太小)
                        int final_index = index_1-1;
                        if (Arrays.stream(invalidsite).anyMatch(num -> num == this_agent_path[final_index])) index_1 --;
                        int[] new_path = Arrays.copyOf(this_agent_path,index_1);
                        new_path_len = new_path.length;
                        this.agent.data_path = Arrays.toString(new_path) // 转为字符串 "[1, 2, 3, 4]"
                                .replace("[", "") // 去掉左方括号
                                .replace("]", "") // 去掉右方括号
                                .replace(" ", ""); // 去掉多余空格
                        System.err.println("切割的新路径:"+this.agent.data_path); // 输出："1,2,3,4"
                        System.err.printf("原来的路径方向列表:%s", Arrays.toString(this.agent.data_path_vector));
                        System.err.printf("方向数组索引:%d",index_1-1);

                        this.agent.status_direction = this.agent.data_path_vector[index_1-1];
                        System.out.println("new path:"+this.agent.data_path); // 输出："1,2,3,4"
                        System.out.println("new direction:"+this.agent.status_direction); // 输出："1,2,3,4"
                        if (new_path_len>1)
                        {
                            System.out.println("路径非空，AGV开始移动");
                            ActionUnitWait("control_move");
                        }
                        else{
                            System.out.println("路径为空，继续等待！");
                        }

                    }
                }else {
                    System.out.println("未检测到碰撞，即将驱动AGV移动！");
                    ActionUnitWait("control_move");
                }


                try {                              //防止多轮循环时速度太快
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("路径规划完成，service_pathplanning已经重置！");

            }
            this.agent.function_move = false;
        }
    }
}