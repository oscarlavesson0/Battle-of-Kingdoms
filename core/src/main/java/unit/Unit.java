package unit;

import GuiMainGame.UnitType;
import base.Player;

public class Unit {

    private int maxHP;
    private int currentHP;
    private int attack;
    private int speed;
    private int defence;
    private int x;
    private int y;
    private Weapon weapon;
    private Player player;
    private UnitType unitType;
    private boolean alive = true;



    public Unit(int maxHp, int attack, int speed, int defence, Weapon weapon, int startX, int startY, Player player, UnitType unitType) {
        this.maxHP = maxHp;
        this.currentHP = maxHp;
        this.attack = attack;
        this.speed = speed;
        this.defence = defence;
        this.weapon = weapon;
        this.x = startX;
        this.y = startY;
        this.player = player;
        this.unitType = unitType;
    }

    public int getMaxHp()      {
        return maxHP; }
    public int getCurrentHp()  { return currentHP; }
    public int getAttack()     { return attack; }
    public int getSpeed()      { return speed; }
    public int getDefence()    { return defence; }
    public int getX()          { return x; }
    public int getY()          { return y; }
    public Weapon getWeapon()  { return weapon; }
    public Player getPlayer()     { return player; }
    public boolean isAlive()   { return alive; }

    public boolean canMoveTo(int newX, int newY) {
        int distance = Math.abs(newX - x) + Math.abs(newY - y);
        return distance <= speed;
    }

    public void takeDamage(int damage){
        int actualDamage = Math.max(1, damage - defence);
        currentHP -= actualDamage;
        if(currentHP <= 0){
            currentHP = 0;
            alive = false;
        }
    }

    public void addToCurrentHP(int healthPoints) {
        if (currentHP + healthPoints > maxHP) {
            currentHP = maxHP;
        }
        else{
            currentHP += healthPoints;
        }
    }

    public void moveTo(int newX, int newY) {
        if (canMoveTo(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    public void setPosition(int newX, int newY) {
        x = newX;
        y = newY;
    }
    public UnitType getUnitType() {
        return unitType;
    }
}
