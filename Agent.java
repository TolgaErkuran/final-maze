public class Agent {
    int id;
    int currentX, currentY;
    AgentStack moveHistory;
    boolean hasReachedGoal;
    boolean hasPowerUp;
    boolean trapImmune;
    int totalMoves, backtracks, trapsTriggered;

    public Agent(int id, int sx, int sy) {
        this.id = id;
        this.currentX = sx;
        this.currentY = sy;
        moveHistory = new AgentStack();
        moveHistory.push(sx, sy);
        hasReachedGoal = hasPowerUp = trapImmune = false;
    }

    public void move(String direction) {
        int newX = currentX;
        int newY = currentY;
        switch (direction.toUpperCase()) {
            case "UP": newX--; break;
            case "DOWN": newX++; break;
            case "LEFT": newY--; break;
            case "RIGHT": newY++; break;
            default: return;
        }
        currentX = newX;
        currentY = newY;
        moveHistory.push(currentX, currentY);
        totalMoves++;
    }

    public void applyPowerUp() {
        if (hasPowerUp) {
            trapImmune = true;
            hasPowerUp = false;
            System.out.println("Agent " + id + " is now immune to the next trap!");
        }
    }

    public void usePowerUp() {
        applyPowerUp();
    }

    public void backtrack() {
        if (trapImmune) {
            System.out.println("Agent " + id + " resisted trap due to immunity.");
            trapImmune = false;
            return;
        }
        // Auto-use power-up if available before trap effect
        if (hasPowerUp) {
            System.out.println("Agent " + id + " auto-used power-up to avoid trap!");
            usePowerUp();
            trapImmune = true;
            backtrack();
            return;
        }
        for (int i = 0; i < 2 && moveHistory.getSize() > 1; i++) {
            moveHistory.pop();
            backtracks++;
        }
        int[] prev = moveHistory.peek();
        currentX = prev[0];
        currentY = prev[1];
    }

    public String getMoveHistoryAsString() {
        AgentStack temp = new AgentStack();
        StringBuilder sb = new StringBuilder();
        while (!moveHistory.isEmpty()) {
            int[] pos = moveHistory.pop();
            sb.insert(0, "(" + pos[0] + "," + pos[1] + ") ");
            temp.push(pos[0], pos[1]);
        }
        while (!temp.isEmpty()) {
            int[] pos = temp.pop();
            moveHistory.push(pos[0], pos[1]);
        }
        return sb.toString();
    }

    public String chooseNextMove(MazeManager maze, int goalX, int goalY) {
        int width = maze.width;
        int height = maze.height;

        boolean[][] visited = new boolean[width][height];
        String[][] cameFrom = new String[width][height];
        LinkedList<int[]> queue = new LinkedList<>();

        visited[currentX][currentY] = true;
        queue.add(new int[] {currentX, currentY});

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            int[] pos = queue.get(0);
            queue.remove(0);
            int x = pos[0];
            int y = pos[1];

            if (x == goalX && y == goalY) {
                String key = x + "," + y;
                String prev = cameFrom[x][y];
                while (prev != null && !prev.equals(currentX + "," + currentY)) {
                    String[] parts = prev.split(",");
                    x = Integer.parseInt(parts[0]);
                    y = Integer.parseInt(parts[1]);
                    key = prev;
                    prev = cameFrom[x][y];
                }
                String[] parts = key.split(",");
                int dx = Integer.parseInt(parts[0]) - currentX;
                int dy = Integer.parseInt(parts[1]) - currentY;
                if (dx == -1) return "UP";
                if (dx == 1) return "DOWN";
                if (dy == -1) return "LEFT";
                if (dy == 1) return "RIGHT";
            }

            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (nx < 0 || ny < 0 || nx >= width || ny >= height) continue;
                if (!maze.grid[nx][ny].isTraversable()) continue;
                if (visited[nx][ny]) continue;
                visited[nx][ny] = true;
                queue.add(new int[] {nx, ny});
                cameFrom[nx][ny] = x + "," + y;
            }
        }
        return "WAIT";
    }
}
