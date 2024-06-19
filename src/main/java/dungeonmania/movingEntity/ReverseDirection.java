package dungeonmania.movingEntity;

import java.io.Serializable;
import java.util.List;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ReverseDirection implements SpiderMovingState, Serializable{
    private Spider spider;

    public ReverseDirection(Spider spider) {
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
     * spider will move in a reverse circle around its swapn position
     * change to clockwise if it hit boulder
     */
    @Override
    public void move() {
        List<Position> adjacentPositions = spider.getSpawnPosition().getAdjacentPositions();
        int index = adjacentPositions.indexOf(spider.getPos());
        // if there the location doesnt have boulders in it, move to the next location
        // else change state to circle direction
        if (spider.getEntitiesStringByPosition(adjacentPositions.get(Math.floorMod((index - 1), adjacentPositions.size()))).contains("boulder")) {
            onClockwise(spider);
            spider.move();
        } else {
            spider.setPosition(adjacentPositions.get(Math.floorMod((index - 1),adjacentPositions.size())));
        }
        
    }
    
}
