package dungeonmania.collectableEntity;

import java.io.Serializable;
import java.util.List;

import dungeonmania.inventoryItem.InvItem;
import dungeonmania.inventoryItem.ItemKey;
import dungeonmania.util.Position;

public class Key extends CollectableEntity implements Serializable{
    private int key;

    public Key(String id, String type, Position position, int key){
        super(id, type, position);
        this.key = key;
    }

    public int getKey() {
        return key;
    }
    
    @Override
    public void pickup(){
        //check if there is already a key in the inventory
        List<InvItem> items = dungeonInfo.getItemList();
        for (InvItem i : items){
            if (i.getItemResponse().getType() == "key") {
                return;
            }
        }

        //add this to item list
        InvItem newItem = new ItemKey(id, key);
        items.add(newItem);

        //remove the entity
        dungeonInfo.getEntityMap().remove(this.id);
    }
}
