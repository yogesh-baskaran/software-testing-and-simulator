package org.example;
import java.util.*;
import java.util.regex.*;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;

    // Instance variables for testability
    private int row, col, direction;
    private boolean penUp;
    private int n;
    private int[][] floor;
    private List<String> history = new ArrayList<>();

    // Static variables for backward compatibility
    private static int staticRow, staticCol, staticDirection;
    private static boolean staticPenUp;
    private static int staticN;
    private static int[][] staticFloor;
    private static List<String> staticHistory = new ArrayList<>();

    // Constructor for testing
    public Main() {
    }

    public static void main( String[] args) {
        Main robot = new Main();
        Scanner sc = new Scanner(System.in);
        System.out.println("Initialize the floor using the 'i n' command (e.g., i 5)");
        printHelp();

        while (true) {
            System.out.print("command: ");
            String command = sc.nextLine().trim();

            if(command.equalsIgnoreCase("q")) {
                System.out.println("exiting...");
                break;
            }
            if (!command.equalsIgnoreCase("h")) {
                robot.history.add(command);
            }
            robot.executeCommand(command);
        }
        sc.close();
    }

    //initialise system with size n
    private void initisalize(int size) {
        this.n = size;
        this.floor = new int[n][n];
        this.row = 0;
        this.col = 0;
        this.direction = NORTH;
        this.penUp = true;
        this.history.clear();
    }
    private void execute(String cmd) {
        if (cmd.equalsIgnoreCase("u")){
            penUp = true;
            System.out.println("pen up");

        }
        else if (cmd.equalsIgnoreCase("d")){
            penUp = false;
            System.out.println("pen down");
        }
        //move right
        else if (cmd.equalsIgnoreCase("r")){
            direction = (direction +1) %4;
            System.out.println("turned right");
        }
        //move left
        else if (cmd.equalsIgnoreCase("l")){
            direction = (direction +3) %4;
            System.out.println("turned left");
        }
        //move up
        else if (cmd.equalsIgnoreCase("n")){
            direction = (direction +0) %4;
            System.out.println("turned north");
        }
        //move down
        else if (cmd.equalsIgnoreCase("s")){
            direction = (direction +2) %4;
            System.out.println("turned south");
        }
        //move forward
        else if (cmd.startsWith("m") || cmd.startsWith("M")){
            int steps = Integer.parseInt(cmd.substring(1));
            move(steps);
        }
        // initialize command: accepts "i n" or "in"
        else if (cmd.toLowerCase().startsWith("i")){
            Pattern p = Pattern.compile("i\\s*(\\d+)", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(cmd);
            if (m.find()) {
                int newSize = Integer.parseInt(m.group(1));
                if (newSize > 0) {
                    initisalize(newSize);
                    System.out.println("floor initialized to size " + newSize);
                } else {
                    System.out.println("invalid size. must be a positive integer.");
                }
            } else {
                System.out.println("invalid initialize command. usage: i n (e.g. i 5 or i5)");
            }
        }
        else if (cmd.equalsIgnoreCase("p")){
            printfloor();
        }
        else if (cmd.equalsIgnoreCase("c")){
            printStatus();
        }
        else if (cmd.equalsIgnoreCase("h")){
            replayHistory();
        }
        else{
            System.out.println("invalid command.");
        }
    }
    private void move(int steps){
        for (int i = 0; i < steps; i++){
            int nextRow = row;
            int nextCol = col;

            switch (direction){
                case NORTH -> nextRow++;
                case EAST -> nextCol++;
                case SOUTH -> nextRow--;
                case WEST -> nextCol--;
            }
            if (nextRow < 0 || nextRow >= n || nextCol < 0 || nextCol >= n){
                System.out.println("move out of bounds. stopping at edge.");
                return;
            }
            row = nextRow;
            col = nextCol;
            if (!penUp){
                floor[row][col] = 1;
            }
        }
    }
    private void printfloor(){
        //print floor
        for(int r = n -1; r>= 0; r--){
            System.out.printf("%2d|", r);
            for (int c =0; c < n; c++){
                if (floor[r][c] == 1){
                    System.out.print("* ");
                }
                else {
                    System.out.print(".  ");
                }
            }
            System.out.println();
        }
        //print column indices
        System.out.print("  ");
        for (int i =0; i < n*2; i++){
            System.out.print("-");
        }
        System.out.println();

        System.out.print(" ");
        for (int c =0; c < n; c++){
            System.out.printf("%2d", c);
            //{System.out.print(c + " ");}
        }
        System.out.println();
    }

    private void replayHistory()
    {
        List<String> temp = new ArrayList<>(history);
        initisalize(n);

        System.out.println(":):)");
        for (String command : temp) {
            execute(command);
        }
    }
    private String directionToString(){
        return switch (direction) {
            case NORTH -> "NORTH";
            case EAST -> "EAST";
            case SOUTH -> "SOUTH";
            case WEST -> "WEST";
            default -> "->->";
        };
    }


    private static void printHelp(){
        System.out.println("""
                commands:
                u - pen up
                d - pen down
                r - turn right  
                l - turn left
                n - turn north
                s - turn south
                ms - move s steps (example: m5)
                p - print floor
                c - current position
                i n - initialize system with size n
                h - replay history
                q - quit
                """);
    }
    private void printStatus(){
        System.out.println("Position: (" + this.row + ", " + this.col + ")");
        System.out.println("Direction: " + directionToString());
        System.out.println("Pen: " + (this.penUp ? "UP" : "DOWN"));
    }

    // Instance methods for testing
    public void initializeFloor(int size) {
        this.n = size;
        this.floor = new int[n][n];
        this.row = 0;
        this.col = 0;
        this.direction = NORTH;
        this.penUp = true;
        this.history.clear();
    }

    public void executeCommand(String cmd) {
        execute(cmd);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isPenUp() {
        return penUp;
    }

    public int getFloorSize() {
        return n;
    }

    public int[][] getFloor() {
        return floor;
    }

    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

}