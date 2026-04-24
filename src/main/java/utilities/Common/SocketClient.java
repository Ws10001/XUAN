package utilities.Common;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocketClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public SocketClient(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void Send(String message) {
        out.println(message);
    }

    public String Receive(){

        try {
            String result = in.readLine();
            System.out.println("读取驱动层返回状态:"+result);
            return result;
        }
        catch (IOException e) {
            System.err.println("读取驱动层返回状态失败！！！");
            System.out.println("请按任意键退出:");
            new Scanner(System.in).nextLine();
            System.exit(0);
            return "false";    //这行没用，仅作为语法条件的满足，因为函数要求有返回。
        }
    }

    public void Close(){
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
