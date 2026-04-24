package Behaviours.Base;
import Agents.BaseAgent;
import jade.core.behaviours.TickerBehaviour;
import utilities.Common.SocketClient;

import java.io.IOException;

public abstract class Service extends TickerBehaviour {
    private BaseAgent agent;
    protected boolean condition;
    protected String ip;
    protected int port;
    protected String message;
    protected String response;
    public Service(BaseAgent agent,int delay ){
        super(agent,delay);
        this.agent = agent;
    }
    public Service(BaseAgent agent){
        super(agent,1000);
        this.agent = agent;
    }

    @Override
    public void onTick(){
        Bind();           // 绑定数据
        Link();          // 检测触发驱动指令，并重置属性
    }

    /**
     * 你需要在该方法中，你需要绑定condition、ip、port以及message三个变量的值。<br>
     */
    public abstract void Bind();

    /**
     * 在该方法中，你需要对重置操作进行定义。
     */
    public abstract void VariableReset();
    /**
     * 在这个方法中，你可以对接收到的信息response进行处理，这个过程将发生在标志位重置之前。
     */
    public abstract void SomeOperations();
    public void Link() {
        if (condition){
            try {
                SocketClient socket = new SocketClient(ip,port);
                System.out.println("message:"+message);
                socket.Send(message);
                response = socket.Receive();
                socket.Close();
                System.out.println("result:"+ response);
                SomeOperations();
                VariableReset();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
