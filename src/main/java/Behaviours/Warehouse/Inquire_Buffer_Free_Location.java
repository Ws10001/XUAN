package Behaviours.Warehouse;


import Agents.BaseAgent;
import Agents.Warehouse;
import Agents.Buffer;
import Behaviours.Base.Inquire;
import jade.lang.acl.ACLMessage;
import org.json.JSONObject;


public class Inquire_Buffer_Free_Location extends Inquire {
    private Warehouse agent;

    public Inquire_Buffer_Free_Location(Warehouse agent){
        super(agent);
        this.agent = agent;
    }


    @Override
    public void Bind() {
        condition = this.agent.inquire_buffer_free_location;
        service_name =Buffer.Service_IMPORT;
    }

    @Override
    public void VariableReset() {
        this.agent.inquire_buffer_free_location = false;
    }

    @Override
    public void SomeOperations() {
        this.agent.provider = provider;
        message = this.agent.protocol.getBufferFreeLocation(BaseAgent.process.getJSONArray(this.agent.material_type).getString(0));
        JSONObject result = new JSONObject(Call_Special(provider,message, ACLMessage.INFORM));
        this.agent.buffer_target_location = result.getJSONObject("body").getInt("result");
    }
}
