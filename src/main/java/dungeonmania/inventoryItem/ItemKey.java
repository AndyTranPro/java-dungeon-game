package dungeonmania.inventoryItem;

import java.io.Serializable;

import dungeonmania.DungeonInfo;
import dungeonmania.response.models.ItemResponse;

public class ItemKey implements InvItem, Serializable {

    private String id;
    private int key;
    private String type = "key";

    public ItemKey(String id, int key) {
        this.id = id;
        this.key = key;
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

    public int getKey() {
        return key;
    }
    
    @Override
    public void setDungeonInfo(DungeonInfo dungeonInfo) {}
    
}
