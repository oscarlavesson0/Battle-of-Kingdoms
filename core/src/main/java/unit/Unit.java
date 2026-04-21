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

    //getters för units

    //Hämtar enhetens maximala hälsa.
    public int getMaxHp(){
        return maxHP;
    }

    //Hämtar enhetens nuvarande hälsa.
    public int getCurrentHp(){
        return currentHP;
    }

    //Hämtar enhetens basattackvärde.
    public int getAttack(){
        return attack;
    }

    //Hämtar hur långt enheten kan röra sig per tur.
    public int getSpeed(){
        return speed;
    }

    //Hämtar enhetens baförsvarsvärde.
    public int getDefence() {
        return defence;
    }


    //Hämtar enhetens nuvarande X‑position på kartan.
    public int getX(){
        return x;
    }

    //Hämtar enhetens nuvarande Y‑position på kartan.
    public int getY(){
        return y;
    }

    //Hämtar vapnet som enheten är utrustad med.
    public Weapon getWeapon(){
        return weapon;
    }

    //metoder för hur units rör på sig

    //Uniten får flytta om avståndet är mindre än eller lika med speed.
    public boolean canMoveTo(int newX, int newY) {
        int distance = Math.abs(newX - x) + Math.abs(newY - y);
        return distance <= speed;
    }

    //Flyttar enheten till den angivna positionen om avståndet är tillåtet enligt enhetens speed.
    public void moveTo(int newX, int newY) {
        if (canMoveTo(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

}
