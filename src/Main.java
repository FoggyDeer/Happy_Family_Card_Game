import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        SwingUtilities.invokeLater(Game::new);
    }
}
