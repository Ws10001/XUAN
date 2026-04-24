package utilities.Common;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import org.json.JSONObject;


public class AgentInitiator {
    public AgentInitiator(AgentContainer container,Class<?> agentClass,String agent_name,String ip,int port) {
        // 定义要传递的初始化参数
        Object[] agentArgs = new Object[3];
        agentArgs[0] = agent_name;  // 名称
        agentArgs[1] = ip;  // 地址
        agentArgs[2] = port;  // IP
        try {
            // 启动 MyAgent 代理
            AgentController agentController = container.createNewAgent(agent_name, agentClass.getName(), agentArgs);  // 实例名，Agent对象路径，参数
            agentController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
    public AgentInitiator(AgentContainer container,Class<?> agentClass,String agent_name,String ip,int port,int direction,int open_port) {
        // 定义要传递的初始化参数
        Object[] agentArgs = new Object[5];
        agentArgs[0] = agent_name;  // 名称
        agentArgs[1] = ip;  // 地址
        agentArgs[2] = port;  // IP
        agentArgs[3] = direction;  // AGV的方向
        agentArgs[4] = open_port;  // AGV的手动指令监听端口
        try {
            // 启动 MyAgent 代理
            AgentController agentController = container.createNewAgent(agent_name, agentClass.getName(), agentArgs);  // 实例名，Agent对象路径，参数
            agentController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
    public AgentInitiator(AgentContainer container,Class<?> agentClass,String agent_name,String ip,int port,int open_port) {
        // 定义要传递的初始化参数
        Object[] agentArgs = new Object[4];
        agentArgs[0] = agent_name;  // 名称
        agentArgs[1] = ip;  // 地址
        agentArgs[2] = port;  // IP
        agentArgs[3] = open_port;  // Agent的手动指令监听端口
        try {
            // 启动 MyAgent 代理
            AgentController agentController = container.createNewAgent(agent_name, agentClass.getName(), agentArgs);  // 实例名，Agent对象路径，参数
            agentController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public AgentInitiator(AgentContainer container, Class<?> agentClass, JSONObject config) {
        // 定义要传递的初始化参数
        Object[] agentArgs = new Object[1];
        agentArgs[0] = config;
        try {
            // 启动 MyAgent 代理
            AgentController agentController = container.createNewAgent(config.getString("agent_name"), agentClass.getName(), agentArgs);  // 实例名，Agent对象路径，参数
            agentController.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
