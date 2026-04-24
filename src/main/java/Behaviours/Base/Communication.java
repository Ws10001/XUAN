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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;

public abstract class Communication extends TickerBehaviour{
    protected CommonProtocol protocol = new CommonProtocol();
    public enum SelectorType {
        GREEDY,        // 贪婪选择器
        RANDOM,        // 随机选择器
        CUSTOM         // 自定义选择器
    }
    public BaseAgent agent;
    public boolean condition;
    public String service_name;
    public String message;
    public String response;
    public SelectorType selector_type = SelectorType.GREEDY;   //选择器默认采用贪婪选择法

    protected DFAgentDescription[] providers_list_find;   // 指定服务提供者列表(查到的)
    protected ArrayList<DFAgentDescription> providers_list_final = new ArrayList<>();// 指定服务提供者列表(可用的)

    protected AID provider;   //目标Agent的AID，沟通凭证
    public Communication(BaseAgent agent, int delay) {
        super(agent, delay);
        this.agent = agent;
    }
    public Communication(BaseAgent agent) {
        super(agent, 1000);
        this.agent = agent;
    }

    @Override
    public void onTick(){
        Bind();                     // 绑定数据
        if (condition) {
            System.out.println("监测到标志位触发！");
            SearchDFServiceProviders();   // 搜索所有DF提供者
            if (!providers_list_final.isEmpty()) {
                Selector();                   // 选择某一DF提供者
                SomeOperations();             // 对结果进行一些操作
                VariableReset();              // 重置标志位
            }
        }

  //      else
  //         System.out.println("等待触发标志位！");
    }

    /**
     * 你需要在该方法中，你需要绑定condition、service_name、message以及selector_type四个变量的值。<br>
     */
    public abstract void Bind();

    /**
     * 在这个方法，你需要构建接收者provider、发送的信息message、以及返回值response进行处理。通过函数Call_Manual(provider,message,mess_type)。这个过程将发生在标志位重置之前。
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
        providers_list_final.clear();
        System.out.println("Agent "+this.agent.getLocalName()+" 开始搜索 "+sd.getType()+" 服务：");
        try {
            while (true) {
                providers_list_find = DFService.search(myAgent, template);
                System.out.printf("Agent " + this.agent.getLocalName() + "初步搜索到%d个服务提供者！%n", providers_list_find.length);
                if (providers_list_find.length > 0) {
                    List<String> providers_name = new ArrayList<>();
                    for (int i = 0; i < providers_list_find.length; i++) {
                        String content = Call_Manual(providers_list_find[i].getName(),protocol.InquireFreeStatus(),ACLMessage.INFORM);
                        JSONObject content_json = new JSONObject(content);
                        Boolean result = content_json.getJSONObject("body").getBoolean("result");
                        System.out.printf("查询Agent %s 的free状态，结果是: %s%n", providers_list_find[i].getName(),result);
                        if (!result)
                            continue;
                        providers_list_final.add(providers_list_find[i]);

                        String s = String.valueOf(providers_list_find[i].getName());
                        Matcher matcher = Pattern.compile(":name\\s+([^@]+)@").matcher(s);
                        if (matcher.find()) {
                            providers_name.add(matcher.group(1));
                        }
                    }
                    System.out.println("Agent " + this.agent.getLocalName() + " 最终确定有效 " + sd.getType() + " 服务提供者" + Integer.toString(providers_list_final.size()) + "个：" + String.join(",", providers_name));
                    break;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void Selector(){
        switch (selector_type){
            case GREEDY -> DFProvidersSelectorGreedy();
            case RANDOM -> DFProvidersSelectorRandom();
            case CUSTOM -> DFProvidersSelectorCustom();
        }
    }
    protected void DFProvidersSelectorGreedy(){  //贪婪选择器
        provider = providers_list_final.get(0).getName();
        System.out.printf("贪婪算法选择了目标智能体:%s %n",provider);
    }
    protected void DFProvidersSelectorRandom(){  //随机选择器
        Random random = new Random();
        provider = providers_list_final.get(random.nextInt(providers_list_final.size())).getName();
    }

    protected void DFProvidersSelectorCustom(){  //用户自定义选择器
        provider = providers_list_final.get(0).getName();  //这里默认填入贪婪选择
    }


    protected void Call_Auto(){
        String conversationId = "conv_id_" + System.currentTimeMillis();
        String replyWithId = "reply_with_" + System.currentTimeMillis();
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(provider);
        msg.setContent(message);
        msg.setConversationId(conversationId);
        msg.setReplyWith(replyWithId);
        myAgent.send(msg);
        System.out.println("Agent "+this.agent.getLocalName()+" 已向 "+provider+" 发送请求: "+msg.getContent()+" ,消息识别码: "+conversationId+" ,回复识别码: "+replyWithId);

        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchConversationId(conversationId),MessageTemplate.MatchInReplyTo(replyWithId));
        ACLMessage result_msg = this.agent.blockingReceive(mt);
        response = result_msg.getContent();
        if (result_msg != null) {
            System.out.println("Agent "+this.agent.getLocalName()+" 接收到来自 "+provider+" 的回复："+ response +" ,回复识别码: "+result_msg.getReplyWith());
        }
    }

    protected String Call_Manual(AID agent_id, String mess, int mess_type){
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
