public class AgentStack {
    private static class StackNode {
        int x, y;
        StackNode next;
        StackNode(int x, int y) { this.x = x; this.y = y; }
    }

    private StackNode top;
    private int size;

    public AgentStack() {
        top = null;
        size = 0;
    }

    public void push(int x, int y) {
        StackNode n = new StackNode(x, y);
        n.next = top;
        top = n;
        size++;
    }

    public int[] pop() {
        if (top == null) return null;
        int[] c = {top.x, top.y};
        top = top.next;
        size--;
        return c;
    }

    public int[] peek() {
        return top == null ? null : new int[] {top.x, top.y};
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int getSize() {
        return size;
    }

    public void clear() {
        top = null;
        size = 0;
    }
}
