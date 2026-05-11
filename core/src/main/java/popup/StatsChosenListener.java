package popup;

import base.BaseStats;
public interface StatsChosenListener {
    void onStatsChosen(BaseStats base, int hp, int attack, int speed, int defence);
}
