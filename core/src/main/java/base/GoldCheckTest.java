package base;

import terrain.Land;
import terrain.Tile;
import unit.CustomUnit;
import unit.UnitController;

public class GoldCheckTest {

    public static void main(String [] args){

        Player p1 = Player.PLAYER_ONE;
        Player p2 = Player.PLAYER_TWO;

        p1.addGold(150);

        System.out.println(p1.getDisplayName() + " har " + p1.getGold() + " guld");
        System.out.println(p2.getDisplayName() + " har " + p2.getGold() + " guld");
        System.out.println();

        Tile[][] grid = new Tile[3][3];
        for(int row = 0; row < 3; row++){
            for(int col = 0; col < 3; col++){
                grid[row][col] = new Tile(col, row, new Land("Land", 1));
            }
        }

        Tile basePosition = grid[1][1];
        BaseStats base = new BaseStats(p1, basePosition);

        UnitController unitController = new UnitController(grid);
        BaseController baseController = new BaseController(unitController);

        //Test1
        System.out.println("Test1: P1 försöker skapa unit (har 150 guld");
        CustomUnit unit1 = baseController.createUnitFromChoice(base);
        System.out.println(unit1);
        System.out.println();

        //Test2
        System.out.println("Test2: P1 försöker skapa unit (har " + p1.getGold() + " guld");
        CustomUnit unit2 = baseController.createUnitFromChoice(base);
        System.out.println(unit2);
        System.out.println();

        //Test3
        System.out.println("Test3: P1 får 100 guld och försöker igen");
        p1.addGold(100);
        System.out.println(p1.getDisplayName() + " har nu " + p1.getGold() + " guld");
        CustomUnit unit3 = baseController.createUnitFromChoice(base);
        System.out.println(unit3);
        System.out.println();
    }
}
