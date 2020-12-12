package final_mini_project.client;

import java.net.*;
import java.io.*;

public class ClientReceive implements Runnable{
    Socket socket;
    DataInputStream in;

    public ClientReceive(Socket socket) {
        this.socket = socket;
        try {
            this.in = new DataInputStream(socket.getInputStream());
        }catch(IOException error) {
            error.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while(in != null) {
                System.out.println(in.readUTF());
            }
        }catch (IOException error) {
            System.out.println("IOException occur");
            error.printStackTrace();
        } finally {

        }
    }
}
