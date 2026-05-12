package base;

import java.util.List;

//Help class for the turn system to give the players each turn.
public class IncomeHelper {

    public void applyIncome(Player player, BaseStats base, List<GoldMine> mines){
        if (player == null){
            return;
        }

        if(base != null && base.getOwner() == player){
            player.addGold(base.getGoldPerTurn());
        }

        if(mines != null){
            for(GoldMine mine : mines){
                if(mine.isOccupied() && mine.getOwner() == player){
                    player.addGold(mine.getGoldPerTurn());
                }
            }
        }
    }
}
