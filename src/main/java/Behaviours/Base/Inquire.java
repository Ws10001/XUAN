package Behaviours.Base;

import Agents.BaseAgent;
import CommunProtocols.CommonProtocol;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Inquire extends TickerBehaviour{     //注意：查询行为，一定是仅有一个符合条件的Agent
    protected CommonProtocol protocol = new CommonProtocol();

    public BaseAgent agent;
    public boolean condition;
    public String service_name;
    public String message;
    public String response;

    protected DFAgentDescription[] providers_list_find;   // 指定服务提供者列表(查到的)
    protected ArrayList<DFAgentDescription> providers_list_final = new ArrayList<>();// 指定服务提供者列表(可用的)

    protected AID provider;   //目标Agent的AID，沟通凭证
    public Inquire(BaseAgent agent, int delay) {
        super(agent, delay);
        this.agent = agent;
    }
    public Inquire(BaseAgent agent) {
        super(agent, 1000);
        this.agent = agent;
    }

    @Override
    public void onTick(){
        Bind();                     // 绑定数据
        if (condition){
            System.out.println("监测到标志位触发！");
            SearchDFServiceProviders();   // 搜索所有DF提供者
            SomeOperations();             // 对结果进行一些操作
            VariableReset();              // 重置标志位

        }
//        else
//            System.out.println("等待触发标志位！");
    }

    /**
     * 你需要在该方法中，你需要绑定condition、service_name、message以及selector_type四个变量的值。<br>
     */
    public abstract void Bind();

    /**
     * 在这个方法，你需要构建接收者provider、发送的信息message、以及返回值response进行处理。通过函数Call_special(provider,message,mess_type)。这个过程将发生在标志位重置之前。
     */
    public abstract void SomeOperations();

    /**
     * 在该方法中，你需要对重置操作进行定义。
     */
    public abstract void VariableReset();




    /**
     * 搜索指定的DF服务,并存储服务提供者于列表
     */
    protected void SearchDFServiceProviders(){
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(service_name);
        template.addServices(sd);
        System.out.println("Agent "+this.agent.getLocalName()+" 开始搜索"+sd.getType()+" 信息查询服务：");
        try {
            while (true) {
                providers_list_find = DFService.search(myAgent, template);
                System.out.printf("Agent " + this.agent.getLocalName() + "搜索到%d个信息查询服务提供者！%n", providers_list_find.length);
                if (providers_list_find.length > 0) {
                    provider = providers_list_find[0].getName();
                    System.out.println("选择查询服务提供商："+provider);
                    break;
                }
                Thread.sleep(1000);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

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
