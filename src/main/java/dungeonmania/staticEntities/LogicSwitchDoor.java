package dungeonmania.staticEntities;

import dungeonmania.util.Position;

public class LogicSwitchDoor extends SwitchDoor{
    private String logic;
        
    public LogicSwitchDoor(Position position, int key, String id, String logic) {

        super(position, key, id);
        this.logic = logic;
    }
    
    @Override
    public void tick() {
        Logic l = new Logic(this.dungeonInfo, this.pos, this.logic);
        if (l.logicProcess()) {
            Open();
            return;
        }

        if (canClose == true) {
            Close();
        }
    }
}
