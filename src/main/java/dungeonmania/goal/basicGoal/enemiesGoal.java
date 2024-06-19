package dungeonmania.goal.basicGoal;

import java.io.Serializable;

import dungeonmania.DungeonInfo;
import dungeonmania.Entity;
import dungeonmania.goal.Goal;
import dungeonmania.movingEntity.Moving;
import dungeonmania.staticEntities.ZombieToastSpawner;

public class enemiesGoal implements Goal, Serializable{
    private DungeonInfo dungeonInfo;
    private int enemy_goal;

    public enemiesGoal(DungeonInfo info, int enemy_goal) {
        this.dungeonInfo = info;
        this.enemy_goal = enemy_goal;
    }

    @Override
    public String evalGoal() {
        int counter = dungeonInfo.getPlayer().getEnemiesMet();
        for (Entity e : dungeonInfo.getEntityMap().values()) {
            if (e instanceof ZombieToastSpawner) {
                return ":enemies";               
            }

        }

        if (counter < enemy_goal) {
            return ":enemies";
        }

        return "";
    }
    
}
