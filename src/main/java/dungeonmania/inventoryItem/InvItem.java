package dungeonmania.inventoryItem;

import dungeonmania.DungeonInfo;
import dungeonmania.response.models.ItemResponse;

public interface InvItem {
    public void use();
    public ItemResponse getItemResponse();
    public void setDungeonInfo(DungeonInfo dungeonInfo);
    public String getId();
}
