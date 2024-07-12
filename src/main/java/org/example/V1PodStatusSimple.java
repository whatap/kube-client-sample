package org.example;

import java.util.ArrayList;
import java.util.List;

import io.kubernetes.client.openapi.models.V1ContainerStatus;
import io.kubernetes.client.openapi.models.V1PodStatus;
import whatap.lang.value.Value;

public class V1PodStatusSimple {

    private List<V1ContainerStatusSimple> cstatuses;
    private String phase;

    public V1PodStatusSimple(V1PodStatus status) {
        cstatuses = parseCStatuses(status);
        this.phase = status.getPhase();
    }

    private List<V1ContainerStatusSimple> parseCStatuses(V1PodStatus statusList) {
        ArrayList<V1ContainerStatusSimple> ret = new ArrayList<V1ContainerStatusSimple>();
        if (statusList.getContainerStatuses() != null) {
            for(V1ContainerStatus status: statusList.getContainerStatuses()) {
                ret.add(new V1ContainerStatusSimple(status));
            }
        }

        return ret;
    }

    public List<V1ContainerStatusSimple> getContainerStatuses() {

        return this.cstatuses;
    }

    public String getPhase() {

        return this.phase;
    }

}
