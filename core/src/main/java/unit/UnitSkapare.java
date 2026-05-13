package unit;

import GuiMainGame.UnitType;
import base.Player;

public class UnitSkapare {

    //Defaultvörden
    private int maxHP = 10;
    private int attack = 0;
    private int speed = 0;
    private int defence = 0;
    private Weapon weapon = Weapon.SWORD;
    private int startX = 0;
    private int startY = 0;

    //Gränsvärden
    private static final int MIN_STAT = 1;
    private static final int MAX_HP = 25;
    private static final int MAX_ATTACK = 5;
    private static final int MAX_SPEED = 5;
    private static final int MAX_DEFENCE = 5;

    //Setters
    public UnitSkapare maxHP(int maxHP) {
        this.maxHP = clamp(maxHP, MIN_STAT, MAX_HP);
        return this;
    }

    public UnitSkapare attack(int attack) {
        this.attack = clamp(attack, MIN_STAT, MAX_ATTACK);
        return this;
    }

    public UnitSkapare speed(int speed) {
        this.speed = clamp(speed, MIN_STAT, MAX_SPEED);
        return this;
    }

    public UnitSkapare defence(int defence) {
        this.defence = clamp(defence, MIN_STAT, MAX_DEFENCE);
        return this;
    }

    public UnitSkapare weapon(Weapon weapon) {
        if (weapon == null)
            throw new IllegalArgumentException("Vapen kan inte vara null.");
        this.weapon = weapon;
        return this;
    }

    public UnitSkapare startPosition(int x, int y) {
        this.startX = x;
        this.startY = y;
        return this;
    }

    private Player player;

    public UnitSkapare player(Player player) {
        if (player == null)
            throw new IllegalArgumentException("Player kan inte vara null.");
        this.player = player;
        return this;
    }



    //Builder
    public CustomUnit build() {
        validate();
        return new CustomUnit(maxHP, attack, speed, defence, weapon, startX, startY, player, UnitType.AXEMAN);
    }

    //Validering
    private void validate() {
        if (maxHP < MIN_STAT)
            throw new IllegalStateException("maxHP måste vara minst " + MIN_STAT);
        if (speed < MIN_STAT)
            throw new IllegalStateException("speed måste vara minst " + MIN_STAT);
        if (player == null)
            throw new IllegalStateException("Player måste anges innan build().");

    }

    //hJÄLPMEDEL
    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    //Statiska gräsvärden
    public static int getMaxHP()      { return MAX_HP; }
    public static int getMaxAttack()  { return MAX_ATTACK; }
    public static int getMaxSpeed()   { return MAX_SPEED; }
    public static int getMaxDefence() { return MAX_DEFENCE; }
    public static int getMinStat()    { return MIN_STAT; }
}
