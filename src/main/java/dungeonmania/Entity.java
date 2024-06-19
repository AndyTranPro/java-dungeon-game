package dungeonmania;

import java.io.Serializable;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public abstract class Entity implements Serializable, Cloneable{
    protected DungeonInfo dungeonInfo;
    public abstract String getId();
    public abstract String getType();
    public abstract Position getPos();
    public abstract EntityResponse getEntityResponse();
    public abstract void setConfig();
    
    public void setPos(Position pos) {
    }
    
    public void setDungeonInfo(DungeonInfo dungeonInfo) {
        this.dungeonInfo = dungeonInfo;
    }

    public DungeonInfo getDungeonInfo(){
        return dungeonInfo;
    }

    //clone the entity
    public Entity clone(){
        try{
            return (Entity) super.clone();
        }catch(CloneNotSupportedException e){
            e.printStackTrace();
            return null;
        }
    }
}
