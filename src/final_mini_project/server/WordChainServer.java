package final_mini_project.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WordChainServer {
    /**
     * 유저들의 <이름, outputStream> 으로 관리하는 HashMap이다
     */
    static ConcurrentHashMap<String, DataOutputStream> member = new ConcurrentHashMap<>();
    /**
     * 유저들이 들어온 순서대로 이름을 저장하는 큐 이다
     */
    static Queue<String> sequence = new LinkedList<>();
    /**
     * 유저들이 제시한 단어들을 저장하는 리스트이다
     */
    static List<String> wordSequence = new ArrayList<>();
    static boolean finish = false;

    public static void main(String[] args){

        if(args.length < 1) {
            System.out.println("~ port");
            return;
        }

        int port = Integer.parseInt(args[0]);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            //첫 제시어를 추가하는 부분
            System.out.println("server start...");
            System.out.println("wait for user...");
            wordSequence.add(WordGenerator.generator());

            while(sequenceSize() < 5){
                Socket socket = serverSocket.accept();
                System.out.println(socket.getInetAddress());
                Runnable r = new ServerReceive(socket);
                Thread multi = new Thread(r);
                multi.start();
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

    static void setMember(String name, DataOutputStream writer){
        member.put(name, writer);
    }

    static void setFinish(boolean finish){
        WordChainServer.finish = finish;
    }
    static void setSequence(String name) {
        sequence.offer(name);
    }

    static int sequenceSize() {
        return sequence.size();
    }

    static void sendToAll(String message) {
        Iterator iter = member.keySet().iterator();

        while(iter.hasNext()){
            try {
                DataOutputStream writer = member.get(iter.next());
                writer.writeUTF(message);
            }
            catch (IOException error){
                error.printStackTrace();
            }
        }
    }

    static void presentWord() {
        int idx = wordSequence.size()-1;
        String lastWord = wordSequence.get(idx);
        sendToAll("present word : "+lastWord);
    }

    static void gameStart() {
        sendToAll("game start!!");
        String first = sequence.peek();
        sendToAll("first turn "+first);
        presentWord();
    }

    static boolean checkWord(String word) {
        int idx = wordSequence.size()-1;
        String lastWord = wordSequence.get(idx);
        int lastIdx = lastWord.length()-1;
        char lastChar = lastWord.charAt(lastIdx);
        char firstChar = word.charAt(0);

        if (lastChar == firstChar) {
            wordSequence.add(word);
            return true;
        }
        else {
            wordSequence.add(WordGenerator.generator());
            return false;
        }
    }

    static void pass() {
        String name = sequence.poll();
        sequence.offer(name);
        String turn = sequence.peek();
        sendToAll(turn+" turn");
        presentWord();
    }

    static void fail() {
        sequence.poll();
        if(WordChainServer.sequenceSize() == 1) {
            sendToAll("==================================");
            sendToAll("winner is " + sequence.poll());
            sendToAll("!!please get out from this room!!");
            sendToAll("==================================");
            finish = true;
        }
        else {
            String turn = sequence.peek();
            sendToAll(turn + " turn");
            presentWord();
        }
    }
}