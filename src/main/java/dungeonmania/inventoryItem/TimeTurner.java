package dungeonmania.inventoryItem;

import java.io.Serializable;

import dungeonmania.DungeonInfo;
import dungeonmania.response.models.ItemResponse;

public class TimeTurner implements InvItem, Serializable{
    private String id;
    private String type = "time_turner";
    private DungeonInfo info;

    public TimeTurner(String id) {
        this.id = id;
    }

    @Override
    public void use() {
        //info.rewind(1);
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

    @Override
    public String getId() {
        return id;
    }
    
    
}
