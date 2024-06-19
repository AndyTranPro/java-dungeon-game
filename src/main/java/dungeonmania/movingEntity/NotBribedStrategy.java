package dungeonmania.movingEntity;

import java.io.Serializable;
import java.util.ArrayList;

import dungeonmania.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class NotBribedStrategy implements MercenaryMovingStrategy, Serializable{
    
    public String getStatename() {
        return "NotBribed";
    }

    /**
     * move the entity to the next position in the direction of player
     */
    @Override
    public void move(Entity movingEntity) {
        //DijkstraAlgoPathFinder pathFinder = new DijkstraAlgoPathFinder();

        NewDijkstraAlgoPathFinder pathFinder = new NewDijkstraAlgoPathFinder();
        Position targetPos = movingEntity.getDungeonInfo().getPlayer().getPos();
        Direction direction = pathFinder.findNextPath(movingEntity, targetPos);
        if (direction == null) {
            return;
        }
        movingEntity.setPos(movingEntity.getPos().translateBy(direction));
    }
    
}
