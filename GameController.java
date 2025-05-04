import java.io.FileWriter;
import java.io.IOException;

public class GameController {
    MazeManager maze;
    TurnManager turns;
    int maxTurns = 100;
    int turnCount = 0;
    Agent[] agents;
    int goalX, goalY;
    Agent[] finishOrder;
    int finishIndex = 0;

    public void initializeGame(int numAgents, int width, int height, int maxTurns) {
        this.maxTurns = maxTurns;
        maze = new MazeManager(width, height, numAgents);
        turns = new TurnManager();
        agents = new Agent[numAgents];
        finishOrder = new Agent[numAgents];
        goalX = width - 1;
        goalY = height - 1;

        for (int i = 0; i < numAgents; i++) {
            int x, y;
            do {
                x = (int) (Math.random() * width);
                y = (int) (Math.random() * height);
            } while (!maze.grid[x][y].isTraversable() || maze.grid[x][y].hasAgent);

            agents[i] = new Agent(i + 1, x, y);
            maze.grid[x][y].hasAgent = true;
            turns.addAgent(agents[i]);
        }
    }

    public void runSimulation() {
        while (turnCount < maxTurns && !turns.allAgentsFinished()) {
            Agent current = turns.getCurrentAgent();
            if (current == null) break;
            if (!current.hasReachedGoal) {
                processAgentAction(current);
            }
            turns.printTurnOrder();
            turns.advanceTurn();
            maze.rotateCorridor(turnCount % maze.height);
            turnCount++;
        }
        printFinalStatistics();
        logGameSummaryToFile("simulation_log.txt");
    }

    public void processAgentAction(Agent agent) {
        int oldX = agent.currentX;
        int oldY = agent.currentY;

        String move = agent.chooseNextMove(maze, goalX, goalY);
        if (!move.equals("WAIT")) {
            agent.move(move);
            maze.updateAgentLocation(agent, oldX, oldY);
            MazeTile tile = maze.getTile(agent.currentX, agent.currentY);
            checkTileEffect(agent, tile);
            if (tile.type == 'G' && !agent.hasReachedGoal) {
                agent.hasReachedGoal = true;
                finishOrder[finishIndex++] = agent;
            }
        }

        turns.logTurnSummary(agent);
        printAgentStack(agent);
        printMazeVisual();
    }

    public void checkTileEffect(Agent a, MazeTile tile) {
        if (tile.type == 'T') {
            a.trapsTriggered++;
            a.backtrack();
            System.out.println("Agent " + a.id + " triggered a trap! Backtracking...");
        } else if (tile.type == 'P') {
            a.hasPowerUp = true;
            System.out.println("Agent " + a.id + " collected a power-up!");
        }
    }

    public void printAgentStack(Agent a) {
        System.out.println("Agent " + a.id + " Stack (last moves):");
        System.out.println(a.getMoveHistoryAsString());
    }

    public void printMazeVisual() {
        System.out.println("Maze State:");

        // Top border
        System.out.print("+");
        for (int j = 0; j < maze.height; j++) System.out.print("-");
        System.out.println("+");

        for (int i = 0; i < maze.width; i++) {
            System.out.print("|");
            for (int j = 0; j < maze.height; j++) {
                MazeTile tile = maze.grid[i][j];
                boolean printedAgent = false;
                for (Agent a : agents) {
                    if (a.currentX == i && a.currentY == j) {
                        System.out.print(a.id);  // Show agent ID
                        printedAgent = true;
                        break;
                    }
                }
                if (!printedAgent) {
                    switch (tile.type) {
                        case 'W': System.out.print("#"); break;
                        case 'T': System.out.print("T"); break;
                        case 'P': System.out.print("P"); break;
                        case 'G': System.out.print("G"); break;
                        default: System.out.print(" "); break;
                    }
                }
            }
            System.out.println("|");
        }

        // Bottom border
        System.out.print("+");
        for (int j = 0; j < maze.height; j++) System.out.print("-");
        System.out.println("+");

        // Print agent status
        System.out.println("Agent Positions:");
        for (Agent a : agents) {
            System.out.print("Agent " + a.id + " → (" + a.currentX + ", " + a.currentY + ")");
            System.out.print(" | Power-Up: " + a.hasPowerUp);
            System.out.print(" | Immune: " + a.trapImmune);
            System.out.print(" | Goal: " + a.hasReachedGoal);
            System.out.println(" | Path: " + lastMoves(a, 3));
        }
        System.out.println();
    }

    private String lastMoves(Agent a, int limit) {
        AgentStack temp = new AgentStack();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        while (!a.moveHistory.isEmpty()) {
            int[] pos = a.moveHistory.pop();
            temp.push(pos[0], pos[1]);
            count++;
        }
        while (!temp.isEmpty()) {
            int[] pos = temp.pop();
            if (count-- <= limit) sb.append("(").append(pos[0]).append(",").append(pos[1]).append(") ");
            a.moveHistory.push(pos[0], pos[1]);
        }
        return sb.toString().trim();
    }


    public void printFinalStatistics() {
        System.out.println("\nSimulation Complete!");
        for (Agent a : agents) {
            System.out.println("Agent " + a.id + " Stats:");
            System.out.println("  Reached Goal: " + a.hasReachedGoal);
            System.out.println("  Total Moves: " + a.totalMoves);
            System.out.println("  Backtracks: " + a.backtracks);
            System.out.println("  Traps Triggered: " + a.trapsTriggered);
            System.out.println("  Max Stack Depth: " + a.moveHistory.getSize());
        }
        System.out.println("Total Rounds: " + turnCount);
        if (finishIndex > 0) {
            System.out.println("Winner: Agent " + finishOrder[0].id);
        }
    }

    public void logGameSummaryToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Game Summary:\n");
            for (Agent a : agents) {
                writer.write("Agent " + a.id + ":\n");
                writer.write("  Reached Goal: " + a.hasReachedGoal + "\n");
                writer.write("  Moves: " + a.totalMoves + ", Backtracks: " + a.backtracks + "\n");
                writer.write("  Traps: " + a.trapsTriggered + ", Power-up: " + a.hasPowerUp + "\n");
                writer.write("  Stack Depth: " + a.moveHistory.getSize() + "\n\n");
            }
            writer.write("Total Rounds: " + turnCount + "\n");
            if (finishIndex > 0) {
                writer.write("Winner: Agent " + finishOrder[0].id + "\n");
            }
        } catch (IOException e) {
            System.out.println("Failed to write log: " + e.getMessage());
        }
    }
}
