package dungeonmania.movingEntity;

import java.util.Arrays;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.staticEntities.SwampTile;
import dungeonmania.util.Position;

public class MoveFactorCounter {
    Entity movingEntity;
    Position targetPos;
    List<String> movingFactorItemList = Arrays.asList("swamp_tile");
    int movementFactor = 0;

    public MoveFactorCounter(Entity movingEntity, Position targetPos) {
        this.movingEntity = movingEntity;
        this.targetPos = targetPos;
        List<Entity> posEntities = movingEntity.getDungeonInfo().getEntitiesByPosition(targetPos);
        Entity swamp_tile = posEntities.stream().filter(element -> "swamp_tile".equals(element.getType()))
        .findAny()
        .orElse(null);
        if(swamp_tile != null){
            movementFactor = ((SwampTile) swamp_tile).getMovementFactor();
        }
    }

    public boolean movementFactorCounter(){
        if (movementFactor == 0) {
            return true;
        }
        movementFactor -= 1;
        return false;
    }
}
