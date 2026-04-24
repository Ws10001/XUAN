package Behaviours.Base;

import Agents.BaseAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import CommunProtocols.CommonProtocol;
import org.json.JSONObject;

public abstract class Listener_Information extends CyclicBehaviour {
    public CommonProtocol protocol = new CommonProtocol();
    private BaseAgent agent;
    private MessageTemplate notDFTemplate;
    private MessageTemplate requestTemplate;
    private MessageTemplate listenTemplate;
    protected String message;
    protected JSONObject mess_info_json;
    private boolean have_message;
    private boolean complete;
    private int next_stage = 0;
    private ACLMessage msg;
    private String replyWithId;
    private String requester;

    public Listener_Information(BaseAgent agent) {
        this.agent = agent;
        notDFTemplate = MessageTemplate.not(MessageTemplate.MatchSender(agent.getAID("df")));
        requestTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        listenTemplate = MessageTemplate.and(notDFTemplate,requestTemplate);
    }
    @Override
    public void action() {
        if (isGetMessage()) {
            ActionSpace();
        } else {
            block();
        }
    }

    /**
     * 在这个行为空间中，你需要根据message进行相应的信息的反馈，使用SendMessage(info)进行回信。
     */
    public abstract void ActionSpace();
    private boolean isGetMessage(){
        have_message = false;
        msg = this.agent.receive(listenTemplate);
        if (msg != null) {
            message = msg.getContent();
            System.out.println("%s.listen_info收到消息：".formatted(this.agent.name)+message);
            mess_info_json = new JSONObject(message);
            have_message = true;
            requester = msg.getSender().getLocalName();
            replyWithId = msg.getReplyWith();
            System.out.println("Agent " + this.agent.getLocalName() + " 接收到来自 " + requester + " 的指令：" + mess_info_json.getJSONObject("body").getString("command") + " ,回复识别码: " + replyWithId);
        }
        return have_message;
    }
    public void SendMessage(String info){

        ACLMessage reply = msg.createReply(ACLMessage.CONFIRM);
        reply.setReplyWith(replyWithId);
        reply.setContent(info);
        System.out.println("Agent "+this.agent.getLocalName()+" 向 "+requester+" 回复: "+reply.getContent());
        this.agent.send(reply);

    }
}
