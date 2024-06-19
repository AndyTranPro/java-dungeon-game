package dungeonmania.movingEntity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import dungeonmania.Entity;
import dungeonmania.util.Direction;

public class RandomStrategy implements MercenaryMovingStrategy , Serializable{
    private List<Direction> directions = Arrays.asList(Direction.DOWN, Direction.UP, Direction.LEFT, Direction.RIGHT);
    private List<String> movingConstrintItemList = Arrays.asList("wall", "boulder");


    /**
     * move the entity to a random direction that would not be a obstacle
     */
    public void move(Moving entity) {
        Random rand = new Random();
        Direction randDirection = directions.get(rand.nextInt(directions.size()));
        // if the entity is surrounded with obstacles, then don't move.
        if (checkObstacle(entity, Direction.UP) && 
        checkObstacle(entity, Direction.DOWN) && 
        checkObstacle(entity, Direction.LEFT) && 
        checkObstacle(entity, Direction.RIGHT)) { 
            return;
        }
        // if the random direction will not lead to a wall or a boulder, move the entity.
        while (checkObstacle(entity, randDirection)) {
            rand = new Random();
            randDirection = directions.get(rand.nextInt(directions.size()));
        }
        entity.setPos(entity.getPos().translateBy(randDirection));
    }

    @Override
    public String getStatename() {
        return "Random";
    }

    public boolean checkObstacle(Moving entity, Direction direction) {
        // check if that direction of entity is a obstacle, return true if it is and vice versa
        return entity.getEntitiesStringByPosition(entity.getPos().translateBy(direction)).stream().anyMatch(element -> movingConstrintItemList.contains(element));
    }

    @Override
    public void move(Entity movingEntity) {
        move((Moving) movingEntity);
    }

}
