package Behaviours.Base;

import Agents.BaseAgent;
import CommunProtocols.CommonProtocol;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.json.JSONObject;

public abstract class CommunicationSpecial extends TickerBehaviour{
    protected CommonProtocol protocol = new CommonProtocol();

    public BaseAgent agent;
    public boolean condition;
    public String service_name;
    public String message;
    public String response;


    public CommunicationSpecial(BaseAgent agent, int delay) {
        super(agent, delay);
        this.agent = agent;
    }
    public CommunicationSpecial(BaseAgent agent) {
        super(agent, 1000);
        this.agent = agent;
    }

    @Override
    public void onTick(){
        Bind();                     // 绑定数据
        if (condition){
            SomeOperations();             // 对结果进行一些操作
            VariableReset();
        }
//        else
//            System.out.println("等待触发标志位！");
    }

    /**
     * 你需要在该方法中，你需要绑定condition变量的值。<br>
     */
    public abstract void Bind();

    /**
     * 在这个方法，你需要构建已有的接收者this.agent.provider、发送的信息message、以及返回值response进行处理。通过函数Call_special(provider,message,mess_type)。这个过程将发生在标志位重置之前。
     */
    public abstract void SomeOperations();

    /**
     * 在该方法中，你需要对重置操作进行定义。
     */
    public abstract void VariableReset();



    protected String Call_Special(AID agent_id,String mess,int mess_type){
        String conversationId = "conv_id_" + System.currentTimeMillis();
        String replyWithId = "reply_with_" + System.currentTimeMillis();
        ACLMessage msg = new ACLMessage(mess_type);
        msg.addReceiver(agent_id);
        msg.setContent(mess);
        JSONObject mess_json = new JSONObject(mess);
        msg.setConversationId(conversationId);
        msg.setReplyWith(replyWithId);
        myAgent.send(msg);
        System.out.println("Agent "+this.agent.getLocalName()+" 已向 "+agent_id.getLocalName()+" 发送请求: "+mess_json.getJSONObject("body").getString("command")+" ,消息识别码: "+conversationId+" ,回复识别码: "+replyWithId);

        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchConversationId(conversationId),MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
        ACLMessage result_msg = this.agent.blockingReceive(mt);
        response = result_msg.getContent();
        if (result_msg != null) {
            System.out.println("Agent "+this.agent.getLocalName()+" 接收到来自 "+agent_id.getLocalName()+" 的回复： no_show,回复识别码: "+result_msg.getReplyWith());
        }
        return response;
    }
}
