package dungeonmania.buildableEntity;

import dungeonmania.DungeonInfo;
import dungeonmania.exceptions.InvalidActionException;

public interface Buildable {
    public void craft() throws InvalidActionException;
    public Boolean checkCraftable();
    public void setDungeonInfo(DungeonInfo dungeonInfo);
    public String getType();
}
