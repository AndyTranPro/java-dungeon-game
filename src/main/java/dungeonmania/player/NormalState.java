package dungeonmania.player;

import java.io.Serializable;

import dungeonmania.inventoryItem.Potion.Potion;
import dungeonmania.util.Position;

public class NormalState implements PlayerState, Serializable {

    private Player player;
    public NormalState(Player player) {
        this.player = player;
    }

    public String getStateName() {
        return "Normal";
    }

    public double getAttack() {
        return player.attack;
    }

    public double getHealth() {
        return player.health;
    }

    public Position getPosition() {
        return player.position;
    }

    public int getPotionTime() {
        return -1;
    }

    public void tickPotionTime() {
        Potion potion = player.pullPotion();
        // if there are potion in the queue and the player is not in the influence of any potion then activate the potion
        if (player.getPlayerState().getStateName() == "Normal") {
            if (potion != null) {
                potion.takeAction();
                return;
            }
        }
        return;
    }
    
    public String getPotionId() {
        return "NoPotionUsed";
    }
}
