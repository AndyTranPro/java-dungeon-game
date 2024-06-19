package dungeonmania.movingEntity;

public interface SpiderMovingState {
    public void onClockwise(Spider spider);
    public void onCounterClockwise(Spider spider);
    public void move();
}
