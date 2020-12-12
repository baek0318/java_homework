package final_mini_project.server;

import java.net.*;
import java.io.*;

public class ServerReceive implements Runnable {

    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    ServerReceive(Socket socket){
        this.socket = socket;
        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException error){
            error.printStackTrace();
        }
    }

    @Override
    public void run() {
        String name = "";
        try {
            if(in != null) {
                name = in.readUTF();
                System.out.println(name);
                WordChainServer.setMember(name, out);
                WordChainServer.setSequence(name);
                System.out.println("client "+name+" is connect");
                WordChainServer.sendToAll(name+" participate in game");
            }
            if(WordChainServer.sequenceSize() < 3) {
                int restCount = 3 - WordChainServer.sequenceSize();
                WordChainServer.sendToAll("게임 시작까지 "+restCount+"명 남았습니다");
            }
            else if(WordChainServer.sequenceSize() == 3){
                WordChainServer.gameStart();
            }

            while(in != null && out != null){
                System.out.println("run");
                if(WordChainServer.finish) break;
                String word = in.readUTF();
                if (WordChainServer.checkWord(word)) {
                    WordChainServer.sendToAll(name+" is right");
                    WordChainServer.pass();
                }
                else {
                    WordChainServer.sendToAll(name+" is wrong so "+name+" fail");
                    WordChainServer.fail();
                }
            }

        } catch (IOException error) {
            error.printStackTrace();
        }finally {
            try {
                System.out.println("disconnect");
                socket.close();
                in.close();
                out.close();
            }catch (IOException error){
                error.printStackTrace();
            }
        }
    }
}
