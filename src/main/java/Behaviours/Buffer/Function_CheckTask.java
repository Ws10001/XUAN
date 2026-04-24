package Behaviours.Buffer;

import Agents.Buffer;
import Behaviours.Base.Function;

import java.util.Arrays;

public class Function_CheckTask extends Function{
    Buffer agent;
    public Function_CheckTask(Buffer agent) {
        super(agent);
        this.agent = agent;
    }


    @Override
    protected void onTick() {
            int task_index = 0;
            for(;task_index<this.agent.location_status.length;task_index++){
                if (this.agent.location_status[task_index] == 2){                //需要加工
                    System.out.printf("检测到台位%d上存在加工任务！%n", task_index);
                    System.err.println("location_status:"+Arrays.toString(this.agent.location_status));
                    if (task_index<=3){                                          //车床的4个工位台
                        if (!this.agent.function_che_process){                   // 如果这个行为没被触发，防止正在执行，下面的代码导致数据覆盖
                            this.agent.task_che_location = task_index;           // 任务台位
                            this.agent.location_status[task_index] ++;           //台位状态改变
                            this.agent.function_che_process = true;              //开始车床加工过程
                        }else { System.err.println("Buffer请求车床加工进程正在执行，请等待完成再请求！");}
                    } else if (task_index<=7) {
                        if (!this.agent.function_xi_process){                   // 如果这个行为没被触发，防止正在执行，下面的代码导致数据覆盖
                            this.agent.task_xi_location = task_index;           // 任务台位
                            this.agent.location_status[task_index] ++;           //台位状态改变
                            this.agent.function_xi_process = true;              //开始车床加工过程
                        }else { System.err.println("Buffer请求铣床加工进程正在执行，请等待完成再请求！");}
                    }else{
                        if (!this.agent.function_diao_process){                   // 如果这个行为没被触发，防止正在执行，下面的代码导致数据覆盖
                            this.agent.task_diao_location = task_index;           // 任务台位
                            this.agent.location_status[task_index] ++;           //台位状态改变
                            this.agent.function_diao_process = true;              //开始车床加工过程
                        }else { System.err.println("Buffer请求雕刻机加工进程正在执行，请等待完成再请求！");}
                    }

                } else if (this.agent.location_status[task_index] == 4) {        //待出库
                    System.out.printf("检测到台位%d上存在出库任务！%n", task_index);
                    System.err.println("location_status:"+Arrays.toString(this.agent.location_status));
                    if (!this.agent.function_export){                        // 如果这个行为没被触发，防止正在执行，下面的代码导致数据覆盖
                        this.agent.task_export_location = task_index;           // 任务台位
                        this.agent.function_export = true;                      //开始出库过程
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
    }
}
