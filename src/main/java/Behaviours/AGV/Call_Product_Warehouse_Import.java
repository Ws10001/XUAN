package Behaviours.AGV;

import Agents.AGV;
import Behaviours.Base.Communication;
import jade.lang.acl.ACLMessage;

public class Call_Product_Warehouse_Import extends Communication {
    private AGV agent;

    public Call_Product_Warehouse_Import(AGV agent){
        super(agent);
        this.agent = agent;
    }
    @Override
    public void Bind() {
       condition     = this.agent.call_warehouse_product_import;
       service_name  = "warehouse_product";
       message = this.agent.protocol.CallProductWarehouseImport(this.agent.material_type);
    }
    @Override
    public void SomeOperations() {
        System.out.printf("AGV发送消息通知成品库进行入库:%n");
        System.out.printf(message);
        Call_Manual(provider,message, ACLMessage.REQUEST);
    }
    @Override
    public void VariableReset() {
        this.agent.call_warehouse_product_import = false;
    }
}
