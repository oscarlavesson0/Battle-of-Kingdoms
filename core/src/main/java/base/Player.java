package base;

public class Player {

    private String id;
    private BaseStats base;

    public Player(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setBase(BaseStats base){
        this.base = base;
    }

    public BaseStats getBase() {
        return base;
    }
}
