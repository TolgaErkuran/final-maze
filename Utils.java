public class Utils {
    public static void printDivider() {
        System.out.println("------------------------------");
    }

    public static boolean isDirection(String input) {
        return input.equals("UP") || input.equals("DOWN") || input.equals("LEFT") || input.equals("RIGHT");
    }
}
