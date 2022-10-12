package dk.mjolner.workshop.docker.parameters.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipServiceTest {

    @Test
    public void move_firstMove_isAdded(){
        var sut = new ShipService();

        var isMoved = sut.move("A", 1, 2);

        assertTrue(isMoved);
        var ship = sut.getShip("A");

        assertEquals("A",ship.getDisplayName());
        assertEquals(1, ship.getX());
        assertEquals(2, ship.getY());
    }

    @Test
    public void move_secondMove_isNotValidBecauseOfTime(){
        var sut = new ShipService();

        sut.move("A", 1, 2);
        var isMoved = sut.move("A", 2, 2);

        assertFalse(isMoved);
    }

    @Test
    public void move_secondMove_isAllowed(){
        var ship = new Ship(1, 2, "A");
        var sut = new ShipService(ship);

        var isMoved = sut.move("A", 2, 2);

        assertTrue(isMoved);
    }

    @Test
    public void shoot_coordinatesOfTheOtherShip_isAllowed(){
        var sut = new ShipService(
                new Ship(1, 1, "A"),
                new Ship(2, 2, "B")
        );

        var wasShootingAllowed = sut.shoot("A", 2, 2);

        assertTrue(wasShootingAllowed);
        assertEquals(5, sut.getShip("A").getPoints());
    }

    @Test
    public void shoot_invalidCoordinates_isNotAllowed(){
        var sut = new ShipService(
                new Ship(1, 1, "A"),
                new Ship(2, 2, "B")
        );

        assertFalse(sut.shoot("A", 1, 1)); // Shooting own coordinates
        assertFalse(sut.shoot("A", 4, 4)); // Target missed
        assertFalse(sut.shoot("C", 4, 4)); // The ship is not present yet
    }
}