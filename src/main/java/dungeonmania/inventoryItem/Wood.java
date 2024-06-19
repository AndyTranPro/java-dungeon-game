package dungeonmania.inventoryItem;

import java.io.Serializable;

import dungeonmania.DungeonInfo;
import dungeonmania.response.models.ItemResponse;

public class Wood implements InvItem, Serializable {
    private String id;
    private String type = "wood";

    public Wood(String id){
        this.id = id;
    }

    @Override
    public void use() {
        // TODO Auto-generated method stub
    }

    @Override
    public ItemResponse getItemResponse() {
        return new ItemResponse(id, type);
        
    }

    @Override
    public void setDungeonInfo(DungeonInfo dungeonInfo) {}
    
    @Override
    public String getId() {
        return id;
    }
}
