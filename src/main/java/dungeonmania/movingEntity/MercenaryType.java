package dungeonmania.movingEntity;

import dungeonmania.DungeonInfo;
import dungeonmania.player.Player;

public interface MercenaryType {
    public boolean checkBribeAmoountEnough(int bribeAmount);
    public boolean checkBribeDistance(Player player);
    public boolean bribe(int bribeAmount);
    public void mindControl(DungeonInfo dungeonInfo);
    public int getCostToBribe();
    public void setStrategy(MercenaryMovingStrategy strategy);
    public void setPlayerInvisibleStrategy(Player player);
    public void revertStrategy();
    public void setPlayerInvincibleStrategy();
}
