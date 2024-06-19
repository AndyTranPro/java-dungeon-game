package dungeonmania.player;

import dungeonmania.util.Position;

public interface PlayerState {
    public String getStateName();
    public double getAttack();
    public double getHealth();
    public Position getPosition();
    public int getPotionTime();
    public void tickPotionTime();
    public String getPotionId();
}
