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
    private final Socket socket;
    private final InetAddress ADRESS;

    MultiClient(Socket socket) {
        this.socket = socket;
        this.ADRESS = socket.getInetAddress();
    }

    @Override
    public void run() {
        try {
            System.out.println("client "+ADRESS+" is connected");

            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String receive = reader.readLine();
            System.out.println("server recevied expression : "+ receive);
            int result = parseExpNCal(receive);

            OutputStream outputStream = socket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write("("+ADRESS+")"+"result : "+result+"\n");
            writer.flush();

            System.out.println("client "+ADRESS+"is disconnected");
            socket.close();
        }
        catch (IOException error) {
            System.out.println(error.getCause().toString());
            error.printStackTrace();
        }
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