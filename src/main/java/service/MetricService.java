package service;

import com.google.api.LabelDescriptor;
import com.google.api.MetricDescriptor;
import com.google.api.MonitoredResourceDescriptor;
import com.google.cloud.logging.v2.MetricsClient;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.cloud.monitoring.v3.MetricServiceClient.ListMonitoredResourceDescriptorsPagedResponse;
import com.google.gson.Gson;
import com.google.logging.v2.CreateLogMetricRequest;
import com.google.logging.v2.ListLogMetricsRequest;
import com.google.logging.v2.LogMetric;
import com.google.monitoring.v3.*;
import model.InternalLogMetric;

import java.io.IOException;
import java.util.List;

public class MetricService {

    private final String metricDomain;
    private final String projectId;

    public MetricService(String metricDomain, String projectId) {
        this.metricDomain = metricDomain;
        this.projectId = projectId;
    }

    /**
     * Creates a metric descriptor.
     *
     * <p>See:
     * https://cloud.google.com/monitoring/api/ref_v3/rest/v3/projects.metricDescriptors/create
     *
     * @param type The metric type
     */
    public void createMetricDescriptor(String type) throws IOException {
        // [START monitoring_create_metric]
        // Your Google Cloud Platform project ID
        String metricType = metricDomain + "/" + type;

        final MetricServiceClient client = MetricServiceClient.create();
        ProjectName name = ProjectName.of(projectId);

        MetricDescriptor descriptor =
                MetricDescriptor.newBuilder()
                        .setType(metricType)
                        .addLabels(
                                LabelDescriptor.newBuilder()
                                        .setKey("store_id")
                                        .setValueType(LabelDescriptor.ValueType.STRING))
                        .setDescription("This is a simple example of a custom metric.")
                        .setMetricKind(MetricDescriptor.MetricKind.GAUGE)
                        .setValueType(MetricDescriptor.ValueType.DOUBLE)
                        .build();

        CreateMetricDescriptorRequest request =
                CreateMetricDescriptorRequest.newBuilder()
                        .setName(name.toString())
                        .setMetricDescriptor(descriptor)
                        .build();

        client.createMetricDescriptor(request);
        client.close();
        // [END monitoring_create_metric]
    }

    /**
     * Delete a metric descriptor.
     *
     * @param name Name of metric descriptor to delete
     */
    public void deleteMetricDescriptor(String name) throws IOException {
        // [START monitoring_delete_metric]
        final MetricServiceClient client = MetricServiceClient.create();
        MetricDescriptorName metricName = MetricDescriptorName.of(projectId, name);
        client.deleteMetricDescriptor(metricName);
        System.out.println("Deleted descriptor " + name);
        client.close();
        // [END monitoring_delete_metric]
    }

    /**
     * Returns the first page of all metric descriptors.
     */
    public void listMetricDescriptors() throws IOException {
        // [START monitoring_list_descriptors]

        ProjectName name = ProjectName.of(projectId);

        final MetricServiceClient client = MetricServiceClient.create();

        ListMetricDescriptorsRequest request =
                ListMetricDescriptorsRequest.newBuilder()
                        .setName(name.toString())
                        .build();
        MetricServiceClient.ListMetricDescriptorsPagedResponse response = client.listMetricDescriptors(request);

        System.out.println("Listing descriptors: ");

        for (MetricDescriptor d : response.iterateAll()) {
            System.out.println(d.getName() + " " + d.getDisplayName());
        }
        client.close();
        // [END monitoring_list_descriptors]
    }

    /**
     * Gets all monitored resource descriptors.
     */
    public void listMonitoredResources() throws IOException {
        // [START monitoring_list_resources]
        // Your Google Cloud Platform project ID

        final MetricServiceClient client = MetricServiceClient.create();
        ProjectName name = ProjectName.of(projectId);

        ListMonitoredResourceDescriptorsRequest request =
                ListMonitoredResourceDescriptorsRequest.newBuilder().setName(name.toString()).build();

        System.out.println("Listing monitored resource descriptors: ");

        ListMonitoredResourceDescriptorsPagedResponse response =
                client.listMonitoredResourceDescriptors(request);

        for (MonitoredResourceDescriptor d : response.iterateAll()) {
            System.out.println(d.getType());
        }
        client.close();
        // [END monitoring_list_resources]
    }

    // [START monitoring_get_resource]
    public void getMonitoredResource(String resourceId) throws IOException {
        MetricServiceClient client = MetricServiceClient.create();
        MonitoredResourceDescriptorName name =
                MonitoredResourceDescriptorName.of(projectId, resourceId);
        MonitoredResourceDescriptor response = client.getMonitoredResourceDescriptor(name);
        System.out.println("Retrieved Monitored Resource: " + new Gson().toJson(response));
        client.close();
    }
    // [END monitoring_get_resource]

    /**
     * Gets full information for a monitored resource.
     *
     * @param type The resource type
     */
    public void describeMonitoredResources(String type) throws IOException {
        // [START monitoring_get_descriptor]
        // Your Google Cloud Platform project ID
        final MetricServiceClient client = MetricServiceClient.create();
        MonitoredResourceDescriptorName name = MonitoredResourceDescriptorName.of(projectId, type);
        MonitoredResourceDescriptor response = client.getMonitoredResourceDescriptor(name);

        System.out.println("Printing monitored resource descriptor: ");
        System.out.println(response);
        client.close();
        // [END monitoring_get_descriptor]
    }

    public void listLogMetrics() throws IOException {
        // [START list-log-metrics]
        ProjectName name = ProjectName.of(projectId);

        MetricsClient client = MetricsClient.create();

        ListLogMetricsRequest request = ListLogMetricsRequest.newBuilder()
                .setParent(name.toString())
                .build();

        MetricsClient.ListLogMetricsPagedResponse response = client.listLogMetrics(request);

        System.out.println("Listing Log Metrics: ");

        for (LogMetric d : response.iterateAll()) {
            System.out.println(d.getName() + " " + d.getDescription());
        }
        client.close();
        // [END list-log-metrics]
    }

    public void createLogMetrics(String resourceFileName) throws IOException {

        ProjectName name = ProjectName.of(projectId);

        MetricsClient client = MetricsClient.create();

        List<InternalLogMetric> metricList = new JSONService<InternalLogMetric>().convert(resourceFileName);

        for (InternalLogMetric metric : metricList) {

            LogMetric inputMetric = LogMetric.newBuilder()
                    .setName(metric.getName())
                    .setDescription(metric.getDescription())
                    .setFilter(metric.getFilter())
                    .build();

            CreateLogMetricRequest request = CreateLogMetricRequest.newBuilder()
                    .setParent(name.toString())
                    .setMetric(inputMetric)
                    .build();

            LogMetric response = client.createLogMetric(request);
            System.out.println("Log Metric successfully created: " + response.getName());
        }

        client.close();
    }

    public void deleteLogMetrics(String logMetricName) throws IOException {

        ProjectName name = ProjectName.of(projectId);

        MetricsClient client = MetricsClient.create();

        String path = String.format("%s/metrics/%s", name, logMetricName);

        client.deleteLogMetric(path);
        System.out.println("Log Metric successfully deleted.");
        client.close();
    }
}