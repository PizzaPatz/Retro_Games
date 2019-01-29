package racingGame;

public interface CartMoveDelegate {

    boolean canMoveToPosition(int cartNumber, double x, double y);
    boolean isInTrack(double x, double y);

}
