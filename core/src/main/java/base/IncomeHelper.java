package base;

/**
 * Helper that grants gold to a player at the start of their turn.
 * <p>
 * Note: currently applies a fixed income. Gold mine income was planned but
 * not fully integrated due to time constraints; the related logic is left
 * commented out below.
 *
 * @author Emil Hadzic
 */
public class IncomeHelper {

    /**
     * Grants the player their per-turn income.
     *
     * @param player the player receiving income; ignored if null
     */
    public void applyIncome(Player player){
        if (player == null){
            return;
        }
        player.addGold(50);

        //if(base != null && base.getOwner() == player){
        //player.addGold(base.getGoldPerTurn());
        //}

        /*int ownedMines = 0;

        if(mines != null){
            for(GoldMine mine : mines){
                if(mine.isOccupied() && mine.getOwner() == player){
                    player.addGold(mine.getGoldPerTurn());
                }
            }
        }

        int Income;
        if(ownedMines == 0){
            Income = 50;
        } else if(ownedMines == 1){
            Income = 150;
        } else{
            Income = 250;
        }*/
    }
}
