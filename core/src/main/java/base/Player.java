package base;

public class Player {

    private String id;
    private Base base;

    public Player(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setBase(Base base){
        this.base = base;
    }
}
