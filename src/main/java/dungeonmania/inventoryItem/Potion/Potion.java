package dungeonmania.inventoryItem.Potion;

import dungeonmania.DungeonInfo;

public interface Potion {

    public int getDuration();
    public void setDuration(int duration);
    public void setConfig();
    public void setDungeonInfo(DungeonInfo dungeonInfo);
    public void takeAction();
    public String getId();
    public String getType();
}
