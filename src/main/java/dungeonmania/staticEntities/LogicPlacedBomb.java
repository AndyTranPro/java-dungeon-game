package dungeonmania.staticEntities;

import dungeonmania.util.Position;

public class LogicPlacedBomb extends PlacedBomb{
    private String logic;

    public LogicPlacedBomb(Position position, String id, int radius, String logic) {
        super(position, id, radius);
        this.logic = logic;
    }
    
    @Override
    public void tick() {
        Logic l = new Logic(this.dungeonInfo, this.pos, this.logic);
        if (l.logicProcess()) {
            blast();
        }
    }
}
