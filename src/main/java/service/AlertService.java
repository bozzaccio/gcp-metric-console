package service;

import com.google.cloud.monitoring.v3.AlertPolicyServiceClient;
import com.google.monitoring.v3.AlertPolicy;
import com.google.monitoring.v3.ListAlertPoliciesRequest;
import com.google.monitoring.v3.ProjectName;

import java.io.IOException;

/**
 * Service class for alert policies management.
 *
 * <p>
 * implementation of alert gcp service client, see also {@link AlertPolicyServiceClient}.
 * </p>
 */
public class AlertService {

    private final ProjectName projectName;

    public AlertService(String projectId) {
        this.projectName = ProjectName.of(projectId);
    }

    public void readAllAlertPolicies() throws IOException {

        AlertPolicyServiceClient client = AlertPolicyServiceClient.create();

        ListAlertPoliciesRequest request = ListAlertPoliciesRequest
                .newBuilder()
                .setName(this.projectName.toString())
                .build();

        AlertPolicyServiceClient.ListAlertPoliciesPagedResponse response = client.listAlertPolicies(request);

        System.out.println("Listing alert policies: ");


        for (AlertPolicy policy : response.iterateAll()) {
            System.out.println(policy.getDisplayName());
            System.out.println("Conditions: ");

            for (AlertPolicy.Condition condition : policy.getConditionsList()) {
                System.out.println(condition.getDisplayName());
            }

            System.out.println();
        }
        client.close();
    }
}
