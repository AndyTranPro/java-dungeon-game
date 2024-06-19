package dungeonmania.staticEntities;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class LogicLightBulb extends LightBulb {
    private String logic;


    public LogicLightBulb(Position p, String id, String logic) {
        super(p, id);
        this.logic = logic;
    }
    
    @Override
    public void tick() {
        Logic l = new Logic(this.dungeonInfo, this.pos, this.logic);
        if (l.logicProcess()) {
            turnOn();
            return;
        }

        turnOff();
    }
}
