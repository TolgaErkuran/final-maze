import java.util.Random;

public class MazeManager {
    MazeTile[][] grid;
    int width, height;
    Agent[] agents;
    int[] rotatingRows;

    public MazeManager(int width, int height, int agentCount) {
        this.width = width;
        this.height = height;
        this.grid = new MazeTile[width][height];
        this.agents = new Agent[agentCount];
        this.rotatingRows = new int[] {1, 3, 5};
        generateMaze();
    }

    public void generateMaze() {
        Random rand = new Random();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                char type;
                double r = rand.nextDouble();
                if (r < 0.20) type = 'W';
                else if (r < 0.22) type = 'T';
                else if (r < 0.24) type = 'P';
                else type = 'E';
                grid[i][j] = new MazeTile(i, j, type);
            }
        }
        grid[width - 1][height - 1].type = 'G';
    }

    public void rotateCorridor(int rowId) {
        if (rowId < 0 || rowId >= height) return;
        CircularCorridor corridor = new CircularCorridor();
        for (int i = 0; i < width; i++) {
            corridor.add(grid[i][rowId]);
        }
        corridor.rotateRight();
        MazeTile[] rotated = corridor.toArray();
        for (int i = 0; i < width; i++) {
            grid[i][rowId] = rotated[i];
        }
    }

    public boolean isValidMove(int fromX, int fromY, String direction) {
        int toX = fromX, toY = fromY;
        switch (direction.toUpperCase()) {
            case "UP": toX--; break;
            case "DOWN": toX++; break;
            case "LEFT": toY--; break;
            case "RIGHT": toY++; break;
            default: return false;
        }
        if (toX < 0 || toX >= width || toY < 0 || toY >= height) return false;
        return grid[toX][toY].isTraversable();
    }

    public MazeTile getTile(int x, int y) {
        return grid[x][y];
    }

    public void updateAgentLocation(Agent a, int oldX, int oldY) {
        grid[oldX][oldY].hasAgent = false;
        grid[a.currentX][a.currentY].hasAgent = true;
    }

    public void printMazeSnapshot() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}
