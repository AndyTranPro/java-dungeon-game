package dungeonmania.movingEntity;

import dungeonmania.Entity;

public interface MercenaryMovingStrategy {
    public void move(Entity movingEntity);
    public String getStatename();
}
