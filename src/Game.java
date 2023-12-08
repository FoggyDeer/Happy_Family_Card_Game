import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Game extends JFrame{
    public static final List<String> familiesList = List.of("Bull, the Butcher", "Bounce, the Barrister", "Butt, the Brewer", "Clamp, the Carpenter", "Dram, the Doctor", "Fin, the Fishmonger", "Goody, the Grocer", "Grub, the Gardener", "Sole, the Shoemaker", "Putty, the Painter",  "Trim, the Tailor");
    public static final List<Card> allCards = new ArrayList<>();

    static {
        for (String s : familiesList) {
            allCards.add(new Card("Mr. " + s + " (Father)", s));
            allCards.add(new Card("Mrs. " + s + " (Mother)", s));
            allCards.add(new Card("Master " + s + " (Son)", s));
            allCards.add(new Card("Miss " + s + " (Daughter)", s));
        }
        System.out.println(allCards);
    }
    private List<Card> cardsAvailable = allCards;
    private List<Player> players = new ArrayList<>();
    private int currentPlayerId = 0;
    private JLabel cardsPanelLabel;
    private JList<Card> cardsList;
    private GamePanel gamePanel;
    public Game() {

        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(200,200));
        setLocationRelativeTo(null);
        setVisible(true);

        Collections.shuffle(cardsAvailable);

        List<Card> temp;
        for(int i = 0; i < 4; i++){
            temp = new ArrayList<>(cardsAvailable.subList(6*i, 6*i + 6));
            temp.sort(Comparator.comparing(Card::getFamily).thenComparing(Card::toString));
            players.add(new Player(temp));
        }
        cardsAvailable = new ArrayList<>(cardsAvailable.subList(25, cardsAvailable.size()));

        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new BorderLayout());
        cardsPanelLabel = new JLabel();
        cardsPanelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardsPanelLabel.setFont(new Font("San-Serif", Font.BOLD, 18));
        cardsList = new JList<>(new DefaultListModel<>());
        cardsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setIcon(new ImageIcon(((Card)value).getImage()));
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(cardsList);
        cardsPanel.add(cardsPanelLabel, BorderLayout.NORTH);
        cardsPanel.add(scrollPane, BorderLayout.CENTER);

        gamePanel = new GamePanel(this);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cardsPanel, gamePanel);
        add(splitPane);
        play();
    }

    public boolean checkGameStatus(){
        return cardsAvailable.size() > 0 && players.stream().anyMatch(p -> p.getCards().size() == 0);
    }

    public void nextPlayer(){
        currentPlayerId++;
        currentPlayerId = currentPlayerId % 4;
    }

    public void play(){
        ((DefaultListModel<Card>) cardsList.getModel()).removeAllElements();
        cardsPanelLabel.setText("Player " + (currentPlayerId + 1) + " cards:");
        for (Card card : players.get(currentPlayerId).getCards())
            ((DefaultListModel<Card>) cardsList.getModel()).addElement(card);
        gamePanel.makeStep();
    }

    public void updateGame(boolean nextPlayer){
        boolean end = false;
        for(int i = 0; i < 4; i++){
            players.get(i).updateFamiliesCollected(this);
            if(players.get(i).getCards().size() == 0)
                end = true;
        }

        if(nextPlayer)
            nextPlayer();

        if(cardsAvailable.size() == 0 || end){
            end();
        } else {
            play();
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer(){
        return players.get(currentPlayerId);
    }

    public List<Card> getCardsAvailable() {
        return cardsAvailable;
    }

    public void end(){
        removeAll();
        repaint();

        players.sort((p1, p2) -> Integer.compare(p2.getFamiliesCollected(), p1.getFamiliesCollected()));
        String text = players.get(0).getFamiliesCollected() == 0 ? "Draw!" : players.get(0) + " won! Families collected: " + players.get(0).getFamiliesCollected();
        JOptionPane.showMessageDialog(this, text,
                "Card not found", JOptionPane.INFORMATION_MESSAGE);
    }
}