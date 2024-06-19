package dungeonmania.staticEntities;

import dungeonmania.Tick;
import dungeonmania.util.Position;

public class LogicFloorSwitch extends FloorSwitch implements Tick{
    private String logic;

    public LogicFloorSwitch(Position p, String id, String logic) {
        super(p, id);
        this.logic = logic;
    }

    @Override
    public void tick() {
        Logic l = new Logic(this.dungeonInfo, this.pos, this.logic);
        if (l.logicProcess()) {
            isTriggered = true;
            return;
        }

        if (hasBoulder == false) {
            isTriggered = false;
        }
           
    }
    
}
