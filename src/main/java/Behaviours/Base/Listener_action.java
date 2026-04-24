package Behaviours.Base;

import Agents.BaseAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.json.JSONObject;

import java.lang.reflect.Field;

public abstract class Listener_action extends CyclicBehaviour {
    private BaseAgent agent;
    private MessageTemplate notDFTemplate;
    private MessageTemplate requestTemplate;
    private MessageTemplate listenTemplate;
    protected String message;
    private boolean have_message;
    protected boolean complete;
    private int next_stage = 0;
    protected JSONObject mess_act_json;
    private ACLMessage msg;
    private String replyWithId;
    private String requester;

    public Listener_action(BaseAgent agent) {
        this.agent = agent;
        notDFTemplate = MessageTemplate.not(MessageTemplate.MatchSender(agent.getAID("df")));
        requestTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        listenTemplate = MessageTemplate.and(notDFTemplate,requestTemplate);
    }
    @Override
    public void action() {
        if (isGetMessage()) {
            ActionSpace();
            SendMessage();
        } else {
            block();
        }
    }

    /**
     * 该方法中需要使用ActionByCondition()定义条件行为。
     */
    public abstract void ActionSpace();
    public void ActionByCondition(boolean condition, Runnable... actions) {
        if (condition) {
            for (Runnable action : actions) {
                action.run();
            }
        }
    }

    private boolean isGetMessage(){
        have_message = false;
        msg = this.agent.receive(listenTemplate);
        if (msg != null) {
            System.out.println("%s.listen_act收到消息：".formatted(this.agent.name)+message);
            message = msg.getContent();
            mess_act_json = new JSONObject(message);
            have_message = true;
            requester = msg.getSender().getLocalName();
            replyWithId = msg.getReplyWith();
            System.out.println("Agent " + this.agent.getLocalName() + " 接收到来自 " + requester + " 的指令：" + mess_act_json.getJSONObject("body").getString("command") + " ,回复识别码: " + replyWithId);
        }
        return have_message;
    }
    public void SendMessage(){
        if (complete)
        {
            ACLMessage reply = msg.createReply(ACLMessage.CONFIRM);
            reply.setContent("指令："+ mess_act_json.getJSONObject("body").getString("command") +"已完成！");
            System.out.println("Agent "+this.agent.getLocalName()+" 向 "+requester+" 回复: "+reply.getContent());
            this.agent.send(reply);
            complete = false;
        }
    }

    /**
     * 该方法可以实现将一个标志位设置为true以启动某行为，并等待该标志位重置为false。
     * @param fieldName 要设置为 true 的标志位字段的名称。
     */
    public void ActionUnitWait(String fieldName){
        SetObjectVariable(this.agent,fieldName,true);
        while ((boolean)GetObjectVariable(this.agent,fieldName)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



    /**
     * 该方法可以实现将一个标志位设置为true以启动某行为。
     * @param fieldName 要设置为 true 的标志位字段的名称。
     */
    public void ActionUnit(String fieldName){
        SetObjectVariable(this.agent,fieldName,true);
    }

    private Object GetObjectVariable(Object obj, String fieldName) {
        try {
            // 获取对象的类类型
            Class<?> objClass = obj.getClass();
            // 遍历字段名列表，打印对应字段的值
            Field field = objClass.getField(fieldName);  // 根据字段名获取字段
            field.setAccessible(true); // 确保可以访问 private 字段
            // 获取字段值并打印
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";

    }
    private void SetObjectVariable(Object obj, String fieldName,Object value) {
        try {
            // 获取对象的类类型
            Class<?> objClass = obj.getClass();
            // 遍历字段名列表，打印对应字段的值
            Field field = objClass.getField(fieldName);  // 根据字段名获取字段
            field.setAccessible(true); // 确保可以访问 private 字段
            field.set(obj,value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
