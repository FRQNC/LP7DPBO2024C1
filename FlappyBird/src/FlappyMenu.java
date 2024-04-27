import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FlappyMenu extends JFrame{
    private JButton playFlappyBirdButton;
    private JPanel panel1;

    FlappyMenu(){
        playFlappyBirdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        });

        setLayout(new FlowLayout());
        add(playFlappyBirdButton);
    }
}
