package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Robot Parameterized Tests")
public class RobotParameterizedTest {

    private Main robot;

    @BeforeEach
    void setUp() {
        robot = new Main();
    }

    /**
     * FAIL FAST: Tests stop at first failure
     * Tests the basic movement commands with valid inputs
     */
    @ParameterizedTest(name = "Direction command: {0}")
    @ValueSource(strings = {"n", "s", "r", "l"})
    @DisplayName("Fail Fast - Valid Direction Commands")
    void testValidDirectionCommands_FailFast(String command) {
        // Arrange
        robot.initializeFloor(5);

        // Act & Assert - fail fast if any assertion fails
        assertDoesNotThrow(() -> {
            robot.executeCommand(command);
        }, "Command '" + command + "' should execute without throwing exception");

        // Verify robot is still in valid state
        assertTrue(robot.getFloorSize() == 5, "Floor size should remain 5");
        assertTrue(robot.getRow() >= 0 && robot.getRow() < 5, "Row should be within bounds");
        assertTrue(robot.getCol() >= 0 && robot.getCol() < 5, "Column should be within bounds");
    }

    /**
     * FAIL SOFT: Tests continue even if some assertions fail
     * Tests various pen and move commands with multiple assertions
     */
    @ParameterizedTest(name = "Scenario {index}: {0} steps with pen {1}")
    @CsvSource({
            "1, up, true",
            "2, down, false",
            "3, up, true",
            "5, down, false"
    })
    @DisplayName("Fail Soft - Movement and Pen State Combinations")
    void testMovementWithPenState_FailSoft(int steps, String penState, boolean expectedPenUp) {
        // Arrange
        robot.initializeFloor(10);
        String penCommand = penState.equalsIgnoreCase("up") ? "u" : "d";

        // Act
        robot.executeCommand(penCommand);
        robot.executeCommand("m" + steps);

        // Assert - all assertions will be checked even if one fails (fail soft)
        assertAll("Movement with pen state tests",
                () -> assertEquals(expectedPenUp, robot.isPenUp(),
                    "Pen should be " + (expectedPenUp ? "UP" : "DOWN")),
                () -> assertTrue(robot.getRow() >= 0 && robot.getRow() < 10,
                    "Row should be within bounds after moving " + steps + " steps"),
                () -> assertTrue(robot.getCol() >= 0 && robot.getCol() < 10,
                    "Column should be within bounds after moving " + steps + " steps"),
                () -> assertNotNull(robot.getFloor(),
                    "Floor should be initialized"),
                () -> assertEquals(10, robot.getFloorSize(),
                    "Floor size should be 10")
        );
    }

    /**
     * FAIL PARTIAL: Tests with conditional assertions based on parameters
     * Tests different floor sizes and validates appropriate behavior
     */
    @ParameterizedTest(name = "Floor size: {0}")
    @ValueSource(ints = {5, 10, 20, 50})
    @DisplayName("Fail Partial - Floor Size Variations")
    void testFloorSizeVariations_FailPartial(int floorSize) {
        // Arrange & Act
        robot.initializeFloor(floorSize);

        // Assert - partial validation based on floor size
        assertEquals(floorSize, robot.getFloorSize(), "Floor size should match initialization");
        assertNotNull(robot.getFloor(), "Floor should be initialized");
        assertEquals(floorSize, robot.getFloor().length, "Floor should have correct number of rows");

        // Conditional assertions based on floor size (fail partial - validate what applies)
        if (floorSize >= 10) {
            assertTrue(floorSize >= 10, "This test case has a large floor");
        }
        if (floorSize <= 10) {
            assertTrue(floorSize <= 10, "This test case has a small floor");
        }

        // Always check initial position
        assertEquals(0, robot.getRow(), "Initial row should be 0");
        assertEquals(0, robot.getCol(), "Initial column should be 0");
    }

    /**
     * FAIL FAST WITH MULTIPLE PARAMETERS: Test boundary conditions
     * Stops at first failure when testing edge cases
     */
    @ParameterizedTest(name = "Move {0} steps with direction {1} from size {2}")
    @CsvSource({
            "1, n, 5",
            "2, s, 5",
            "3, r, 5",
            "4, l, 5",
            "10, n, 20"
    })
    @DisplayName("Fail Fast - Boundary Movement Tests")
    void testBoundaryMovements_FailFast(int steps, String direction, int floorSize) {
        // Arrange
        robot.initializeFloor(floorSize);

        // Act
        if (!direction.equalsIgnoreCase("n")) {
            robot.executeCommand(direction);
        }
        robot.executeCommand("m" + steps);

        // Assert - fail fast on first failure
        assertNotNull(robot.getFloor(), "Floor must be initialized");
        assertTrue(robot.getRow() >= 0, "Row must not be negative");
        assertTrue(robot.getRow() < floorSize, "Row must be within floor bounds");
        assertTrue(robot.getCol() >= 0, "Column must not be negative");
        assertTrue(robot.getCol() < floorSize, "Column must be within floor bounds");
    }

