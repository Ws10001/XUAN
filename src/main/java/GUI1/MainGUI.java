package GUI1;

import jade.wrapper.AgentContainer;
import utilities.Common.Container;
import Agents.*;
import utilities.Common.AgentInitiator;

public class MainGUI {
    public static void main(String[] args) {
        Container container = new Container();
        AgentContainer ac = container.getContainer();
        ConfigData config = new ConfigData();
        AgentInitiator warehouse1_warehouse1_control = new AgentInitiator(ac, Warehouse1.class, config.Warehouse1());
        AgentInitiator agv1_agv1_control = new AgentInitiator(ac, AGV1.class, config.AGV1());

        
    }
}
