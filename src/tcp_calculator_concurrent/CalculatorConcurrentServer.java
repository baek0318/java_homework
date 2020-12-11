package tcp_calculator_concurrent;

import tcp_calculator.CalculatorServer;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorConcurrentServer {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("~ port");
            return;
        }

        int port = Integer.parseInt(args[0]);

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("server is ready to listen request");

            while (true) {
                Socket socket = serverSocket.accept();
                Runnable r = new MultiClient(socket);
                Thread thread = new Thread(r);
                thread.start();
            }

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

class MultiClient implements Runnable {
    private Socket socket;
    private InputStream in;
    private OutputStream out;

    MultiClient(Socket socket){
        try {
            this.socket = socket;
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
        }
        catch(IOException error){
            error.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String name = insertName(reader);
            if(name == null) return;

            while(true) {
                String receive = reader.readLine();
                if(receive.equals("q")) {
                    break;
                }
                System.out.println("server recevied expression : "+ receive);
                int result = parseExpNCal(receive);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
                writer.write("("+name+")"+"result : "+result+"\n");
                writer.flush();
            }
            System.out.println("client "+name+" is disconnected");
            socket.close();
        }
        catch (IOException error) {
            System.out.println(error.getCause().toString());
            error.printStackTrace();
        }
    }

    private String insertName(BufferedReader reader){
        String name;
        try {
            name = reader.readLine();
            System.out.println("client "+name+" is connect");
        }
        catch (IOException error){
            error.printStackTrace();
            return null;
        }
        return name;
    }

    private int parseExpNCal(String exp) {
        String[] expression = exp.split(" ");
        String type = expression[0];
        int num1 = Integer.parseInt(expression[1]);
        int num2 = Integer.parseInt(expression[2]);
        return checkNCal(type, num1, num2);
    }

    private int checkNCal(String type, int num1, int num2) {
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