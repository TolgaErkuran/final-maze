public class TurnManager {
    private TurnQueue agentQueue;
    private int currentRound;

    public TurnManager() {
        this.agentQueue = new TurnQueue();
        this.currentRound = 0;
    }

    public void addAgent(Agent agent) {
        agentQueue.enqueue(agent);
    }

    public void advanceTurn() {
        currentRound++;
        agentQueue.rotate();
    }

    public Agent getCurrentAgent() {
        return agentQueue.peek();
    }

    public boolean allAgentsFinished() {
        int size = agentQueue.getSize();
        for (int i = 0; i < size; i++) {
            Agent a = agentQueue.dequeue();
            agentQueue.enqueue(a);
            if (!a.hasReachedGoal) return false;
        }
        return true;
    }

    public void logTurnSummary(Agent a) {
        System.out.println("Turn " + currentRound + ": Agent " + a.id + " at (" + a.currentX + "," + a.currentY + ")");
        System.out.println("Moves: " + a.totalMoves + ", Backtracks: " + a.backtracks + ", Power-up: " + a.hasPowerUp);
        System.out.println("Recent Path: " + a.getMoveHistoryAsString());
        System.out.println();
    }

    public void printTurnOrder() {
        System.out.println("Current Turn Order: " + agentQueue.getTurnOrder());
    }

    public int getCurrentRound() {
        return currentRound;
    }
}
