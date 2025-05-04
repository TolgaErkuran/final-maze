public class TurnQueue {
    private static class QueueNode {
        Agent agent;
        QueueNode next;

        QueueNode(Agent agent) {
            this.agent = agent;
            this.next = null;
        }
    }

    private QueueNode front;
    private QueueNode rear;
    private int size;

    public TurnQueue() {
        front = rear = null;
        size = 0;
    }

    public void enqueue(Agent agent) {
        QueueNode newNode = new QueueNode(agent);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public Agent dequeue() {
        if (isEmpty()) return null;
        Agent a = front.agent;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return a;
    }

    public Agent peek() {
        return isEmpty() ? null : front.agent;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int getSize() {
        return size;
    }

    public void rotate() {
        if (size <= 1) return;
        Agent a = dequeue();
        enqueue(a);
    }

    public String getTurnOrder() {
        StringBuilder sb = new StringBuilder("[");
        QueueNode temp = front;
        while (temp != null) {
            sb.append("Agent ").append(temp.agent.id);
            if (temp.next != null) sb.append(", ");
            temp = temp.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
