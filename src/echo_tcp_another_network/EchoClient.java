package echo_tcp_another_network;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class EchoClient {

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
            System.out.println("메시지를 입력해주세요");
            Scanner sc = new Scanner(System.in);
            String message = sc.nextLine();
            writer.write(message+"\n");
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
