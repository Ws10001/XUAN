package Behaviours.Base;
import Agents.BaseAgent;
import jade.core.behaviours.TickerBehaviour;

public abstract class Driver extends TickerBehaviour {
    protected BaseAgent agent;
    protected boolean condition;
    protected String order;
    protected String info;
    public Driver(BaseAgent agent, int delay ){
        super(agent,delay);
        this.agent = agent;
    }
    public Driver(BaseAgent agent){
        super(agent,1000);
        this.agent = agent;
    }

    @Override
    public void onTick(){
        Bind();           // 绑定数据
        Check();          // 检测触发驱动指令，并重置属性
    }

    /**
     * 你需要在该方法中，你需要绑定condition、order以及info三个变量的值。<br>
     */
    public abstract void Bind();

    /**
     * 在该方法中，你需要对重置操作进行定义。
     */
    public abstract void VariableReset();

    /**
     * 该方法允许你在满足条件时，对一些标志位进行处理。
     */
    public void VarChangeWhenCondition(){};

    public void Check() {
        if (condition){
            VarChangeWhenCondition();
            System.out.println("Agent "+this.agent.getLocalName()+":"+info);
            this.agent.client.Send(order);
            String a = this.agent.client.Receive();
            System.out.println("Agent "+this.agent.getLocalName()+" 接收到C#层返回信息:"+a);
            VariableReset();
        }
    }
}
