package dungeonmania.movingEntity;

import java.io.Serializable;

import dungeonmania.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BribedStrategy implements MercenaryMovingStrategy, Serializable {
    
    public String getStatename() {
        return "Bribed";
    }

    /**
     * move the entity to the next position in the direction of player last position
     */
    @Override
    public void move(Entity movingEntity) {
        //DijkstraAlgoPathFinder pathFinder = new DijkstraAlgoPathFinder();
        NewDijkstraAlgoPathFinder pathFinder = new NewDijkstraAlgoPathFinder();
        Position targetPos = movingEntity.getDungeonInfo().getPlayer().getPlayerLastPosition();
        Direction direction = pathFinder.findNextPath(movingEntity, targetPos);
        if (direction == null) {
            return;
        } 
        // if that move would cause the entity to hit player, dont move
        if (checkPlayer(movingEntity, direction)) {
            return;
        }
        movingEntity.setPos(movingEntity.getPos().translateBy(direction));
    }

    // check if that position is player
    public boolean checkPlayer(Entity entity, Direction direction) {
        // check if that direction of entity is a obstacle, return true if it is and vice versa
        return entity.getPos().translateBy(direction) == entity.getDungeonInfo().getPlayer().getPos();
    }
    
}
