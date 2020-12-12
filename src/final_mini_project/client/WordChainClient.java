package final_mini_project.client;

import java.io.*;
import java.net.*;

public class WordChainClient {

    public static void main(String[] args){
        if(args.length < 3){
            System.out.println("~ ip port name");
            return;
        }
        String address = args[0];
        int port = Integer.parseInt(args[1]);
        try {
            Socket socket = new Socket(address, port);
            Runnable r2 = new ClientSend(socket, args[2]);
            Thread tr2 = new Thread(r2);

            Runnable r1 = new ClientReceive(socket);
            Thread tr1 = new Thread(r1);

            tr2.start();
            tr1.start();
        }catch (UnknownHostException error) {
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
