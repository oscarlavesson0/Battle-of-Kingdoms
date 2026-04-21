package unit;

public class Unit {

    //Statestik för Unitsen
    //Alla gubbarna ska ha olika stats och det kommer påverka hur gubbarna interagera med varandra och kartan
    private int maxHP;
    private int currentHP;
    private int attack;
    private int speed;
    private int defence;

    //Postion på kartan
    //Unit måste veta vilken tile var på kartan de är och vilken  tile den ska till
    private int x;
    private int y;

    //Instans av vapen enumet
    //Unites ska veta vilket vapen de har
    private Weapon weapon;

    //Konstruktor för en Unit. Initierar enhetens statistik, vapen och startposition.
    public Unit(int maxHp, int attack, int speed, Weapon weapon, int startX, int startY) {
        this.maxHP = maxHp;
        this.currentHP = maxHp;
        this.attack = attack;
        this.speed = speed;
        this.weapon = weapon;
        this.x = startX;
        this.y = startY;
    }
}
