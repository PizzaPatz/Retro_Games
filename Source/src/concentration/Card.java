package concentration;
/* Credit for this game goes to user: swnet on CodeReview.StackExchange.com */

import javax.swing.JButton;

@SuppressWarnings("serial")
public class Card extends JButton{
    private int id;
    private boolean matched = false;


    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }


    public void setMatched(boolean matched){
        this.matched = matched;
    }

    public boolean getMatched(){
        return this.matched;
    }
}
