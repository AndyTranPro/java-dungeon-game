package dungeonmania.movingEntity;

import java.io.Serializable;
import java.util.List;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class CircleDirection implements SpiderMovingState, Serializable{
    private Spider spider;

    public CircleDirection(Spider spider) {
        this.spider = spider;
    }


    @Override
    public void onClockwise(Spider spider) {
        spider.setCurrentState(new CircleDirection(spider));
    }

    @Override
    public void onCounterClockwise(Spider spider) {
        spider.setCurrentState(new ReverseDirection(spider));
    }


    /**
     * spider will move in a circle around its swapn position
     * change to anticlockwise if it hit boulder
     * dont move if it is between two boulder
     */
    @Override
    public void move() {
        List<Position> adjacentPositions = spider.getSpawnPosition().getAdjacentPositions();
        if (spider.getPos() == spider.getSpawnPosition()) {
            spider.setPosition(spider.getPos().translateBy(Direction.UP));
            return;
        }
        int index = adjacentPositions.indexOf(spider.getPos());
        // if there the location doesnt have boulders in it, move to the next location
        // else change state to reverse direction
        if (spider.getEntitiesStringByPosition(adjacentPositions.get((index + 1) % adjacentPositions.size())).contains("boulder")) {
            // if spider got stuck between two boulder, just dont move
            if(spider.getEntitiesStringByPosition(adjacentPositions.get((index - 1) % adjacentPositions.size())).contains("boulder")) {
                return;
            }
            onCounterClockwise(spider);
            spider.move();
        } else {
            spider.setPosition(adjacentPositions.get((index + 1) % adjacentPositions.size()));
        }
    
    }
    
    
}
