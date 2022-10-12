package dk.mjolner.workshop.docker.parameters;

import dk.mjolner.workshop.docker.parameters.game.Ship;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {
    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime twoSecondsAgo = now.minusSeconds(2);
    private final LocalDateTime tweentySecondsAgo = now.minusSeconds(20);
    private final LocalDateTime tenSecondsAgo = now.minusSeconds(10);

    private final LocalDateTime oneHourAgo = now.minusHours(1);
    private final LocalDateTime sixSecondsAgo = now.minusSeconds(6);

    @ParameterizedTest
    @CsvSource({"6,6","4,4","5,4","4,5","6,5","5,6"})
    void setLocation_newLocationShouldBeAllowed(int x, int y) {
        var ship = new Ship(5,5, "");

        ship.moveTo(x, y);

        assertEquals(x, ship.getX());
        assertEquals(y, ship.getY());
    }

    @ParameterizedTest
    @CsvSource({"-1,-1","0,-1","-1,0","2,2", "0,0"})
    void setLocation_newLocationShouldNotBeAllowed(int x, int y) {
        var ship = new Ship(0,0, "");

        ship.moveTo(x, y);

        // location has not changed
        assertEquals(0, ship.getX());
        assertEquals(0, ship.getY());
    }

    @Test
    void setLocation_setLocation_notAllowedBecauseOfTime(){
        var ship = new Ship(0,0, "");

        ship.setLastMoveTime(now);
        ship.moveTo(1,1);

        // not allowed
        assertEquals(0, ship.getX());
        assertEquals(0, ship.getY());

        // allowed
        ship.setLastMoveTime(twoSecondsAgo);
        ship.moveTo(1,1);
        assertEquals(1, ship.getX());
        assertEquals(1, ship.getY());
    }

    @Test
    void getPoints_2secondSinceCreated_2point(){
        var ship = new Ship(0, 0, "");
        ship.setCreated(tweentySecondsAgo);

        var points = ship.getPoints();

        assertEquals(2, points);
    }

    @Test
    void getPoints_20secondsAnd1Move_6point(){
        var ship = new Ship(0, 0, "");
        ship.setCreated(tweentySecondsAgo);

        ship.moveTo(1, 1);
        var points = ship.getPoints();

        assertEquals(2 + 1, points);
    }

    @Test
    void getPoints_20sec1Move1Hit_6point(){
        var ship = new Ship(0, 0, "");
        ship.setCreated(tweentySecondsAgo);
        ship.incrementHitCount();

        ship.moveTo(1, 1);
        var points = ship.getPoints();

        assertEquals(2 + 1 + 5, points);
    }

    @Test
    void isShootingAllowed(){
        var ship = new Ship(0, 0, "");
        ship.setLastHitTime(now);
        assertFalse(ship.isShootingAllowedBasedOnTime());

        ship.setLastHitTime(twoSecondsAgo);
        assertFalse(ship.isShootingAllowedBasedOnTime());

        ship.setLastHitTime(sixSecondsAgo);
        assertTrue(ship.isShootingAllowedBasedOnTime());
    }


}