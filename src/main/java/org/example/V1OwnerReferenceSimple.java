package org.example;

import io.kubernetes.client.openapi.models.V1OwnerReference;
import whatap.lang.value.Value;

public class V1OwnerReferenceSimple {

    private String kind;
    private String name;

    public V1OwnerReferenceSimple(V1OwnerReference ownerReference) {
        this.kind = ownerReference.getKind();
        this.name = ownerReference.getName();
    }

    public String getKind() {

        return this.kind;
    }

    public String getName() {

        return this.name;
    }

}
