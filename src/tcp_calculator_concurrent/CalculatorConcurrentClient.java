package tcp_calculator_concurrent;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CalculatorConcurrentClient {

    public static void main(String[] args) {

        if(args.length < 2) {
            System.out.println("~ ip port");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(host, port)) {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            OutputStream outputStream = socket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            Scanner sc = new Scanner(System.in);
            System.out.println("이름을 입력해 주세요");
            String name = sc.nextLine();
            writer.write(name+"\n");
            writer.flush();

            while(true){
                System.out.println("수식을 입력해주세요 (eg. + 3 8)");
                String expression = sc.nextLine();
                for(int i = 0; i < 500; i++){
                    writer.write(expression + "\n");
                    writer.flush();

                    String receive = reader.readLine();
                    System.out.println(receive);
                }
            }
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
