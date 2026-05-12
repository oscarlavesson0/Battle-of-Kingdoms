package base;

//Help class for the turn system to give the players each turn.
public class IncomeHelper {

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
