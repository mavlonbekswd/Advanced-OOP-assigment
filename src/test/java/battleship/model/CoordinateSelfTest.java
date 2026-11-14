package battleship.model;

/**
 * Temporary self-checks that run without third-party test libraries.
 * These are not JUnit tests. Later phases will add automated unit tests.
 */
public final class CoordinateSelfTest {
    private static int checks = 0;

    private CoordinateSelfTest() {
    }

    /**
     * Checks normal, boundary and invalid coordinate behaviour.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        Coordinate topLeft = new Coordinate(0, 0);
        Coordinate bottomRight = new Coordinate(9, 9);
        Coordinate outside = new Coordinate(10, 0);
        Coordinate negative = new Coordinate(-1, 3);
        Coordinate sameTopLeft = new Coordinate(0, 0);

        check(topLeft.isWithin(10), "Top-left should be on board");
        check(bottomRight.isWithin(10), "Bottom-right should be on board");
        check(!outside.isWithin(10), "Row 10 should be outside");
        check(!negative.isWithin(10), "Negative row should be outside");
        check(topLeft.getRow() == 0 && topLeft.getColumn() == 0,
                "Getters should return constructor values");
        check(topLeft.equals(sameTopLeft), "Equal locations should compare equal");
        check(topLeft.hashCode() == sameTopLeft.hashCode(),
                "Equal locations must have equal hash codes");
        check(!topLeft.equals(bottomRight), "Different locations should not be equal");
        check("(0, 0)".equals(topLeft.toString()), "String should be readable");

        boolean rejectedInvalidSize = false;
        try {
            topLeft.isWithin(0);
        } catch (IllegalArgumentException expected) {
            rejectedInvalidSize = true;
        }
        check(rejectedInvalidSize, "Invalid board sizes should be rejected");

        System.out.println("PASS: " + checks + " Coordinate self-checks");
    }

    private static void check(boolean result, String message) {
        if (!result) {
            throw new AssertionError(message);
        }
        checks++;
    }
}
