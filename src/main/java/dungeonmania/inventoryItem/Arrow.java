package dungeonmania.inventoryItem;

import java.io.Serializable;

import dungeonmania.DungeonInfo;
import dungeonmania.response.models.ItemResponse;

public class Arrow implements InvItem, Serializable {

    private String id;
    private String type = "arrow";

    public Arrow(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
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
    
}
