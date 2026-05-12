package unit;

import base.Player;

public class CustomUnit extends Unit {

    public CustomUnit(int maxHP, int attack, int speed, int defence, Weapon weapon, int startX, int startY, Player player) {
        super(maxHP, attack, speed, defence, weapon, startX, startY, player);
    }
    @Override
    public String toString() {
        return String.format(
            "CustomUnit{hp=%d, atk=%d, spd=%d, def=%d, weapon=%s, pos=(%d,%d)}",
            getMaxHp(), getAttack(), getSpeed(), getDefence(), getWeapon(), getX(), getY()
        );
    }
}
