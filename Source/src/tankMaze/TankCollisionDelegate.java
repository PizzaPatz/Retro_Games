package tankMaze;

public interface TankCollisionDelegate{
    boolean canMoveToPosition(double x, double y);
    void tankHealthDepleted(Tank tank);

}

