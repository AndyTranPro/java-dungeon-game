package dungeonmania.inventoryItem.Potion;
import java.io.Serializable;

import dungeonmania.DungeonInfo;
import dungeonmania.inventoryItem.InvItem;
import dungeonmania.player.InvincibleState;
import dungeonmania.player.Player;
import dungeonmania.response.models.ItemResponse;

public class InvincibilityPotion implements Potion, InvItem, Serializable {

    private int duration;
    private String id;
    private String type = "invincibility_potion";
    private DungeonInfo dungeonInfo;
    
    public InvincibilityPotion(int duration, String id) {
        this.duration = duration + 1;
        this.id = id;
    }
    @Override
    public int getDuration() {
        return duration;
    }
    @Override
    public void setDuration(int duration) {
        this.duration = duration;
    }

    // For the player to use the potion, place the potion into a queue.
    @Override
    public void use() {
        Player player = dungeonInfo.getPlayer();
        player.addPotion(this);
        dungeonInfo.getItemList().remove(this);
    }

    @Override
    public ItemResponse getItemResponse() {
        return new ItemResponse(id, type);
        
    }
    @Override
    public void setConfig() {
        this.duration = dungeonInfo.getSpecificConfig("invincibility_potion_duration");
    }

    @Override
    public void setDungeonInfo(DungeonInfo dungeonInfo) {
        this.dungeonInfo = dungeonInfo;
    }

    /**
     * This method is used to set the player's invincibility state to true.
     */
    @Override
    public void takeAction() {
        Player player = dungeonInfo.getPlayer();
        player.setPlayerState(new InvincibleState(player, duration, getId()));
    }
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }
    
}
