package org.example;

import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1PodStatus;
public class V1PodSimple {
    private V1ObjectMetaSimple metadata;
    private V1PodStatusSimple status;

    public V1PodSimple(V1Pod a) {
        this.metadata = new V1ObjectMetaSimple(a.getMetadata());
        if (a.getStatus() != null) this.status = new V1PodStatusSimple(a.getStatus());
    }

    public V1ObjectMetaSimple getMetadata() {

        return metadata;
    }

    public V1PodStatusSimple getStatus() {

        return status;
    }
}
