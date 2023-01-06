import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.TimerTask;

public class GamePanel extends JPanel implements ActionListener, KeyListener
{
    private int[] snakexlength=new int[750];
    private int[] snakeylength=new int[750];
    private int lengthOfSnake=3;

    private int[] xPos={25,50,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550};
    private int[] yPos={75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600};
    private Random random=new Random();
    private int enemyX,enemyY;
    private boolean left=false;
    private  boolean right=true;
    private  boolean up=false;
    private  boolean down=false;
    private int move=0;
    private int score=0;
    private boolean gameOver=false;
    private ImageIcon bg=new ImageIcon(getClass().getResource(("bg.jpg")));
    private ImageIcon leftmouth=new ImageIcon(getClass().getResource(("leftmouth.png")));
    private ImageIcon rightmouth=new ImageIcon(getClass().getResource(("rightmouth.png")));
    private ImageIcon upmouth=new ImageIcon(getClass().getResource(("upmouth.png")));
    private ImageIcon downmouth=new ImageIcon(getClass().getResource(("downmouth.png")));
    private ImageIcon snakeimage=new ImageIcon(getClass().getResource(("snakeimage.png")));
    private ImageIcon enemy=new ImageIcon(getClass().getResource(("enemy.png")));
    private Timer timer;
    private int delay=150;
    GamePanel()
    {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        timer=new Timer(delay,this);
        timer.start();
        newEnemy();
    }
    private void newEnemy()
    {
        enemyX=xPos[random.nextInt(21)];
        enemyY=yPos[random.nextInt(21)];
        for(int i=lengthOfSnake-1;i>=-0;i--)
        {
            if(snakexlength[i]==enemyX&& snakeylength[i]==enemyY)
                newEnemy();
        }
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                newEnemy();
            }
        };
        java.util.Timer timer = new java.util.Timer("Timer");
        long delay = 30000;
        long intevalPeriod = 30 * 1000;
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.setColor(Color.white);
        g.drawRect(24,10,851,56); // for heading boarder
        g.drawRect(24,74,851,551); // for playground

        bg.paintIcon(this,g,11,11);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(25,75,850,550);

        if(move==0)
        {
            snakexlength[0]=100; // head of snake from left of x length
            snakexlength[1]=75; // first body of snake
            snakexlength[2]=50; // second body of snake

            snakeylength[0]=100; // head of snake
            snakeylength[1]=100;
            snakeylength[2]=100;
        }
        if(left){
            leftmouth.paintIcon(this,g,snakexlength[0],snakeylength[0]);
        }
        if(right){
            rightmouth.paintIcon(this,g,snakexlength[0],snakeylength[0]);
        }
        if(up){
            upmouth.paintIcon(this,g,snakexlength[0],snakeylength[0]);
        }
        if(down){
            downmouth.paintIcon(this,g,snakexlength[0],snakeylength[0]);
        }
        for(int i=1;i<lengthOfSnake;i++){
            snakeimage.paintIcon(this,g,snakexlength[i],snakeylength[i]);
        }
        enemy.paintIcon(this,g,enemyX,enemyY);
        if(gameOver)
        {
            g.setColor(Color.white);
            g.setFont(new Font("Arial",Font.BOLD,30));
            g.drawString("Final Score: "+score,300,300);

            g.setFont(new Font("Arial",Font.PLAIN,20));
            g.drawString("Press Space to Restart",300,350);

        }
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,14));
        g.drawString("Score :"+score,750,30);
        g.drawString("Length :"+lengthOfSnake,750,50);
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(int i=lengthOfSnake-1;i>0;i--)
        {
            snakexlength[i]=snakexlength[i-1];
            snakeylength[i]=snakeylength[i-1];
        }
        if(left){
            snakexlength[0]=snakexlength[0]-25;
        }
        if(right){
            snakexlength[0]=snakexlength[0]+25;
        }
        if(up){
            snakeylength[0]=snakeylength[0]-25;
        }
        if(down){
            snakeylength[0]=snakeylength[0]+25;
        }

        collidesWithEnemy();
        collidesWithBody();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE)
        {
            restart();
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT && (!right))
        {
            left=true;
            right=false;
            up=false;
            down=false;
            move++;
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT&&(!left))
        {
            left=false;
            right=true;
            up=false;
            down=false;
            move++;
        }
        if(e.getKeyCode()==KeyEvent.VK_UP &&(!down))
        {
            left=false;
            right=false;
            up=true;
            down=false;
            move++;
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN && (!up))
        {
            left=false;
            right=false;
            up=false;
            down=true;
            move++;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    @Override
    public void keyTyped(KeyEvent e) {

    }
    private void collidesWithEnemy(){
        if(snakexlength[0]==enemyX&& snakeylength[0]==enemyY)
        {
            newEnemy();
            lengthOfSnake++;
            score++;
        }
    }
    private  void collidesWithBody()
    {
        for(int i=lengthOfSnake-1;i>0;i--)
        {
            if(snakexlength[i]==snakexlength[0] && snakeylength[i]==snakeylength[0] )
            {
                timer.stop();
                gameOver=true;
            }
            if(snakexlength[0]>850)
            {
                timer.stop();
                gameOver=true;
            }
            if(snakexlength[0]<25)
            {
                timer.stop();
                gameOver=true;
            }
            if(snakeylength[0]>600)
            {
                timer.stop();
                gameOver=true;
            }
            if(snakeylength[0]<75)
            {
                timer.stop();
                gameOver=true;
            }
        }
    }
    private void restart(){
        gameOver=false;
        move=0;
        score=0;
        lengthOfSnake=3;
        left=false;
        right=true;
        up=false;
        down=false;
        timer.start();
        repaint();
    }
}
