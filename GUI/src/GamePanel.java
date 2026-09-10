import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillOval(300, 200, 50, 50);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new GamePanel();
        frame.add(panel);
        frame.setPreferredSize(new Dimension(700, 500));
        frame.pack();
    }
}
