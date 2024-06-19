package dungeonmania.movingEntity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Direction;

public class RunAwayStrategy implements MercenaryMovingStrategy, Serializable {
    private List<String> movingConstrintItemList = Arrays.asList("wall", "boulder");

    
    public String getStatename() {
        return "RunAway";
    }

    /**
     * move the entity to the next position away from the direction of player
     */
    @Override
    public void move(Entity movingEntity) {
        //DijkstraAlgoPathFinder pathFinder = new DijkstraAlgoPathFinder();

        NewDijkstraAlgoPathFinder pathFinder = new NewDijkstraAlgoPathFinder();
        Direction direction = pathFinder.findNextPathAwayPlayer(movingEntity);
        if (direction == null) {
            return;
        }
        movingEntity.setPos(movingEntity.getPos().translateBy(direction));
        
    }

    public boolean checkObstacle(Moving entity, Direction direction) {
        // check if that direction of entity is a obstacle, return true if it is and vice versa
        return entity.getEntitiesStringByPosition(entity.getPos().translateBy(direction)).stream().anyMatch(element -> movingConstrintItemList.contains(element));
    }
    
}
    
