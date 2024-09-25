import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    // Images
    Image backgroundImage;
    Image birdImage;
    Image topPipeImage;
    Image bottomPipeImage;

    // Bird
    private final int birdX = boardWidth / 8;
    private final int birdY = boardHeight / 2;
    private final int birdWidth = 34;
    private final int birdHeight = 24;

    // The Bird class makes it easier to access the bird's parameters:
    // (x,y,width,height)
    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image image;

        Bird(Image image) {
            this.image = image;
        }
    }

    // pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64; // actual image is 384 pixels
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image image;
        boolean passed = false;

        Pipe(Image image) {
            this.image = image;
        }
    }

    // game logic
    Bird bird;
    int velocityX = -4; // pipes moving left
    int velocityY = 0;
    int gravity = 1;
    ArrayList<Pipe> pipes;
    Random random = new Random();
    boolean gameOver = false;
    boolean drawHitbox = false;
    double score = 0;

    // timer
    Timer gameLoop;
    Timer placePipesTimer;

    FlappyBird() {
        // set the size of the panel
        setPreferredSize(new DimensionUIResource(boardWidth, boardHeight));
        // setBackground(Color.BLUE);
        setFocusable(true);
        addKeyListener(this);

        // load images
        // below method doesn't work
        // ImageIcon(getClass().getResource("./../images/flappybirdbg.png")).getImage();
        backgroundImage = new ImageIcon("images/flappybirdbg.png").getImage();
        birdImage = new ImageIcon("images/flappybird.png").getImage();
        topPipeImage = new ImageIcon("images/toppipe.png").getImage();
        bottomPipeImage = new ImageIcon("images/bottompipe.png").getImage();

        // bird
        bird = new Bird(birdImage);
        pipes = new ArrayList<Pipe>();

        // game timer
        // 60 times per 1000ms
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

        placePipesTimer = new Timer(1500, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();
    }

    public void placePipes() {
        // range of actual height: 128-384 (1/4 to 3/4)
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - (random.nextDouble() * (pipeHeight / 2)));
        int openingSpace = boardHeight / 4;
        Pipe topPipe = new Pipe(topPipeImage);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // to draw the objects
    public void draw(Graphics g) {

        // background
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);

        // bird
        g.drawImage(bird.image, bird.x, bird.y, bird.width, bird.height, null);

        // pipes
        for (Pipe pipe : pipes)
            g.drawImage(pipe.image, pipe.x, pipe.y, pipe.width, pipe.height, null);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 28));
        if (gameOver)
            g.drawString("Game Over: " + (int) score, 10, 35);
        else
            g.drawString(String.valueOf((int) score), 10, 35);

        if (drawHitbox) {
            g.setColor(Color.RED);
            g.drawRect(bird.x, bird.y, bird.width, bird.height);
        } // Draw the square hitbox
    }

    // to move the objects
    public void move() {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0); // prevent bird from going above the screen
        // bird.y = Math.min(bird.y, boardHeight - bird.height); // prevent the bird
        // from falling down

        for (Pipe pipe : pipes) {
            pipe.x += velocityX;
            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5;
            }
            if (collision(bird, pipe))
                gameOver = true;
        }

        if (bird.y > boardHeight)
            gameOver = true;

    }

    // detect collision
    public boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width && // bird's top left isn't past pipe's top right
                a.x + a.width > b.x && // bird's top right is past pipe's top left
                a.y < b.y + b.height && // bird's top left is above pipe's bottom left
                a.y + a.height > b.y; // bird's bottom left is below pipe's top left
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // we want to redraw the panel. repaint() will call paintComponent() which will
        // then call draw()
        move();
        repaint();
        if (gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // when pressing spacebar
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;

            if (gameOver) {
                // restart the game by resetting the fields
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_B)
            drawHitbox = !drawHitbox;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // for listening to character keys such as 'a'
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // for when releasing a key
    }
}
