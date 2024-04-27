import javax.swing.*;

public class App {
    public static void main(String[] args){
        FlappyMenu menu = new FlappyMenu();
        menu.setSize(200, 100);
        menu.setLocationRelativeTo(null);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setVisible(true);
    }
}
