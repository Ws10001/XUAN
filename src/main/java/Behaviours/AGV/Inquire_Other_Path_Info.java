package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Communication;
import Behaviours.Base.Inquire;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

public class Inquire_Other_Path_Info extends Inquire {
    private AGV agent;

    public Inquire_Other_Path_Info(AGV agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
       condition     = this.agent.inquire_other_path_info;
       if (Objects.equals(this.agent.getLocalName(), "AGV2"))
           service_name  = "AGV1_Info";
       else service_name = "AGV2_Info";
       message = this.agent.protocol.InquirePathInfo();
    }

    @Override
    public void SomeOperations() {
        System.out.printf("Agent %s 向Agent %s 查询路径信息，消息如下:%n",this.agent.getLocalName(),provider.getLocalName());
        System.out.printf(message);
        Call_Special(provider,message, ACLMessage.INFORM);
        JSONObject  response_json = new JSONObject(response);
        System.out.println("查询到的response内容如下："+response);
        this.agent.data_other_path = response_json.getJSONObject("body").getString("path");
        this.agent.data_other_free_status = response_json.getJSONObject("body").getBoolean("free_status");
        System.out.println("更新数据如下：");
        System.out.println("data_other_path："+this.agent.data_other_path);
        System.out.println("data_other_free_status："+this.agent.data_other_free_status);
    }

    @Override
    public void VariableReset() {
        this.agent.inquire_other_path_info = false;
    }
}
