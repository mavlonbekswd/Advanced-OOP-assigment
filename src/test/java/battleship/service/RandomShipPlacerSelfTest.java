package battleship.service;

import battleship.model.Board;
import battleship.model.Coordinate;
import battleship.model.ShotResult;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Dependency-free checks for random fleet generation; this harness is not JUnit.
 *
 * <p>The placer is inspected only through the public board API. Occupied cells are
 * discovered by firing at every cell, and ships are recovered as eight-way connected
 * groups of hit cells. That recovery is only correct when ships never touch, so the
 * expected group count and sizes simultaneously prove fleet composition, no overlap
 * and the separation rule.
 */
public final class RandomShipPlacerSelfTest {
    private static int checks;

    public static void main(String[] args) {
        Board board = new RandomShipPlacer().createBoard();
        check(board.getSize() == Board.DEFAULT_SIZE, "Standard board size");
        check(board.getShipCount() == 6, "Six ships placed");

        Set<Coordinate> occupied = occupiedCells(board);
        check(occupied.size() == 16, "Sixteen occupied cells");
        check(board.getSunkShipCount() == 6 && board.allShipsSunk(), "Every ship sinks");

        List<Set<Coordinate>> groups = connectedGroups(occupied);
        check(groups.size() == 6, "Six separated groups");
        check(groupSizes(groups).equals(Arrays.asList(2, 2, 2, 3, 3, 4)), "Exact fleet lengths");
        for (Set<Coordinate> group : groups) {
            check(isStraightAndContiguous(group), "Group is a straight unbroken line");
        }

        // A board too small to separate six ships must fail loudly, never loop forever.
        rejects(IllegalStateException.class, () -> new RandomShipPlacer().createBoard(4));
        // Too small even to hold the Battleship: still a generation failure, not a crash.
        rejects(IllegalStateException.class, () -> new RandomShipPlacer().createBoard(3));
        rejects(IllegalArgumentException.class, () -> new RandomShipPlacer().createBoard(0));
        Board sized = new RandomShipPlacer().createBoard(Board.DEFAULT_SIZE);
        check(sized.getSize() == Board.DEFAULT_SIZE && sized.getShipCount() == 6, "Sized board fleet");
        rejects(NullPointerException.class, () -> new RandomShipPlacer(null));

        // The injected source of randomness makes a fleet reproducible from its seed.
        Set<Coordinate> first = occupiedCells(new RandomShipPlacer(new Random(2026)).createBoard());
        Set<Coordinate> again = occupiedCells(new RandomShipPlacer(new Random(2026)).createBoard());
        check(first.equals(again), "Same seed repeats the fleet");

        // Stress run: every generated fleet must obey every rule, and over many
        // fleets both orientations must occur.
        boolean sawHorizontal = false;
        boolean sawVertical = false;
        int valid = 0;
        for (int seed = 0; seed < 1000; seed++) {
            Board generated = new RandomShipPlacer(new Random(seed)).createBoard();
            List<Set<Coordinate>> fleet = connectedGroups(occupiedCells(generated));
            if (fleet.size() != 6 || !groupSizes(fleet).equals(Arrays.asList(2, 2, 2, 3, 3, 4))) {
                continue;
            }
            boolean allStraight = true;
            for (Set<Coordinate> group : fleet) {
                allStraight &= isStraightAndContiguous(group);
                sawHorizontal |= isHorizontal(group);
                sawVertical |= !isHorizontal(group);
            }
            if (allStraight) {
                valid++;
            }
        }
        check(valid == 1000, "One thousand generated fleets are all valid");
        check(sawHorizontal && sawVertical, "Both orientations occur");
        check(!first.equals(occupiedCells(new RandomShipPlacer(new Random(7)).createBoard())),
                "A different seed gives a different fleet");

        System.out.println("PASS: " + checks + " RandomShipPlacer self-checks");
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

    /** Fires at every cell and returns the cells that held part of a ship. */
    private static Set<Coordinate> occupiedCells(Board board) {
        Set<Coordinate> hits = new HashSet<>();
        for (int row = 0; row < board.getSize(); row++) {
            for (int column = 0; column < board.getSize(); column++) {
                Coordinate cell = new Coordinate(row, column);
                ShotResult result = board.fireAt(cell);
                if (result == ShotResult.HIT || result == ShotResult.SUNK) {
                    hits.add(cell);
                }
            }
        }
        return hits;
    }

    /** Groups cells that touch in any of the eight directions. */
    private static List<Set<Coordinate>> connectedGroups(Set<Coordinate> cells) {
        Set<Coordinate> remaining = new HashSet<>(cells);
        List<Set<Coordinate>> groups = new ArrayList<>();
        while (!remaining.isEmpty()) {
            Coordinate seed = remaining.iterator().next();
            remaining.remove(seed);
            Set<Coordinate> group = new HashSet<>();
            Deque<Coordinate> pending = new ArrayDeque<>();
            pending.push(seed);
            while (!pending.isEmpty()) {
                Coordinate current = pending.pop();
                group.add(current);
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        Coordinate neighbour =
                                new Coordinate(current.getRow() + dr, current.getColumn() + dc);
                        if (remaining.remove(neighbour)) {
                            pending.push(neighbour);
                        }
                    }
                }
            }
            groups.add(group);
        }
        return groups;
    }

    /** @return whether every cell of the group shares one row */
    private static boolean isHorizontal(Set<Coordinate> group) {
        return group.stream().map(Coordinate::getRow).distinct().count() == 1;
    }

    private static List<Integer> groupSizes(List<Set<Coordinate>> groups) {
        List<Integer> sizes = new ArrayList<>();
        for (Set<Coordinate> group : groups) {
            sizes.add(group.size());
        }
        Collections.sort(sizes);
        return sizes;
    }

    /** @return whether the cells form one unbroken horizontal or vertical line */
    private static boolean isStraightAndContiguous(Set<Coordinate> group) {
        List<Coordinate> cells = new ArrayList<>(group);
        boolean horizontal = cells.stream().allMatch(c -> c.getRow() == cells.get(0).getRow());
        boolean vertical = cells.stream().allMatch(c -> c.getColumn() == cells.get(0).getColumn());
        if (!horizontal && !vertical) {
            return false;
        }
        List<Integer> line = new ArrayList<>();
        for (Coordinate cell : cells) {
            line.add(horizontal ? cell.getColumn() : cell.getRow());
        }
        Collections.sort(line);
        for (int i = 1; i < line.size(); i++) {
            if (line.get(i) - line.get(i - 1) != 1) {
                return false;
            }
        }
        return true;
    }

    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
        checks++;
    }
}
