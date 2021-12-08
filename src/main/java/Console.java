import service.MetricService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Console {

    private final MetricService metricService;

    public Console() throws IOException {
        Properties prop = new Properties();

        InputStream inputStream = ConsoleRunner.class.getClassLoader()
                .getResourceAsStream("console.properties");
        prop.load(inputStream);

        metricService = new MetricService(prop.getProperty("gcp.domain"), prop.getProperty("gcp.project"));
    }

    public static void banner() {
        System.out.println("\n" +
                "   _____  _____ _____             __  __             _ _                _____                      _      \n" +
                "  / ____|/ ____|  __ \\           |  \\/  |           (_) |              / ____|                    | |     \n" +
                " | |  __| |    | |__) |  ______  | \\  / | ___  _ __  _| |_ ___  _ __  | |     ___  _ __  ___  ___ | | ___ \n" +
                " | | |_ | |    |  ___/  |______| | |\\/| |/ _ \\| '_ \\| | __/ _ \\| '__| | |    / _ \\| '_ \\/ __|/ _ \\| |/ _ \\\n" +
                " | |__| | |____| |               | |  | | (_) | | | | | || (_) | |    | |___| (_) | | | \\__ \\ (_) | |  __/\n" +
                "  \\_____|\\_____|_|               |_|  |_|\\___/|_| |_|_|\\__\\___/|_|     \\_____\\___/|_| |_|___/\\___/|_|\\___|\n" +
                "                                                                                                          \n" +
                "                                                                                                          \n");

        System.out.println("Google Cloud Platform - Metric | Resource | Logging Console");
        System.out.println();
    }

    public static void usage() {
        System.out.println("Usage:");
        System.out.println();
        System.out.println("  new-metric-descriptor       | Creates a metric descriptor");
        System.out.println("  list-metric-descriptors     | Lists of metric descriptors");
        System.out.println("  delete-metric-descriptors   | Deletes a metric descriptor");
        System.out.println("  list-monitored-resources    | Lists the monitored resources");
        System.out.println("  get-resource                | Describes a monitored resource");
        System.out.println("  list-log-metrics            | Lists of log metrics");
        System.out.println("  new-log-metrics             | Creates a log metrics");
        System.out.println("  delete-log-metrics          | Deletes a log metrics");
        System.out.println("  exit                        | Exit from console");
        System.out.println();
    }

    /**
     * Handles a single command.
     *
     * @param commandLine A line of input provided by the user
     */
    public void handleCommandLine(String commandLine) throws IOException {
        String[] args = commandLine.split("\\s+");

        if (args.length < 1) {
            throw new IllegalArgumentException("not enough args");
        }

        String command = args[0];
        switch (command) {
            case "new-metric-descriptor":
                // Everything after the first whitespace token is interpreted to be the description.
                args = commandLine.split("\\s+", 2);
                if (args.length != 2) {
                    throw new IllegalArgumentException("usage: <type>");
                }
                // Set created to now() and done to false.
                metricService.createMetricDescriptor(args[1]);
                System.out.println("Metric descriptor created");
                break;
            case "list-metric-descriptors":
                args = commandLine.split("\\s+", 2);
                if (args.length != 1) {
                    throw new IllegalArgumentException("usage: no arguments");
                }
                metricService.listMetricDescriptors();
                break;
            case "list-monitored-resources":
                args = commandLine.split("\\s+", 2);
                if (args.length != 1) {
                    throw new IllegalArgumentException("usage: no arguments");
                }
                metricService.listMonitoredResources();
                break;
            case "get-resource":
                args = commandLine.split("\\s+", 2);
                if (args.length != 2) {
                    throw new IllegalArgumentException("usage: <type>");
                }
                metricService.describeMonitoredResources(args[1]);
                break;
            case "delete-metric-descriptor":
                args = commandLine.split("\\s+", 2);
                if (args.length != 2) {
                    throw new IllegalArgumentException("usage: <type>");
                }
                metricService.deleteMetricDescriptor(args[1]);
                break;
            case "list-log-metrics":
                args = commandLine.split("\\s+", 2);
                if (args.length != 1) {
                    throw new IllegalArgumentException("usage: no arguments");
                }
                metricService.listLogMetrics();
                break;
            case "new-log-metrics":
                args = commandLine.split("\\s+", 2);
                if (args.length != 2) {
                    throw new IllegalArgumentException("usage: <type>");
                }
                metricService.createLogMetrics(args[1]);
                break;
            case "delete-log-metrics":
                args = commandLine.split("\\s+", 2);
                if (args.length != 2) {
                    throw new IllegalArgumentException("usage: <type>");
                }
                metricService.deleteLogMetrics(args[1]);
                break;
            case "exit":
                System.out.println("exiting...");
                System.exit(0);
                break;
            default:
                throw new IllegalArgumentException("unrecognized command: " + command);
        }
    }
}
