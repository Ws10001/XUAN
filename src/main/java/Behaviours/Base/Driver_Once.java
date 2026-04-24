package Behaviours.Base;
import Agents.BaseAgent;
import jade.core.behaviours.OneShotBehaviour;

public abstract class Driver_Once extends OneShotBehaviour {
    private BaseAgent agent;
    protected String order;
    protected String info;

    protected String response;

    public Driver_Once(BaseAgent agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void action(){
        Bind();           // 绑定数据
        Check();          // 检测触发驱动指令，并重置属性
    }

    /**
     * 你需要在该方法中，你需要绑定order以及info两个变量的值。<br>
     */
    public abstract void Bind();

    /**
     * 在这个方法中，你可以对接收到的信息response进行处理。
     */
    public abstract void SomeOperations();

    public void Check() {

            System.out.println("Agent "+this.agent.getLocalName()+":"+info);
            this.agent.client.Send(order);
            response = this.agent.client.Receive();
            SomeOperations();
    }
}
