import javax.swing.*;
import java.util.*;

public class Player {
    private static int Id = 0;

    private List<Card> cards;
    private int playerId = Id++;

    private int familiesCollected = 0;

    Player(List<Card> cards){
        this.cards = cards;
    }

    public void updateFamiliesCollected(JFrame frame){
        List<Card> list = new ArrayList<>(cards);
        list.sort(Comparator.comparing(Card::getFamily));

        Set<String> families = new HashSet<>();
        for(Card card : list)
            families.add(card.getFamily());

        for(String family : families) {
            if(list.stream().filter(card -> card.getFamily().equals(family)).count() >= 4) {
                list.removeIf(card -> card.getFamily().equals(family));
                familiesCollected++;

                JOptionPane.showMessageDialog(frame, "Happy Family!\n"+family+" have been collected.",
                        "Happy Family", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        cards.clear();
        cards.addAll(list);
    }

    public int getPlayerId() {
        return playerId;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public void removeCard(Card card){
        cards.remove(card);
    }

    public int getFamiliesCollected() {
        return familiesCollected;
    }

    @Override
    public String toString() {
        return "Player " + (playerId+1);
    }
}
