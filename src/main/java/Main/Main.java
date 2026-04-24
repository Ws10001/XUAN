package Main;
import Agents.*;
import utilities.Common.AgentInitiator;
import utilities.Common.Container;
import jade.wrapper.AgentContainer;
import Data.Config;


public class Main {

    public static void main(String[] args) {
        Container container = new Container();
        AgentContainer ac = container.getContainer();
        Config config = new Config();
        AgentInitiator buffer_control = new AgentInitiator(ac, Buffer.class, config.Buffer());
        AgentInitiator lathe1_control = new AgentInitiator(ac, Machine.class, config.Lathe1());
        AgentInitiator lathe2_control = new AgentInitiator(ac, Machine.class, config.Lathe2());
        AgentInitiator milling1_control = new AgentInitiator(ac, Machine.class, config.Milling1());
        AgentInitiator milling2_control = new AgentInitiator(ac, Machine.class, config.Milling2());
        AgentInitiator arm1_control = new AgentInitiator(ac, Arm.class, config.Arm_che());
        AgentInitiator arm2_control = new AgentInitiator(ac, Arm.class, config.Arm_xi());
        AgentInitiator agv1_control = new AgentInitiator(ac, AGV.class, config.AGV1(1));
        AgentInitiator agv2_control = new AgentInitiator(ac, AGV.class, config.AGV2(1));
        AgentInitiator warehouse_mat_control = new AgentInitiator(ac, Warehouse.class, config.Warehouse_Material());
        AgentInitiator warehouse_pro_control = new AgentInitiator(ac, Warehouse.class, config.Warehouse_Product());

    }

}
 