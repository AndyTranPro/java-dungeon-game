package dungeonmania.staticEntities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Portal extends staticEntity implements Serializable{
    private String id;
    private static List<Portal> portalList = new ArrayList<>();
    private Position pos;
    private String colour;
    private String type = "portal";

    public Portal(Position p, String colour, String id) {
        this.id = id;
        this.pos = p;
        this.colour = colour;
        portalList.add(this);
    }

    public Position getPos() {
        return pos;
    }

    public String getColour() {
        return colour;
    }

    public String getId() {
        return id;
    }
    
    public String getType() {
        return type;
    }

    @Override
    public EntityResponse getEntityResponse() {
        EntityResponse response = new EntityResponse(id, type, pos, false);
        return response;
    }

    @Override
    public void setConfig() {
        
    }

    @Override
    public Position playerMoveIn(Position p, Direction d) {
        //find the corresponding portal in checkPos
        Position checkPos = null;
        for (Portal portal : portalList){
            if (portal.getColour().equals(this.colour) && !portal.getPos().equals(this.pos)){
                checkPos = portal.getPos();
                break;
            }
        }

        //check if player is able to teleport
        Position temp = this.teleportTo(checkPos);
        if (temp == checkPos){
            return p;
        } else {
            return temp;
        }
    }

    //teleport to a portal given its position
    public Position teleportTo(Position p){
        Position temp = null;

        //check up
        Position up = p.translateBy(Direction.UP);
        List<Entity> checkEntity = dungeonInfo.getEntitiesByPosition(up);
        for (Entity e : checkEntity){
            if (e instanceof staticEntity){
                staticEntity se = (staticEntity) e;
                temp = se.playerMoveIn(p, Direction.UP);
            }
        }
        if (temp == null || temp.equals(up)){
            return up;
        }

        temp = null;
        //check down
        Position down = p.translateBy(Direction.DOWN);
        checkEntity = dungeonInfo.getEntitiesByPosition(down);
        for (Entity e : checkEntity){
            if (e instanceof staticEntity){
                staticEntity se = (staticEntity) e;
                temp = se.playerMoveIn(p, Direction.DOWN);
            }
        }
        if (temp == null || temp.equals(down)){
            return down;
        }

        temp = null;
        //check left
        Position left = p.translateBy(Direction.LEFT);
        checkEntity = dungeonInfo.getEntitiesByPosition(left);
        for (Entity e : checkEntity){
            if (e instanceof staticEntity){
                staticEntity se = (staticEntity) e;
                temp = se.playerMoveIn(p, Direction.LEFT);
            }
        }
        if (temp == null || temp.equals(left)){
            return left;
        }

        temp = null;
        //check right
        Position right = p.translateBy(Direction.RIGHT);
        checkEntity = dungeonInfo.getEntitiesByPosition(right);
        for (Entity e : checkEntity){
            if (e instanceof staticEntity){
                staticEntity se = (staticEntity) e;
                temp = se.playerMoveIn(p, Direction.RIGHT);

            }
        }                       
        if (temp == null || temp.equals(right)){
            return right;
        }

        return temp;
    }


    @Override
    public Position boulderMoveIn(Position p) {
        return this.pos;
    }
}
