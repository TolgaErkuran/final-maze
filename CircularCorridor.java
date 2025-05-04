public class CircularCorridor {
    private static class Node {
        MazeTile tile;
        Node next;

        Node(MazeTile tile) {
            this.tile = tile;
        }
    }

    private Node head;
    private int size;

    public CircularCorridor() {
        head = null;
        size = 0;
    }

    public void add(MazeTile tile) {
        Node newNode = new Node(tile);
        if (head == null) {
            head = newNode;
            newNode.next = head;
        } else {
            Node temp = head;
            while (temp.next != head) temp = temp.next;
            temp.next = newNode;
            newNode.next = head;
        }
        size++;
    }

    public void rotateRight() {
        if (head == null || head.next == head) return;
        Node curr = head;
        while (curr.next != head) curr = curr.next;
        head = curr;
    }

    public MazeTile[] toArray() {
        MazeTile[] result = new MazeTile[size];
        if (head == null) return result;
        Node temp = head;
        for (int i = 0; i < size; i++) {
            result[i] = temp.tile;
            temp = temp.next;
        }
        return result;
    }
}
