import javax.swing.*;
import java.awt.*;

public class Card extends ImageIcon {
    private String cardName;
    private String family;

    Card(String cardName, String family){
        super("images\\"+cardName+".jpg");
        Image icon = super.getImage().getScaledInstance(210, 320, Image.SCALE_AREA_AVERAGING);
        setImage(icon);
        this.cardName = cardName;
        this.family = family;
    }

    public String getFamily() {
        return family;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Card card))
            return false;

        return cardName.equals(card.cardName) && family.equals(card.family);
    }

    @Override
    public String toString() {
        return cardName;
    }
}
