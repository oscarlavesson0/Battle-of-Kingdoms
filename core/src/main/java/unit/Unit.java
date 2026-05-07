package unit;

public class Unit {

    private int maxHP;
    private int currentHP;
    private int attack;
    private int speed;
    private int defence;
    private int x;
    private int y;
    private Weapon weapon;

    public Unit(int maxHp, int attack, int speed, int defence, Weapon weapon, int startX, int startY) {
        this.maxHP = maxHp;
        this.currentHP = maxHp;
        this.attack = attack;
        this.speed = speed;
        this.defence = defence;
        this.weapon = weapon;
        this.x = startX;
        this.y = startY;
    }

    public int getMaxHp()      { return maxHP; }
    public int getCurrentHp()  { return currentHP; }
    public int getAttack()     { return attack; }
    public int getSpeed()      { return speed; }
    public int getDefence()    { return defence; }
    public int getX()          { return x; }
    public int getY()          { return y; }
    public Weapon getWeapon()  { return weapon; }

    public boolean canMoveTo(int newX, int newY) {
        int distance = Math.abs(newX - x) + Math.abs(newY - y);
        return distance <= speed;
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
}
