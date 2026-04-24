package Behaviours.Test;
import jade.core.behaviours.CyclicBehaviour;
import Agents.AGV;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ManuallyModifyVariable extends CyclicBehaviour {
    private final AGV agent; // 当前智能体

    public ManuallyModifyVariable(AGV agent) {
        super(agent);
        this.agent = agent;
    }

    @Override
    public void action() {
        try {
            // 创建一个服务器套接字，监听指定端口（例如 12345）
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Waiting for a connection...");

            // 等待连接并接受客户端请求
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection established!");

            // 读取输入流
            InputStream inputStream = clientSocket.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024]; // 读取缓冲区
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            // 获取接收到的消息
            String message = byteArrayOutputStream.toString("UTF-8");
            System.out.println("Received message: " + message);

            // 修改智能体的变量 a
            if (agent instanceof AGV) {
                agent.control_import = true; // 将变量 a 设置为 true
            }

            System.out.println("变量已修改，当前为:"+agent.control_import);

            // 关闭套接字
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            System.err.println("Error in socket communication: " + e.getMessage());
        }
    }
}
