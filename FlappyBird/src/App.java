import javax       .swing.*;

public class App {
    public static void main(String[] args){
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360,640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        FlappyBird fb = new FlappyBird();
        frame.add(fb);
        frame.pack();
        fb.requestFocus();
        frame.setVisible(true);
    }
}
