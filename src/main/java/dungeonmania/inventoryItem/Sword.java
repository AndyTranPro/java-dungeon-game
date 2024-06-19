package dungeonmania.inventoryItem;

import java.io.Serializable;

import dungeonmania.DungeonInfo;
import dungeonmania.response.models.ItemResponse;

public class Sword implements InvItem, Serializable {

    private String id;
    private String type = "sword";

    private int attackBonus;
    private int durability;
    private DungeonInfo info;

    public Sword(String id, int attackBonus, int durability) {
        this.id = id;
        this.attackBonus = attackBonus;
        this.durability = durability;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public void use() {
        durability --;
        if (durability <= 0) 
            info.removeInvItemById(id);
    }

    @Override
    public ItemResponse getItemResponse() {
        return new ItemResponse(id, type);
    }
    @Override
    public void setDungeonInfo(DungeonInfo dungeonInfo) {
        info = dungeonInfo;
    }
}
