package dungeonmania.player;

import java.io.Serializable;
import java.util.List;

import dungeonmania.inventoryItem.Potion.Potion;
import dungeonmania.movingEntity.BribedStrategy;
import dungeonmania.movingEntity.Mercenary;
import dungeonmania.movingEntity.MercenaryType;
import dungeonmania.movingEntity.NotBribedStrategy;
import dungeonmania.movingEntity.RandomStrategy;
import dungeonmania.movingEntity.RunAwayStrategy;
import dungeonmania.movingEntity.ZombieToast;
import dungeonmania.movingEntity.ZombieType;
import dungeonmania.util.Position;

public class InvincibleState implements PlayerState, Serializable {

    private double attack = Double.MAX_VALUE;
    private double health = Double.MAX_VALUE;
    private int potionTime;
    private Player player;
    private String potionId;

    public InvincibleState(Player player, int potionTime, String PotionId) {
        this.player = player;
        this.potionTime = potionTime;
        this.potionId = potionId;

        List<MercenaryType> allMencenaryType = player.getDungeonInfo().getAllMencenaryType();
        for (MercenaryType mencenaryType : allMencenaryType) {
            mencenaryType.setPlayerInvincibleStrategy();
        }
        List<ZombieType> allZombieType = player.getDungeonInfo().getAllZombieType();
        for (ZombieType zombieType : allZombieType) {
            zombieType.setStrategy(new RunAwayStrategy());
        }
    }

    public String getStateName() {
        return "Invincible";
    }

    public double getAttack() {
        return attack;
    }

    public double getHealth() {
        return health;
    }

    public Position getPosition() {
        return player.position;
    }

    public int getPotionTime() {
        return potionTime;
    }

    // Reduce the potion duration time by 1. then pull the potion from the queue and activate it or return to normal state if no potion in the queue.
    public void tickPotionTime() {
        potionTime--;
        if (getPotionTime() < 0) {
            List<MercenaryType> allMencenaryType = player.getDungeonInfo().getAllMencenaryType();
            // return all mencenary to its original state
            for (MercenaryType mencenaryType : allMencenaryType) {
                mencenaryType.revertStrategy();
            }
            List<ZombieType> allZombieType = player.getDungeonInfo().getAllZombieType();
            for (ZombieType zombieType : allZombieType) {
                zombieType.setStrategy(new RandomStrategy());
            }
            
            Potion potion = player.pullPotion();
            if (potion != null) {
                potion.takeAction();
                return;
            }
            player.setPlayerState(new NormalState(player));
        }
    }

    public String getPotionId() {
        return potionId;
    }

}

