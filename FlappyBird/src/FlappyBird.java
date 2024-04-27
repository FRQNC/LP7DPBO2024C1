import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    int frameWidth = 360, frameHeight = 640;

    Image bgImage, birdImage, lowerPipeImage, upperPipeImage;

    Player player;
    int playerStartPosX = frameWidth / 8, playerStartPosY = frameHeight / 2, playerWidth = 34, playerHeight = 24;

    int pipeStartPosX = frameWidth, pipeStartPosY = 0, pipeWidth = 64, pipeHeight = 512;
    ArrayList<Pipe> pipes;

    Timer gameLoop, pipesCooldown, collisionTimer, scoreTimer;
    int gravity = 0, score = 0;

    JLabel scoreLabel, gameOverLabel;

    boolean gameStarted = false, gameOver = false;

    public FlappyBird(){
        setPreferredSize(new Dimension(360,640));
        setFocusable(true);
        addKeyListener(this);

        //load images
        bgImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        scoreLabel = new JLabel("Score : 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.WHITE);
        add(scoreLabel);

        gameOverLabel = new JLabel("GAME OVER (Press R to restart)");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setHorizontalAlignment(JLabel.CENTER);
        gameOverLabel.setVerticalAlignment(JLabel.CENTER);
        gameOverLabel.setVisible(false);
        add(gameOverLabel);


        player = new Player(playerStartPosX,playerStartPosY,playerWidth,playerHeight,birdImage);
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gameStarted){
                    placePipes();
                }
            }
        });

        collisionTimer = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collisionDetection();
            }
        });

        scoreTimer = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scoreDetection();
                updateScoreLabel();
            }
        });

        gameLoop = new Timer(1000 / 60,this);
        gameLoop.start();
        pipesCooldown.start();
        collisionTimer.start();
        scoreTimer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.drawImage(bgImage,0,0,frameWidth,frameHeight,null);
        g.drawImage(player.getImage(),player.getPosX(),player.getPosY(),player.getWidth(),player.getHeight(),null);
        for(int i = 0;i < pipes.size();i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(),pipe.getPosX(),pipe.getPosY(),pipe.getWidth(),pipe.getHeight(),null);
        }
    }

    public void move(){
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setVelocityX(player.getVelocityX());
        player.setPosY(player.getPosY() + player.getVelocityY());
        if(player.getPosY() >= 610){
            player.setPosY(610);
        }
        player.setPosY(Math.max(player.getPosY(), 0)); //bottom > 640, upper < 0
        player.setPosX(player.getPosX() + player.getVelocityX());
        player.setPosX(Math.max(Math.min(player.getPosX(), 360),0));

        for(int i = 0;i < pipes.size();i++){
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());
        }
    }

    public void placePipes(){
        int randomPipePostY = (int) (pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = frameHeight/4;

        Pipe upperPipe = new Pipe(pipeStartPosX,randomPipePostY,pipeWidth,pipeHeight,upperPipeImage);
        upperPipe.setUpper(true);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX,randomPipePostY + pipeHeight + openingSpace,pipeWidth,pipeHeight,lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public void collisionDetection() {
        boolean collided = false;
        for (Pipe p : pipes) {
            Rectangle pipeBox = new Rectangle(p.getPosX(), p.getPosY(), p.getWidth(), p.getHeight());
            Rectangle playerBox = new Rectangle(player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());

            if (pipeBox.intersects(playerBox)) {
                collided = true;
            }
        }
        if (player.getPosY() >= 610) {
            collided = true;
        }
        if(collided){
            gameOver = true;
            gameOverLabel.setVisible(true);
        }
    }

    public void scoreDetection(){
        if (!gameOver) {
            for (Pipe p : pipes) {
                if(player.getPosX() > (p.getPosX() + player.getWidth()) && !p.isPassed() && p.isUpper()){
                    p.setPassed(true);
                    score += 1;
                    break;
                }
            }
        }
    }


    private void updateScoreLabel() {
        if (!gameOver) {
            scoreLabel.setText("Score: " + score);
        }
//        else{
//            scoreLabel.setText("Score: " + score + " (GAME OVER)");
//        }
    }

    public void resetGame(){
        pipes.clear();
        player.setPosY(playerStartPosY);
        player.setPosX(playerStartPosX);
        gameStarted = false;
        gameOver = false;
        gravity = 0;
        player.setVelocityY(0);
        score = 0;
        gameOverLabel.setVisible(false);
        updateScoreLabel();
    }


    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e){

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE){
                player.setVelocityY(-10);
                if(!gameStarted){
                    gameStarted = true;
                    gravity = 1;
                }
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_R){
            resetGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
