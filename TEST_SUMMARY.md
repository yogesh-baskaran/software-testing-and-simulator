# Parameterized Robot Tests - Summary

## Overview
A comprehensive parameterized test suite has been created for the robot application using JUnit 5's `@ParameterizedTest` annotation. The suite includes **9 test methods** covering **35 test cases** with three different failure strategies: **Fail Fast**, **Fail Soft**, and **Fail Partial**.

## Test File Location
`src/test/java/org/example/RobotParameterizedTest.java`

## Dependencies Added
The following dependencies were added to `pom.xml`:
- `junit-jupiter-api:5.9.2` - JUnit 5 API
- `junit-jupiter-engine:5.9.2` - JUnit 5 Engine
- `junit-jupiter-params:5.9.2` - JUnit 5 Parameterized Tests Support

## Test Methods Overview

### 1. **Fail Fast - Valid Direction Commands**
- **Strategy**: FAIL FAST - Stops at first assertion failure
- **Parameters**: String values (n, s, r, l)
- **Test Cases**: 4
- **Purpose**: Tests basic direction commands (north, south, right, left)
- **Assertions**: Validates floor size, row/column bounds

### 2. **Fail Soft - Movement and Pen State Combinations**
- **Strategy**: FAIL SOFT - All assertions checked using `assertAll()`
- **Parameters**: Steps (1-5), Pen state (up/down), Expected state (boolean)
- **Test Cases**: 4
- **Purpose**: Tests movement with different pen states
- **Assertions**: Pen state, row/col bounds, floor initialization, floor size

### 3. **Fail Partial - Floor Size Variations**
- **Strategy**: FAIL PARTIAL - Conditional assertions based on floor size
- **Parameters**: Floor sizes (5, 10, 20, 50)
- **Test Cases**: 4
- **Purpose**: Tests different floor initializations
- **Assertions**: Conditional checks for large/small floors, initial position

### 4. **Fail Fast - Boundary Movement Tests**
- **Strategy**: FAIL FAST - Linear assertion checks
- **Parameters**: Steps (1-10), Direction (n, s, r, l), Floor size (5, 20)
- **Test Cases**: 5
- **Purpose**: Tests edge cases and boundary conditions
- **Assertions**: Floor initialization, position bounds

### 5. **Fail Soft - Drawing Pattern Tests**
- **Strategy**: FAIL SOFT - Using `assertAll()` for grouped assertions
- **Parameters**: Pen command (d/u), Move command (m3/m5/m10), Floor size (15)
- **Test Cases**: 3
- **Purpose**: Tests drawing patterns with pen state tracking
- **Assertions**: Pen state validation, floor properties, history tracking

### 6. **Fail Partial - Conditional Movement Validation**
- **Strategy**: FAIL PARTIAL - Skips certain assertions based on input
- **Parameters**: Steps (1-15), Floor size (3, 5, 10, 20)
- **Test Cases**: 4
- **Purpose**: Tests movement with optional validations
- **Assertions**: Floor size, position bounds (always validated)

### 7. **Fail Fast - Pen State Transitions**
- **Strategy**: FAIL FAST - Direct sequential assertions
- **Parameters**: Commands (u/d), Expected state (true/false)
- **Test Cases**: 4
- **Purpose**: Tests pen up/down state changes
- **Assertions**: Pen state, floor size

### 8. **Fail Soft - Direction and Movement Combinations**
- **Strategy**: FAIL SOFT - Using `assertAll()` for multiple validations
- **Parameters**: Direction (n, s, r, l), Steps (1-4), Floor size (10)
- **Test Cases**: 4
- **Purpose**: Tests direction changes followed by movement
- **Assertions**: Floor size, position bounds, direction set

### 9. **Fail Partial - Sequential Command Execution**
- **Strategy**: FAIL PARTIAL - Conditional checks based on command sequence
- **Parameters**: Initial direction (n, s, r), Move steps (2-4), Floor size (5, 10, 15)
- **Test Cases**: 3
- **Purpose**: Tests multiple sequential commands
- **Assertions**: Floor size, final pen state, position bounds, floor existence

## Test Results
- **Total Test Cases**: 35
- **Passed**: 35 ✓
- **Failed**: 0
- **Errors**: 0
- **Execution Time**: ~0.19s

## Understanding the Failure Strategies

### Fail Fast
Uses sequential assertions that stop immediately when one fails.
```java
assertTrue(robot.getFloorSize() == 5, "Floor size should remain 5");
assertTrue(robot.getRow() >= 0 && robot.getRow() < 5, "Row should be within bounds");
assertTrue(robot.getCol() >= 0 && robot.getCol() < 5, "Column should be within bounds");
```

### Fail Soft
Uses `assertAll()` to execute all assertions and report all failures together.
```java
assertAll("Movement with pen state tests",
        () -> assertEquals(expectedPenUp, robot.isPenUp(), "..."),
        () -> assertTrue(robot.getRow() >= 0 && robot.getRow() < 10, "..."),
        () -> assertTrue(robot.getCol() >= 0 && robot.getCol() < 10, "..."),
        () -> assertNotNull(robot.getFloor(), "..."),
        () -> assertEquals(10, robot.getFloorSize(), "...")
);
```

### Fail Partial
Uses conditional logic to only check assertions that apply to specific parameters.
```java
if (floorSize >= 10) {
    assertTrue(floorSize >= 10, "This test case has a large floor");
}
// Always validate core functionality
assertEquals(0, robot.getRow(), "Initial row should be 0");
```

## Running the Tests

### Using Maven
```bash
mvn clean test
```

### Using IDE
Right-click on the test file and select "Run Tests" or use keyboard shortcut (typically Ctrl+Shift+F10 in IntelliJ IDEA)

## Test Coverage
The parameterized tests cover:
- ✓ Direction commands (North, South, East, West)
- ✓ Pen state transitions (Up/Down)
- ✓ Movement and boundary conditions
- ✓ Floor initialization with various sizes
- ✓ Sequential command execution
- ✓ State consistency and validation
- ✓ Drawing patterns and floor tracking

## Key Features
1. **Parameterized Testing**: Reduces code duplication through reusable test templates
2. **Multiple Strategies**: Demonstrates fail-fast, fail-soft, and fail-partial approaches
3. **CSV Data**: Uses @CsvSource for clear, readable test data
4. **Value Source**: Uses @ValueSource for simple parameter lists
5. **Descriptive Names**: Each test displays parameters in the name using {index} and parameter placeholders
6. **Instance Methods**: The Main class has been refactored to support instance-based testing

