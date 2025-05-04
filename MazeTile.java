public class MazeTile {
    int x, y;
    char type; // E, W, T, P, G
    boolean hasAgent;

    public MazeTile(int x, int y, char type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.hasAgent = false;
    }

    public boolean isTraversable() {
        return type != 'W';
    }

    @Override
    public String toString() {
        return hasAgent ? "A" : String.valueOf(type);
    }
}
