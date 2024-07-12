package org.example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1OwnerReference;

public class V1ObjectMetaSimple {

    private String namespace;
    private String name;
    private String uid;
    private List<V1OwnerReferenceSimple> ownerReferences;
    private Map<String, String> labels = new HashMap<String, String>();

    public V1ObjectMetaSimple(V1ObjectMeta metadata) {
        this.namespace = metadata.getNamespace();
        this.name = metadata.getName();
        this.ownerReferences = parseOwnerReferences(metadata.getOwnerReferences());
        this.uid = metadata.getUid();
        if (metadata.getLabels() != null)this.labels.putAll(metadata.getLabels());
    }

    private List<V1OwnerReferenceSimple> parseOwnerReferences(List<V1OwnerReference> ownerReferences2) {
        List<V1OwnerReferenceSimple> simpleORs= new ArrayList<V1OwnerReferenceSimple>();
        if (ownerReferences2 != null) {
            for(V1OwnerReference ownerReference: ownerReferences2) {
                simpleORs.add(new V1OwnerReferenceSimple(ownerReference));
            }
        }

        return simpleORs;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getName() {
        return this.name;
    }
    public String getUid() {return this.uid;}
    public List<V1OwnerReferenceSimple> getOwnerReferences() {

        return this.ownerReferences;
    }

    public Map<String, String> getLabels() {

        return this.labels;
    }

}
