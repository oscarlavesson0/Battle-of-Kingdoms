package base;

public interface TurnChangeListener {

    public void onPlayerSwitch(Player newPlayer);

    public void onTurnComplete(int newTurnNumber);
}
