package tcp_calculator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorServer {

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
            System.out.println("server recevied expression : "+ receive);
            int result = CalculatorServer.parseExpNCal(receive);

            OutputStream outputStream = socket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write("result : "+result+"\n");
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

    private static int parseExpNCal(String exp) {
        String[] expression = exp.split(" ");
        String type = expression[0];
        int num1 = Integer.parseInt(expression[1]);
        int num2 = Integer.parseInt(expression[2]);
        return checkNCal(type, num1, num2);
    }

    private static int checkNCal(String type, int num1, int num2) {
        if (type.equals("+")) {
            return num1+num2;
        }
        else if (type.equals("-")) {
            return num1-num2;
        }
        else if (type.equals("*")) {
            return num1*num2;
        }
        else {
            return num1/num2;
        }
    }
}
