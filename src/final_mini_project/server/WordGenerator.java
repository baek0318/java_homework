package final_mini_project.server;

public class WordGenerator {

    private static String[] words = {"work","home","university","network","concurrency","tcp","learning","java","thread","amazon"};

    static String generator() {
        int idx = (int) (Math.random()*10);
        return words[idx];
    }
}
