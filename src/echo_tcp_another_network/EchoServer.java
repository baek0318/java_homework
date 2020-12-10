package echo_tcp_another_network;

import java.net.*;
import java.io.*;

public class EchoServer {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("~ port");
            return;
        }

        int port = Integer.parseInt(args[0]);

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("server is ready to listen request");

            Socket socket = serverSocket.accept();
            System.out.println("client "+socket.getInetAddress()+" is connected");

            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String receive = reader.readLine();
            System.out.println("server recevied message : "+ receive);

            OutputStream outputStream = socket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(receive+"\n");
            writer.flush();
        }
        catch(IOException error){
            System.out.println("socket open error occur");
            error.printStackTrace();
        }
        catch(IllegalArgumentException error){
            System.out.println("port number is outside the specified range");
            error.printStackTrace();
        }
    }

}
