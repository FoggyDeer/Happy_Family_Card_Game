import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GamePanel extends JPanel {
    private Game game;
    private JComboBox<Object> c1 = new JComboBox<>();
    private JComboBox<Object> c2 = new JComboBox<>();
    GamePanel(Game game){
        this.game = game;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        gbc.fill = GridBagConstraints.HORIZONTAL;

        add(new JLabel("Select player to ask  "));
        add(c1, gbc);
        add(Box.createRigidArea(new Dimension(50, 15)), gbc);
        add(new JLabel("Select card to ask "));
        gbc.fill = GridBagConstraints.NONE;
        add(c2, gbc);

        JButton button = new JButton("OK");

        button.addActionListener(e -> {
            Player player = game.getPlayers().get(((Player) c1.getSelectedItem()).getPlayerId());
            Card card = (Card) c2.getSelectedItem();
            if (player.getCards().contains(card)) {
                showSuccess(player);
                game.getCurrentPlayer().addCard(card);
                player.removeCard(card);
                game.updateGame(false);
            } else {
                showFail(player);
                game.getCurrentPlayer().addCard(game.getCardsAvailable().get(0));
                if(game.getCardsAvailable().remove(0).equals(card)){
                    showLuckyCard();
                    game.updateGame(false);
                } else
                    game.updateGame(true);
            }
        });

        add(Box.createRigidArea(new Dimension(50, 15)), gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        add(button, gbc);
    }

    public void makeStep(){
        List<Player> players = new ArrayList<>(game.getPlayers());
        players.remove(game.getCurrentPlayer());

        Player pLayer = game.getCurrentPlayer();

        List<Card> cardsAllowed = new ArrayList<>(Game.allCards);
        cardsAllowed.removeIf(card -> {
            if(pLayer.getCards().contains(card))
                return true;

            boolean contains = true;
            for(Card c : pLayer.getCards()){
                if(c.getFamily().equals(card.getFamily())) {
                    contains = false;
                    break;
                }
            }
            return contains;
        });

        cardsAllowed.sort(Comparator.comparing(Card::getFamily).thenComparing(Card::toString));

        c1.removeAllItems();
        c2.removeAllItems();

        for(Object player : players.toArray())
            c1.addItem(player);

        for(Object card : cardsAllowed.toArray())
            c2.addItem(card);
    }

    private void showSuccess(Player player){
        JOptionPane.showMessageDialog(this, "Card acquired from " + player.toString() + "!",
                "Card Acquired", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showLuckyCard(){
        JOptionPane.showMessageDialog(this, "Lucky dip!\nYou take another turn.",
                "Lucky dip", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showFail(Player player){
        JOptionPane.showMessageDialog(this, "Card not found with " + player.toString() + "!\nYou drew "+game.getCardsAvailable().get(0)+" card from the stock.",
                "Card not found", JOptionPane.INFORMATION_MESSAGE);
    }

}
