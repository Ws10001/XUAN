package Behaviours.Warehouse;

import Agents.Warehouse;
import jade.core.behaviours.TickerBehaviour;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ManuallyOrder extends TickerBehaviour {
    private final Warehouse agent; // 当前智能体
    protected String message;   //接收到的字符串
    protected String[] message_list;  // 如果字符串需要分割，放在这里面

    public ManuallyOrder(Warehouse agent) {
        super(agent,1000);
        this.agent = agent;
    }

    @Override
    public void onTick() {
        try {
            // 创建一个服务器套接字，监听指定端口（例如 12345）
            ServerSocket serverSocket = new ServerSocket(this.agent.open_port);
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
            message = byteArrayOutputStream.toString("UTF-8");
            System.out.println("info:"+message);
            message_list = message.split("-");
            System.out.println("Received message: " + message);
            doSomething();
            // 关闭套接字
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            System.err.println("Error in socket communication: " + e.getMessage());
        }


    }

    protected void doSomething(){
        if (message_list[0].equals("move"))
        {
            this.agent.move_type = Integer.parseInt(message_list[1]);
            if (this.agent.move_type == 1)
            {
                this.agent.material_type = "falan";
                this.agent.material_process_index = 0;
            } else if (this.agent.move_type == 3) {
                this.agent.material_type = "zhou";
                this.agent.material_process_index = 0;
            }else{
                this.agent.material_type = "ban";
                this.agent.material_process_index = 0;
            }
            this.agent.function_export = true;
            System.out.println("设置标志位this.agent.function_export = true！");
        } else if (message_list[0].equals("agv")) {
            this.agent.call_agv_ready = true;
        } else
            System.out.printf("指令无法解析: %s%n", message_list[0]);
    }
}



