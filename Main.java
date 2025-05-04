import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter maze width: ");
        int width = scanner.nextInt();

        System.out.print("Enter maze height: ");
        int height = scanner.nextInt();

        System.out.print("Enter number of agents: ");
        int agents = scanner.nextInt();

        System.out.print("Enter max number of turns: ");
        int maxTurns = scanner.nextInt();

        GameController game = new GameController();
        game.initializeGame(agents, width, height, maxTurns);
        game.runSimulation();
    }
}
