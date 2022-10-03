package dk.mjolner.workshop.docker.parameters.game;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Ship {
    private LocalDateTime created;
    private LocalDateTime lastMoveTime;
    private LocalDateTime lastHitTime;
    private long moveCount;
    private long hitCount;
    private int x;
    private int y;
    private final String displayName;
    private final String color;

    public Ship(int x, int y, String displayName) {
        this.x = x;
        this.y = y;
        this.displayName = displayName;
        this.created = LocalDateTime.now();
        this.lastMoveTime = LocalDateTime.now().minusSeconds(10);
        this.lastHitTime = LocalDateTime.now().minusSeconds(10);
        this.hitCount = 0;
        this.moveCount = 0;

        var randomColor = new Color((int)(Math.random() * 0x1000000));
        this.color = "#"+Integer.toHexString(randomColor.getRGB()).substring(2);
    }

    public long getPoints(){
        var now = LocalDateTime.now();
        return (int) (ChronoUnit.SECONDS.between(created, now) / 10.0) + moveCount + hitCount * 5;
    }

    public boolean moveTo(int newX, int newY) {
        if(compareXy(newX, newY)){
            return false;
        }

        if (newX < 0 || newX > 10) {
            return false;
        }
        if (newY < 0 || newY > 10) {
            return false;
        }
        if (isDisallowedBasedOnTime(this.lastMoveTime, 1)){
            return false;
        }
        if (isDistanceMoreThan2Meter(newX, newY)) {
            return false;
        }
        x = newX;
        y = newY;
        moveCount++;
        lastMoveTime = LocalDateTime.now();
        return true;
    }

    public boolean compareXy(int x0, int y0){
       return x == x0 && y == y0;
    }

    public boolean isShootingAllowedBasedOnTime() {
        return !isDisallowedBasedOnTime(lastHitTime, 5);
    }

    public void incrementHitCount() {
        hitCount++;
        lastHitTime = LocalDateTime.now();
    }

    private boolean isDisallowedBasedOnTime(LocalDateTime time, int secondsTimeout) {
        return LocalDateTime.now().isBefore(time.plusSeconds(secondsTimeout));
    }

    private boolean isDistanceMoreThan2Meter(int newX, int newY) {
        var distance = Math.sqrt(Math.pow(x - newX, 2) + Math.pow(y - newY, 2));
        return distance > 2.0;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastMoveTime() {
        return lastMoveTime;
    }

    public void setLastMoveTime(LocalDateTime lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }

    public LocalDateTime getLastHitTime() {
        return lastHitTime;
    }

    public void setLastHitTime(LocalDateTime lastHitTime) {
        this.lastHitTime = lastHitTime;
    }

    public long getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public long getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getDisplayName() {
        return displayName;
    }
}
