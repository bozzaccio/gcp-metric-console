import java.util.Scanner;

public class ConsoleRunner {

    /**
     * Exercises the methods defined in this class.
     *
     * <p>
     *
     * <p>Assumes that you are authenticated using the Google Cloud SDK (using {@code gcloud auth
     * application-default-login}).
     */
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        Console console = new Console();

        Console.banner();
        Console.usage();
        while (true) {
            System.out.print("GCP Metrics > ");
            String line = scanner.next();
            if (line.trim().isEmpty()) {
                break;
            }
            try {
                console.handleCommandLine(line);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                Console.usage();
            }
        }
    }
}
