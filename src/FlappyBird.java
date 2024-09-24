import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

public class FlappyBird extends JPanel {
    int boardWidth = 360;
    int boardHeight = 640;

    Image backgroundImage;
    Image birdImage;
    Image topPipeImage;
    Image bottomPipeImage;

    FlappyBird() {
        setPreferredSize(new DimensionUIResource(boardWidth, boardHeight));
        // setBackground(Color.BLUE);

        backgroundImage = new ImageIcon("images/flappybirdbg.png").getImage();
        // below method doesn't work
        // backgroundImage = new ImageIcon(getClass().getResource("./../images/flappybirdbg.png")).getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);
    }
}
