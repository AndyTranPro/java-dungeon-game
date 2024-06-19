package dungeonmania.staticEntities;

import java.io.Serializable;

import dungeonmania.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class staticEntity extends Entity{
    public abstract Position playerMoveIn(Position p, Direction d); 
    public abstract Position boulderMoveIn(Position p);
}
