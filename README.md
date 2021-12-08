# GCP - METRIC CONSOLE

Java CLI for create metrics and logging metrics dynamically on Google Cloud Platform


#### SETUP

- You need Google Cloud SKD installed and configured with your domain.
- JDK 1.8 required

#### USAGE

- Setup you're own properties on console.properties file
- Write the json file with the metrics you want to create
- Open the ConsoleRunner class and run it (or create .jar file and execute)
- Use the CLI for create (with file)/delete/get the metrics or log metrics


### CLI

```
Google Cloud Platform - Metric | Resource | Logging Console

Usage:

new-metric-descriptor       | Creates a metric descriptor
list-metric-descriptors     | Lists of metric descriptors
delete-metric-descriptors   | Deletes a metric descriptor
list-monitored-resources    | Lists the monitored resources
get-resource                | Describes a monitored resource
list-log-metrics            | Lists of log metrics
new-log-metrics             | Creates a log metrics
delete-log-metrics          | Deletes a log metrics
exit                        | Exit from console

GCP Metrics > 
```
