package Behaviours.Warehouse;

import Agents.Warehouse;
import Behaviours.Base.Driver;

public class Control_Move extends Driver {
    Warehouse agent;
    public Control_Move (Warehouse agent){
        super(agent);
        this.agent = agent;
    }

    @Override
    public void Bind() {
        condition = this.agent.control_move;
        if (condition)
        {
            if (this.agent.move_type == 1){
                this.agent.move_to_id = "29";
                //this.agent.move_from_id = String.valueOf(this.agent.sqlClient.getIdWithCondition(this.agent.protocol.getMaterialIdWithCondition(1)));
                this.agent.move_from_id = "9";
            } else if (this.agent.move_type == 3) {
                this.agent.move_to_id = "29";
                this.agent.move_from_id = "8";
            }
            order = "move-"+this.agent.move_from_id+"-"+this.agent.move_to_id;
            info = "驱动仓库C#进行从库位%s出货".formatted(this.agent.move_from_id);

        }


    }

    @Override
    public void VariableReset() {
        this.agent.move_type = -1;
        this.agent.control_move = false;
    }

}
