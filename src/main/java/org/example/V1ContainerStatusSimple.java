package org.example;
import io.kubernetes.client.openapi.models.V1ContainerStatus;

public class V1ContainerStatusSimple {

    private String cid;

    public V1ContainerStatusSimple(V1ContainerStatus status) {
        this.cid = status.getContainerID();
    }

    public String getContainerID() {
        return this.cid;
    }
}