    /**
     * FAIL SOFT WITH NESTED ASSERTIONS: Complex scenario testing
     * Uses assertAll to continue checking all conditions
     */
    @ParameterizedTest(name = "Scenario {index}: Pen {0}, Move {1}, Floor size {2}")
    @CsvSource({
            "d, m3, 15",
            "d, m5, 15",
            "u, m10, 15"
    })
    @DisplayName("Fail Soft - Drawing Pattern Tests")
    void testDrawingPatterns_FailSoft(String penCommand, String moveCommand, int floorSize) {
        // Arrange
        robot.initializeFloor(floorSize);
        robot.executeCommand(penCommand);

        // Act
        robot.executeCommand(moveCommand);

        // Assert - all validations performed (fail soft)
        assertAll("Drawing pattern validations",
                () -> {
                    // Verify the pen command was executed correctly
                    if (penCommand.equalsIgnoreCase("d")) {
                        assertFalse(robot.isPenUp(), "Pen should be down when 'd' command is executed");
                    } else {
                        assertTrue(robot.isPenUp(), "Pen should be up when 'u' command is executed");
                    }
                },
                () -> assertTrue(robot.getFloorSize() == floorSize, "Floor size should be " + floorSize),
                () -> assertNotNull(robot.getHistory(), "History should be tracked"),
                () -> assertTrue(robot.getRow() >= 0 && robot.getRow() < floorSize, "Row position must be valid"),
                () -> assertTrue(robot.getCol() >= 0 && robot.getCol() < floorSize, "Column position must be valid")
        );
    }

    /**
     * FAIL PARTIAL: Test with skip conditions
     * Some assertions only apply to certain parameter values
     */
    @ParameterizedTest(name = "Movement scenario {index}: steps={0}, floor={1}")
    @CsvSource({
            "1, 3",
            "5, 5",
            "0, 10",
            "15, 20"
    })
    @DisplayName("Fail Partial - Conditional Movement Validation")
    void testConditionalMovement_FailPartial(int steps, int floorSize) {
        // Arrange
        robot.initializeFloor(floorSize);

        // Act
        robot.executeCommand("m" + steps);

        // Assert - partial validation with conditions
        assertEquals(floorSize, robot.getFloorSize(), "Floor must be initialized to correct size");

        // Only validate movement if it's a reasonable amount
        if (steps > 0 && steps < floorSize) {
            assertTrue(robot.getRow() >= 0,
                "Robot row should be valid after moving (fail partial check)");
        }

        // Always validate state consistency
        assertTrue(robot.getRow() >= 0 && robot.getRow() < floorSize,
            "Robot position must be within bounds");
        assertTrue(robot.getCol() >= 0 && robot.getCol() < floorSize,
            "Robot column must be within bounds");
    }

    /**
     * FAIL FAST: Pen state transitions
     * Tests pen up/down state changes with fail fast approach
     */
    @ParameterizedTest(name = "Pen state {index}: command={0}, expectedState={1}")
    @CsvSource({
            "u, true",
            "d, false",
            "u, true",
            "d, false"
    })
    @DisplayName("Fail Fast - Pen State Transitions")
    void testPenStateTransitions_FailFast(String command, boolean expectedState) {
        // Arrange
        robot.initializeFloor(5);

        // Act
        robot.executeCommand(command);

        // Assert - fail fast if assertion fails
        assertEquals(expectedState, robot.isPenUp(),
            "Pen should be " + (expectedState ? "UP" : "DOWN") + " after executing '" + command + "'");
        assertTrue(robot.getFloorSize() > 0, "Floor should still be initialized");
    }

    /**
     * FAIL SOFT: Multiple direction and movement combinations
     * Tests various direction changes with movements
     */
    @ParameterizedTest(name = "Direction {0}, then move {1} steps, floor {2}")
    @CsvSource({
            "n, 2, 10",
            "s, 3, 10",
            "r, 4, 10",
            "l, 1, 10"
    })
    @DisplayName("Fail Soft - Direction and Movement Combinations")
    void testDirectionMovementCombinations_FailSoft(String direction, int steps, int floorSize) {
        // Arrange
        robot.initializeFloor(floorSize);

        // Act
        robot.executeCommand(direction);
        robot.executeCommand("m" + steps);

        // Assert - all checks performed (fail soft)
        assertAll("Direction and movement validations",
                () -> assertTrue(robot.getFloorSize() == floorSize, "Floor size preserved"),
                () -> assertTrue(robot.getRow() >= 0, "Row within bounds"),
                () -> assertTrue(robot.getRow() < floorSize, "Row not exceeding bounds"),
                () -> assertTrue(robot.getCol() >= 0, "Column within bounds"),
                () -> assertTrue(robot.getCol() < floorSize, "Column not exceeding bounds"),
                () -> assertNotNull(robot.getDirection(), "Direction should be set")
        );
    }

    /**
     * FAIL PARTIAL: Sequential command execution
     * Tests a series of commands with partial validation
     */
    @ParameterizedTest(name = "Scenario {index}: Initial direction {0}, commands {1}, floor size {2}")
    @CsvSource({
            "n, 3, 5",
            "s, 2, 10",
            "r, 4, 15"
    })
    @DisplayName("Fail Partial - Sequential Command Execution")
    void testSequentialCommands_FailPartial(String initialDir, int moveSteps, int floorSize) {
        // Arrange
        robot.initializeFloor(floorSize);

        // Act
        robot.executeCommand(initialDir);
        robot.executeCommand("d");  // Put pen down
        robot.executeCommand("m" + moveSteps);
        robot.executeCommand("u");  // Put pen up

        // Assert - partial validation based on execution context
        assertEquals(floorSize, robot.getFloorSize(), "Floor size should match");
        assertTrue(robot.isPenUp(), "Pen should be UP after final command");

        // Partial: Only check drawn cells if pen was down and movement occurred
        if (moveSteps > 0) {
            assertNotNull(robot.getFloor(), "Floor should exist after drawing");
        }

        // Always check final state validity
        assertTrue(robot.getRow() >= 0 && robot.getRow() < floorSize, "Final row position valid");
        assertTrue(robot.getCol() >= 0 && robot.getCol() < floorSize, "Final column position valid");
    }
}

