package tcp_calculator;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CalculatorClient {

    public static void main(String[] args) {

        if(args.length < 2) {
            System.out.println("~ ip port");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(host, port)) {

            OutputStream outputStream = socket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            System.out.println("수식을 입력해주세 (eg. + 3 8)");
            Scanner sc = new Scanner(System.in);
            String expression = sc.nextLine();
            writer.write(expression+"\n");
            writer.flush();

            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String receive = reader.readLine();
            System.out.println(receive);

        }
        catch (UnknownHostException error) {
            System.out.println("Server not found");
        }
        catch (IOException error) {
            System.out.println("I/O error");
        }
        catch (IllegalArgumentException error) {
            System.out.println("port number outlined specific port range");
        }
    }
}
