package dungeonmania.inventoryItem;

import java.io.Serializable;

import dungeonmania.DungeonInfo;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.staticEntities.LogicPlacedBomb;
import dungeonmania.staticEntities.PlacedBomb;
import dungeonmania.util.Position;

public class Bomb implements InvItem, Serializable {

    private String id;
    private String type = "bomb";
    private int radius;
    private DungeonInfo info;
    private String logic = "no_logic";

    public Bomb(String id, int radius) {
        this.id = id;
        this.radius = radius;
    }

    public Bomb(String id, int radius, String logic) {
        this.id = id;
        this.radius = radius;
        this.logic = logic;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public void use() {
        int x = info.getPlayer().getPos().getX();
        int y = info.getPlayer().getPos().getY();
        Position bomb_pos = new Position(x, y);
        if (logic != "no_logic") {
            LogicPlacedBomb bomb = new LogicPlacedBomb(bomb_pos, this.id, this.radius, logic);
            bomb.setDungeonInfo(this.info);
            info.getEntityMap().put(this.id, bomb);
            info.addTick(bomb);
            info.getItemList().remove(this);
        } else {
            PlacedBomb bomb = new PlacedBomb(bomb_pos, this.id, this.radius);
            bomb.setDungeonInfo(this.info);
            info.getEntityMap().put(this.id, bomb);
            info.addTick(bomb);
            info.getItemList().remove(this);
        }
        
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
