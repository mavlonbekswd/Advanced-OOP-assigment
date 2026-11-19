package battleship.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** Dependency-free Board behaviour checks; not JUnit. */
public final class BoardSelfTest {
    private static int checks;

    public static void main(String[] args) {
        Board board = new Board();
        check(board.getSize() == 10 && board.getShipCount() == 0, "Default empty board");
        check(!board.allShipsSunk(), "Empty board cannot be victory");
        rejects(IllegalArgumentException.class, () -> new Board(0));
        rejects(IllegalArgumentException.class, () -> new Board(-1));
        rejects(IllegalStateException.class, () -> board.fireAt(c(0, 0)));
        rejects(NullPointerException.class, () -> board.canPlaceShip(null));
        rejects(NullPointerException.class, () -> board.placeShip(null));
        rejects(NullPointerException.class, () -> board.fireAt(null));
        check(!board.canPlaceShip(ship(-1, 0, -1, 1)), "Negative row");
        check(!board.canPlaceShip(ship(0, -1, 0, 0)), "Negative column");
        check(!board.canPlaceShip(ship(9, 9, 9, 10)), "Right overflow");
        check(!board.canPlaceShip(ship(9, 9, 10, 9)), "Bottom overflow");
        rejects(IllegalArgumentException.class, () -> board.placeShip(ship(9, 9, 9, 10)));
        check(board.getShipCount() == 0, "Failed placement is atomic");
        Ship external = ship(0, 0, 0, 1);
        board.placeShip(external);
        external.registerHit(c(0, 0));
        external.registerHit(c(0, 1));
        check(board.getSunkShipCount() == 0, "Board owns independent ship state");
        check(!board.canPlaceShip(ship(0, 1, 0, 2)), "Overlap");
        check(!board.canPlaceShip(ship(0, 2, 0, 3)), "Horizontal contact");
        check(!board.canPlaceShip(ship(1, 0, 1, 1)), "Vertical contact");
        check(!board.canPlaceShip(ship(1, 2, 1, 3)), "Diagonal contact");
        check(board.canPlaceShip(ship(2, 2, 3, 2)), "One empty cell gap");
        board.placeShip(ship(9, 8, 9, 9));
        check(board.getShipCount() == 2, "Boundary ship accepted");
        Map<Coordinate, ShotResult> before = board.getShots();
        rejects(UnsupportedOperationException.class, () -> before.put(c(4, 4), ShotResult.HIT));
        for (Coordinate invalid : Arrays.asList(c(-1, 0), c(0, -1), c(10, 0), c(0, 10))) {
            rejects(IllegalArgumentException.class, () -> board.fireAt(invalid));
        }
        check(board.getShots().isEmpty(), "Invalid shots change nothing");
        check(board.fireAt(c(5, 5)) == ShotResult.MISS, "Miss");
        check(before.isEmpty(), "Shot snapshot does not change");
        check(!board.canPlaceShip(ship(4, 0, 4, 1)), "Setup closes on first shot");
        rejects(IllegalArgumentException.class, () -> board.placeShip(ship(4, 0, 4, 1)));
        check(board.fireAt(c(5, 5)) == ShotResult.ALREADY_SHOT, "Duplicate miss");
        check(board.fireAt(c(0, 0)) == ShotResult.HIT, "First hit unaffected by external mutations");
        check(board.fireAt(c(0, 0)) == ShotResult.ALREADY_SHOT, "Duplicate hit");
        check(board.fireAt(c(0, 1)) == ShotResult.SUNK, "Final hit sinks ship");
        check(board.getSunkShipCount() == 1 && !board.allShipsSunk(), "Other ship remains");
        check(board.fireAt(c(0, 1)) == ShotResult.ALREADY_SHOT, "Duplicate sunk cell");
        check(board.fireAt(c(9, 8)) == ShotResult.HIT, "Second ship hit");
        check(board.fireAt(c(9, 9)) == ShotResult.SUNK, "Second ship sunk");
        check(board.allShipsSunk() && board.getSunkShipCount() == 2, "Victory");
        check(board.getShots().size() == 5, "Only unique valid shots counted");
        check(board.getShots().get(c(0, 1)) == ShotResult.SUNK, "Original shot outcome preserved");

        // The fleet report is what lets the interface list the ships without ever
        // being told where they are. It must name the type and say whether it has
        // gone down, and nothing else.
        Board reported = new Board();
        check(reported.getFleetStatus().isEmpty(), "An empty board reports no ships");
        reported.placeShip(new Ship(ShipType.CRUISER,
                Arrays.asList(c(0, 0), c(0, 1), c(0, 2))));
        reported.placeShip(ship(5, 5, 5, 6));
        List<ShipStatus> fleet = reported.getFleetStatus();
        check(fleet.size() == 2, "Every placed ship is reported");
        check(fleet.get(0).getType() == ShipType.CRUISER, "Ships are reported in placement order");
        check(fleet.get(1).getType() == ShipType.DESTROYER, "The second ship keeps its type");
        check(!fleet.get(0).isSunk() && !fleet.get(1).isSunk(), "Nothing is sunk before a shot");
        check(fleet.get(0).getLength() == ShipType.CRUISER.getLength(),
                "A report carries the length the panel draws");

        reported.fireAt(c(5, 5));
        check(!reported.getFleetStatus().get(1).isSunk(), "A damaged ship is not yet sunk");
        reported.fireAt(c(5, 6));
        check(reported.getFleetStatus().get(1).isSunk(), "A finished ship is reported sunk");
        check(!reported.getFleetStatus().get(0).isSunk(), "Sinking one ship leaves the other");

        // The report is a snapshot. Holding on to an old one must not show the
        // player a ship coming back to life, and the list itself cannot be edited.
        List<ShipStatus> snapshot = reported.getFleetStatus();
        reported.fireAt(c(0, 0));
        reported.fireAt(c(0, 1));
        reported.fireAt(c(0, 2));
        check(!snapshot.get(0).isSunk(), "An old report does not change underneath the caller");
        check(reported.getFleetStatus().get(0).isSunk(), "A fresh report sees the new sinking");
        rejects(UnsupportedOperationException.class, () -> snapshot.add(null));

        Board small = new Board(2);
        small.placeShip(ship(0, 0, 1, 0));
        check(small.getSize() == 2 && small.getShipCount() == 1, "Custom size");
        System.out.println("PASS: " + checks + " Board self-checks");
    }

    private static Coordinate c(int row, int col) { return new Coordinate(row, col); }
    private static Ship ship(int r1, int c1, int r2, int c2) {
        return new Ship(ShipType.DESTROYER, Arrays.asList(c(r1, c1), c(r2, c2)));
    }
    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
        checks++;
    }
    private static void rejects(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable error) {
            if (!type.isInstance(error)) throw new AssertionError("Unexpected exception", error);
            checks++;
            return;
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }
}
