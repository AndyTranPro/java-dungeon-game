package dungeonmania.movingEntity;

public interface ZombieType {
    public void setStrategy(MercenaryMovingStrategy strategy);
    public void revertStrategy();
}
