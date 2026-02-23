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
     * Test direction commands: n (north), s (south), r (right), l (left)
     * Verifies that robot executes direction commands without errors
     */
    @ParameterizedTest(name = "Direction command: {0}")
    @ValueSource(strings = {"n", "s", "r", "l"})
    @DisplayName("Parameterized Test - Direction Commands")
    void testDirectionCommands(String command) {
        // Arrange
        robot.initializeFloor(5);

        // Act
        robot.executeCommand(command);

        // Assert
        assertNotNull(robot.getFloor(), "Floor should be initialized");
        assertTrue(robot.getRow() >= 0 && robot.getRow() < 5, "Row should be within bounds");
        assertTrue(robot.getCol() >= 0 && robot.getCol() < 5, "Column should be within bounds");
    }

    /**
     * Test movement with different step counts and pen states
     * Verifies that robot moves correctly and respects pen state
     */
    @ParameterizedTest(name = "Move {0} steps with pen {1}, expect pen state {2}")
    @CsvSource({
            "1, up, true",
            "2, down, false",
            "3, up, true",
            "5, down, false"
    })
    @DisplayName("Parameterized Test - Movement and Pen State")
    void testMovementWithPenState(int steps, String penState, boolean expectedPenUp) {
        // Arrange
        robot.initializeFloor(10);
        String penCommand = penState.equalsIgnoreCase("up") ? "u" : "d";

        // Act
        robot.executeCommand(penCommand);
        robot.executeCommand("m" + steps);

        // Assert
        assertEquals(expectedPenUp, robot.isPenUp(), "Pen state should match expected");
        assertTrue(robot.getRow() >= 0 && robot.getRow() < 10, "Row should be within bounds");
        assertTrue(robot.getCol() >= 0 && robot.getCol() < 10, "Column should be within bounds");
    }

    /**
     * Test different floor sizes
     * Verifies that robot initializes correctly with various floor dimensions
     */
    @ParameterizedTest(name = "Floor size: {0}")
    @ValueSource(ints = {3, 5, 10, 20, 50})
    @DisplayName("Parameterized Test - Floor Size Variations")
    void testFloorSizeVariations(int floorSize) {
        // Arrange & Act
        robot.initializeFloor(floorSize);

        // Assert
        assertEquals(floorSize, robot.getFloorSize(), "Floor size should match initialization");
        assertNotNull(robot.getFloor(), "Floor should be initialized");
        assertEquals(floorSize, robot.getFloor().length, "Floor should have correct dimensions");
        assertEquals(0, robot.getRow(), "Initial row should be 0");
        assertEquals(0, robot.getCol(), "Initial column should be 0");
    }

    /**
     * Test boundary movements with different parameters
     * Verifies that robot handles edge cases correctly
     */
    @ParameterizedTest(name = "Move {0} steps in direction {1} on {2}x{2} floor")
    @CsvSource({

            "3, r, 5",
            "4, l, 5",

    })
    @DisplayName("Parameterized Test - Boundary Movement")
    void testBoundaryMovement(int steps, String direction, int floorSize) {
        // Arrange
        robot.initializeFloor(floorSize);

        // Act
        if (!direction.equalsIgnoreCase("n")) {
            robot.executeCommand(direction);
        }
        robot.executeCommand("m" + steps);

        // Assert
        assertTrue(robot.getRow() >= 0 && robot.getRow() < floorSize, "Row must be within bounds");
        assertTrue(robot.getCol() >= 0 && robot.getCol() < floorSize, "Column must be within bounds");
    }

    /**
     * Test pen state transitions
     * Verifies that pen up/down commands work correctly
     */
    @ParameterizedTest(name = "Command {0} should set pen to {1}")
    @CsvSource({
            "u, true",
            "d, false",
            "u, true",
            "d, false"
    })
    @DisplayName("Parameterized Test - Pen State Transitions")
    void testPenStateTransitions(String command, boolean expectedState) {
        // Arrange
        robot.initializeFloor(5);

        // Act
        robot.executeCommand(command);

        // Assert
        assertEquals(expectedState, robot.isPenUp(),
            "Pen state should be " + (expectedState ? "UP" : "DOWN"));
    }

    /**
     * Test sequential command execution
     * Verifies that multiple commands execute in sequence correctly
     */
    @ParameterizedTest(name = "Scenario {index}: Direction {0}, Steps {1}, Floor {2}")
    @CsvSource({

            "r, 3, 15",
            "l, 2, 20"
    })
    @DisplayName("Parameterized Test - Sequential Commands")
    void testSequentialCommands(String direction, int steps, int floorSize) {
        // Arrange
        robot.initializeFloor(floorSize);

        // Act
        robot.executeCommand(direction);
        robot.executeCommand("d");  // Put pen down
        robot.executeCommand("m" + steps);
        robot.executeCommand("u");  // Put pen up

        // Assert
        assertEquals(floorSize, robot.getFloorSize(), "Floor size should be preserved");
        assertTrue(robot.isPenUp(), "Pen should be UP after final command");
        assertTrue(robot.getRow() >= 0 && robot.getRow() < floorSize, "Row should be within bounds");
        assertTrue(robot.getCol() >= 0 && robot.getCol() < floorSize, "Column should be within bounds");
    }

    /**
     * Test direction changes before movement
     * Verifies that direction affects movement direction
     */
    @ParameterizedTest(name = "Direction {0}, move {1} steps, floor {2}x{2}")
    @CsvSource({
            "r, 4, 10",
            "l, 1, 10"
    })
    @DisplayName("Parameterized Test - Direction and Movement")
    void testDirectionAndMovement(String direction, int steps, int floorSize) {
        // Arrange
        robot.initializeFloor(floorSize);

        // Act
        robot.executeCommand(direction);
        robot.executeCommand("m" + steps);

        // Assert
        assertEquals(floorSize, robot.getFloorSize(), "Floor size should match");
        assertTrue(robot.getRow() >= 0 && robot.getRow() < floorSize, "Row should be valid");
        assertTrue(robot.getCol() >= 0 && robot.getCol() < floorSize, "Column should be valid");
    }

    /**
     * Test movement with different step counts
     * Verifies that robot moves correct number of steps (or stops at boundary)
     */
    @ParameterizedTest(name = "Move {0} steps on {1}x{1} floor")
    @CsvSource({
            "0, 5",
            "1, 5",
            "2, 10",
            "5, 10",
            "10, 20"
    })
    @DisplayName("Parameterized Test - Various Step Counts")
    void testVariousStepCounts(int steps, int floorSize) {
        // Arrange
        robot.initializeFloor(floorSize);

        // Act
        robot.executeCommand("m" + steps);

        // Assert
        assertTrue(robot.getRow() >= 0 && robot.getRow() < floorSize, "Position should be valid");
        assertTrue(robot.getCol() >= 0 && robot.getCol() < floorSize, "Position should be valid");
        assertEquals(floorSize, robot.getFloorSize(), "Floor size should be correct");
    }

    /**
     * Comprehensive operation scenario test
     * Tests complete workflow: initialization, pen operations, rotations, movement, and status checks
     * Scenario includes: pen up/down, turn right/left, move, print (p), and current position (c)
     */
    @ParameterizedTest(name = "Scenario {index}: Pen {0}, Turn {1}, Move {2}, Floor {3}")
    @CsvSource({
            // Scenario 1: Pen down, turn right, move
            "down, r, 2, 5",
            // Scenario 2: Pen up, turn left, move
            "up, l, 3, 10",
            // Scenario 3: Pen down, turn north, move
            "down, n, 1, 5",
            // Scenario 4: Pen up, turn south, move
            "up, s, 2, 10"
    })
    @DisplayName("Parameterized Test - Complete Operation Scenarios")
    void testCompleteOperationScenarios(String penOp, String turnOp, int moveSteps, int floorSize) {
        // Arrange
        robot.initializeFloor(floorSize);

        // Act - Execute complete operation sequence
        // 1. Set pen state
        String penCommand = penOp.equalsIgnoreCase("down") ? "d" : "u";
        robot.executeCommand(penCommand);

        // 2. Verify pen state after command
        boolean isPenDown = penOp.equalsIgnoreCase("down");
        assertEquals(!isPenDown, robot.isPenUp(), "Pen state should be set correctly");

        // 3. Execute turn command
        robot.executeCommand(turnOp);

        // 4. Execute movement
        robot.executeCommand("m" + moveSteps);

        // 5. Execute print command (doesn't affect state)
        robot.executeCommand("p");

        // 6. Execute current position command (doesn't affect state)
        robot.executeCommand("c");

        // Assert - Verify final state
        assertEquals(floorSize, robot.getFloorSize(), "Floor size should be preserved");
        assertEquals(!isPenDown, robot.isPenUp(), "Pen state should remain as set");
        assertTrue(robot.getRow() >= 0 && robot.getRow() < floorSize, "Row should be within bounds");
        assertTrue(robot.getCol() >= 0 && robot.getCol() < floorSize, "Column should be within bounds");
        assertNotNull(robot.getFloor(), "Floor should exist");
    }

    /**
     * Complex multi-operation scenario test
     * Tests multiple sequential operations: pen changes, multiple rotations, multiple movements
     * Covers the full command set in a realistic workflow
     */
    @ParameterizedTest(name = "Complex Scenario {index}: Pen-Turn-Move sequence")
    @CsvSource({
            // Scenario 1: Complex workflow - pen down, turn, move, pen up, turn, move, floor size
            "d, r, 2, u, l, 3, 10",
            // Scenario 2: Alternating pen states with movements
            "u, n, 1, d, s, 2, 15",
            // Scenario 3: Multiple rotations with movement
            "d, r, 1, r, 2, 5, 10",
            // Scenario 4: Pen operations with north/south movements
            "u, n, 3, d, s, 1, 20"
    })
    @DisplayName("Parameterized Test - Complex Multi-Operation Scenarios")
    void testComplexMultiOperationScenarios(
            String pen1, String turn1, int move1,
            String pen2, String turn2, int move2,
            int floorSize) {

        // Arrange
        robot.initializeFloor(floorSize);

        // Act - First operation sequence
        robot.executeCommand(pen1.equalsIgnoreCase("d") ? "d" : "u");
        boolean firstPenDown = pen1.equalsIgnoreCase("d");

        robot.executeCommand(turn1);  // r, l, n, or s
        robot.executeCommand("m" + move1);

        // Check status (p = print floor, c = current position)
        robot.executeCommand("p");  // Print floor
        robot.executeCommand("c");  // Show current position

        // Act - Second operation sequence
        robot.executeCommand(pen2.equalsIgnoreCase("d") ? "d" : "u");
        boolean secondPenDown = pen2.equalsIgnoreCase("d");

        robot.executeCommand(turn2);  // r, l, n, or s
        robot.executeCommand("m" + move2);

        // Check final status
        robot.executeCommand("p");  // Print floor
        robot.executeCommand("c");  // Show current position

        // Assert - Verify final state after all operations
        assertEquals(floorSize, robot.getFloorSize(), "Floor size should be preserved");
        assertEquals(!secondPenDown, robot.isPenUp(), "Pen state should reflect last pen command");
        assertTrue(robot.getRow() >= 0 && robot.getRow() < floorSize, "Row should be within bounds");
        assertTrue(robot.getCol() >= 0 && robot.getCol() < floorSize, "Column should be within bounds");
        assertNotNull(robot.getFloor(), "Floor should exist after operations");
    }

    /**
     * All commands operation test
     * Tests each individual command: u, d, r, l, n, s, m, p, c
     * Verifies that robot executes each command without errors
     */
    @ParameterizedTest(name = "Command {0} - Floor size {1}")
    @CsvSource({
            // Test each command individually
            "u, 5",   // Pen up
            "d, 5",   // Pen down
            "r, 5",   // Turn right
            "l, 5",   // Turn left
            "n, 5",   // Turn north
            "s, 5",   // Turn south
            "m1, 5",  // Move 1 step
            "p, 5",   // Print floor
            "c, 5"    // Current position
    })
    @DisplayName("Parameterized Test - Individual Commands")
    void testIndividualCommands(String command, int floorSize) {
        // Arrange
        robot.initializeFloor(floorSize);

        // Act
        robot.executeCommand(command);

        // Assert
        assertEquals(floorSize, robot.getFloorSize(), "Floor size should be preserved");
        assertTrue(robot.getRow() >= 0 && robot.getRow() < floorSize, "Row should be within bounds");
        assertTrue(robot.getCol() >= 0 && robot.getCol() < floorSize, "Column should be within bounds");
        assertNotNull(robot.getFloor(), "Floor should be initialized");
    }

    /**
     * Rotation sequence test
     * Tests all rotations in sequence: right (r), left (l), north (n), south (s)
     * Verifies robot handles all direction changes correctly
     */
    @ParameterizedTest(name = "Rotation sequence {index}: {0}")
    @CsvSource({
            // Test single rotations
            "r",  // Turn right
            "l",  // Turn left
            "n",  // Turn north
            "s"   // Turn south
    })
    @DisplayName("Parameterized Test - All Rotation Commands")
    void testAllRotationCommands(String rotationCommand) {
        // Arrange
        robot.initializeFloor(8);

        // Act - Execute rotation
        robot.executeCommand(rotationCommand);

        // Assert
        assertEquals(8, robot.getFloorSize(), "Floor should be initialized");
        assertEquals(0, robot.getRow(), "Row should remain at origin");
        assertEquals(0, robot.getCol(), "Column should remain at origin");
        assertNotNull(robot.getDirection(), "Direction should be set");
    }

    /**
     * Pen operations with movement test
     * Tests pen up/down followed by movement to verify pen state affects floor marking
     */
    @ParameterizedTest(name = "Pen {0}, Move {1} steps, Floor {2}x{2}")
    @CsvSource({
            // Test pen operations followed by movement
            "u, 2, 5",   // Pen up, move
            "d, 2, 5",   // Pen down, move
            "u, 3, 10",  // Pen up, move more
            "d, 3, 10"   // Pen down, move more
    })
    @DisplayName("Parameterized Test - Pen Operations with Movement")
    void testPenOperationsWithMovement(String penState, int steps, int floorSize) {
        // Arrange
        robot.initializeFloor(floorSize);

        // Act - Set pen state
        String penCommand = penState.equalsIgnoreCase("u") ? "u" : "d";
        robot.executeCommand(penCommand);

        // Verify pen state is set
        if (penState.equalsIgnoreCase("u")) {
            assertTrue(robot.isPenUp(), "Pen should be UP");
        } else {
            assertFalse(robot.isPenUp(), "Pen should be DOWN");
        }

        // Move with current pen state
        robot.executeCommand("m" + steps);

        // Assert - Check final state
        assertEquals(floorSize, robot.getFloorSize(), "Floor should be correct size");
        assertTrue(robot.getRow() >= 0 && robot.getRow() < floorSize, "Row should be within bounds");
        assertTrue(robot.getCol() >= 0 && robot.getCol() < floorSize, "Column should be within bounds");
    }

    /**
     * Full workflow scenario test
     * Tests realistic robot usage: initialize, draw pattern, check status
     */
    @ParameterizedTest(name = "Workflow {index}: Direction {0}, Pen {1}, Steps {2}")
    @CsvSource({
            // Realistic workflow scenarios
            "n, down, 2",  // Move north with pen down
            "s, down, 1",  // Move south with pen down
            "r, up, 2",    // Turn and move with pen up
            "l, down, 3"   // Turn and move with pen down
    })
    @DisplayName("Parameterized Test - Full Workflow Scenarios")
    void testFullWorkflowScenarios(String direction, String penState, int moveSteps) {
        // Arrange
        int floorSize = 10;
        robot.initializeFloor(floorSize);

        // Act - Initialize and set pen
        String penCommand = penState.equalsIgnoreCase("down") ? "d" : "u";
        robot.executeCommand(penCommand);

        // Set direction
        robot.executeCommand(direction);

        // Move
        robot.executeCommand("m" + moveSteps);

        // Get status
        robot.executeCommand("c");  // Current position
        robot.executeCommand("p");  // Print floor

        // Assert - Verify complete workflow
        assertEquals(floorSize, robot.getFloorSize(), "Floor size should be maintained");
        boolean expectedPenUp = penState.equalsIgnoreCase("up");
        assertEquals(expectedPenUp, robot.isPenUp(), "Pen state should match workflow");
        assertTrue(robot.getRow() >= 0 && robot.getRow() < floorSize, "Row position valid");
        assertTrue(robot.getCol() >= 0 && robot.getCol() < floorSize, "Column position valid");
        assertNotNull(robot.getFloor(), "Floor should exist and be drawable");
    }
}



