package popup;

import GuiMainGame.UnitType;
import base.BaseStats;

/**
 * Listener interface used by the UnitStatsPopup.
 * Called when the player confirms the chosen stats for a new unit.
 *
 * Author:
 * Stefan Rajkovic
 */
public interface StatsChosenListener {

    /**
     * Triggered when the player finalizes the unit's stats and type.
     *
     * @param base the base creating the unit
     * @param hp chosen HP value
     * @param attack chosen attack value
     * @param speed chosen speed value
     * @param defence chosen defence value
     * @param unitType the selected unit type
     */
    void onStatsChosen(BaseStats base, int hp, int attack, int speed, int defence, UnitType unitType);
}
