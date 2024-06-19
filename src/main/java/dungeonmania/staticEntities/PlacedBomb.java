package dungeonmania.staticEntities;

import java.util.List;

import dungeonmania.DungeonInfo;
import dungeonmania.Entity;
import dungeonmania.Tick;
import dungeonmania.player.Player;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PlacedBomb extends staticEntity implements Tick{
    
    protected Position pos;
    protected String type = "bomb";
    protected String id;
    protected int radius;

    public PlacedBomb(Position position, String id, int radius) {
        this.pos = position;
        this.id = id;   
        this.radius = radius;   
    }

    @Override
    public void tick() {
        int x_pos = pos.getX();
        int y_pos = pos.getY();
        //check all adjacent positions for active switch and call blast if true
        //above
        List<Entity> entities = dungeonInfo.getEntitiesByPosition(new Position(x_pos, y_pos - 1));
        for (Entity e : entities) {
            if (e instanceof FloorSwitch) {
                FloorSwitch floorSwitch = (FloorSwitch) e;
                if (floorSwitch.isTriggered())
                    blast();
                    return;
            }
            if (e instanceof Wire) {
                Wire w = (Wire) e;
                if (w.getIsConnected()) {
                    blast();
                    return;
                }
            }
        }
        //below
        entities = dungeonInfo.getEntitiesByPosition(new Position(x_pos, y_pos + 1));
        for (Entity e : entities) {
            if (e instanceof FloorSwitch) {
                FloorSwitch floorSwitch = (FloorSwitch) e;
                if (floorSwitch.isTriggered())
                    blast();
                    return;
            }
            if (e instanceof Wire) {
                Wire w = (Wire) e;
                if (w.getIsConnected()) {
                    blast();
                    return;
                }
            }
        }
        //left
        entities = dungeonInfo.getEntitiesByPosition(new Position(x_pos - 1, y_pos));
        for (Entity e : entities) {
            if (e instanceof FloorSwitch) {
                FloorSwitch floorSwitch = (FloorSwitch) e;
                if (floorSwitch.isTriggered())
                    blast();
                    return;
            }
            if (e instanceof Wire) {
                Wire w = (Wire) e;
                if (w.getIsConnected()) {
                    blast();
                    return;
                }
            }
        }
        //right
        entities = dungeonInfo.getEntitiesByPosition(new Position(x_pos + 1, y_pos));
        for (Entity e : entities) {
            if (e instanceof FloorSwitch) {
                FloorSwitch floorSwitch = (FloorSwitch) e;
                if (floorSwitch.isTriggered())
                    blast();
                    return;
            }

            if (e instanceof Wire) {
                Wire w = (Wire) e;
                if (w.getIsConnected()) {
                    blast();
                    return;
                }
            }
        }
    }

    public void blast() {
        // blast everything within a square with the player being in the center
        // square has sides of length radius * 2 + 1
        int start_x_pos = pos.getX() - radius;
        int start_y_pos = pos.getY() - radius;
        int end_y_pos = start_y_pos + (2 * radius);
        int end_x_pos = start_x_pos + (2 * radius);
        // we blast the "square" starting from the top left
        while (start_y_pos <= end_y_pos) {
            int x_pos = start_x_pos;
            while (x_pos <= end_x_pos) {
                List<Entity> entities = dungeonInfo.getEntitiesByPosition(new Position(x_pos, start_y_pos));
                for (Entity e : entities) {
                    if (e instanceof Player) {}
                    else
                        dungeonInfo.getEntityMap().remove(e.getId());
                }
                x_pos ++;
            }
            start_y_pos ++;
        }
    }
    public void setConfig(){
    }
    
    public Position getPos() {
        return pos;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }


    @Override
    public EntityResponse getEntityResponse() {
        EntityResponse response = new EntityResponse(id, type, pos, false);
        return response;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public Position playerMoveIn(Position p, Direction d) {
        return p;
    }

    @Override
    public Position boulderMoveIn(Position p) {
        return p;
    }


    
}
