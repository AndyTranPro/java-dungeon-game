package dungeonmania.staticEntities;

import java.io.Serializable;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends staticEntity implements Serializable{
    private String id;
    private Position pos;
    private int Spawntime;
    private int TimeToSpawn;
    private String type = "zombie_toast_spawner";

    public ZombieToastSpawner(Position p, String id) {
        this.id = id;
        this.pos = p; 
        
    }

    public void setConfig(){
        this.Spawntime = dungeonInfo.getSpecificConfig("zombie_spawn_rate"); 
        TimeToSpawn = Spawntime;
    }

    public Position getPos() {
        return pos;
    }   

    public void spawn(){
        if (Spawntime == 0){
            return;
        }
        TimeToSpawn = TimeToSpawn - 1;
        if (TimeToSpawn == 0){
            //spawn the zombie 
            Position p = getSpawnPos();
            if (p == this.pos){
                return;
            }
            dungeonInfo.zombieSpawn(p);
            //refresh time to spawn
            TimeToSpawn = Spawntime;
        }
    }



    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public EntityResponse getEntityResponse() {
        EntityResponse response = new EntityResponse(id, type, pos, true);
        return response;
    }

    @Override
    public Position playerMoveIn(Position p, Direction d) {
        return p;
    }

    @Override
    public Position boulderMoveIn(Position p) {
        return p;
    }

    public Position getSpawnPos(){
        Position temp = null;
        Position p = this.pos;

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
}
