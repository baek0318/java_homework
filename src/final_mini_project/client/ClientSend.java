package final_mini_project.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientSend implements Runnable {
    Socket socket;
    DataOutputStream out;
    String name;

    public ClientSend(Socket socket, String name){
        this.socket = socket;
        this.name = name;
        try {
            this.out = new DataOutputStream(socket.getOutputStream());
        }catch(IOException error){
            error.printStackTrace();
        }
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);

        try {
            if (out != null) {
                out.writeUTF(name);
            }
            while(out != null){
                out.writeUTF(sc.nextLine());
            }
        }catch(IOException error){
            error.printStackTrace();
        }finally {

        }
    }
}
