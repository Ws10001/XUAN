package Behaviours.Buffer;


import Agents.BaseAgent;
import Agents.Buffer;
import Agents.Warehouse;
import Behaviours.Base.Inquire;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;


public class Inquire_Self_Free_Location extends Inquire {
    private Buffer agent;

    public Inquire_Self_Free_Location(Buffer agent){
        super(agent);
        this.agent = agent;
    }


    @Override
    public void Bind() {
        condition = this.agent.inquire_self_free_location;
        service_name =Buffer.Service_IMPORT;
    }

    @Override
    public void VariableReset() {
        this.agent.inquire_self_free_location = false;
    }

    @Override
    public void SomeOperations() {
        message = this.agent.protocol.getSelfFreeLocation(this.agent.export_process);
        JSONObject result = new JSONObject(Call_Special(provider,message, ACLMessage.INFORM));
        this.agent.export_target_location = result.getJSONObject("body").getInt("result");
    }
}
