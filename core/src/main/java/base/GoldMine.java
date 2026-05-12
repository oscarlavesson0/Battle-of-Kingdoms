package base;
//Can be occupied by a player to receive more gold each turn.
public class GoldMine {

    private Player owner;
    private final int goldPerTurn = 100;

    public GoldMine(){
        this.owner = null;
    }

    public boolean isOccupied(){
        return owner != null;
    }

    public Player getOwner(){
        return owner;
    }

    public void occupy(Player player){
        this.owner = player;
    }

    public void clear(){
        this.owner = null;
    }

    public int getGoldPerTurn(){
        return goldPerTurn;
    }
}
