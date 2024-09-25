import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); // center the frame in the screen
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when clicking close button, terminate the program

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird); // add JPanel object
        frame.pack(); // dimensions will take into account title bar if you don't do pack()
        flappyBird.requestFocus(); // requests input focus 
        frame.setVisible(true); // make the window visible after adding JPanel
    }
}
