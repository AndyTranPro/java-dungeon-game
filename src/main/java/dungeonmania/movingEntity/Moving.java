package dungeonmania.movingEntity;

import java.util.List;

import dungeonmania.player.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public interface Moving {
    public Position getPos();
    public void setPos(Position pos);
    public String getType();
    public double getHealth();
    public void setHealth(double health);
    public double getDamage();
    public void move();
    public String getId();
    public List<String> getEntitiesStringByPosition(Position pos);
}
