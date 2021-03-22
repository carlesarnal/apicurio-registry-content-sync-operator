/*
 * Copyright 2021 Red Hat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apicurio.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import io.apicurio.registry.events.dto.ArtifactId;
import io.apicurio.sync.api.Artifact;
import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;

/**
 * @author Fabian Martinez
 */
@ApplicationScoped
public class ArtifactLabelsHandler {

    private static final String GROUP_ID_LABEL = "apicur.io/groupId";
    private static final String ARTIFACT_ID_LABEL = "apicur.io/artifactId";
    private static final String VERSION_LABEL = "apicur.io/version";

    public void setLabels(Artifact artifact) {

        Map<String,String> labels = Optional.ofNullable(artifact.getMetadata().getLabels())
            .orElse(new HashMap<>());
        artifact.getMetadata().setLabels(labels);

        if (artifact.getSpec().getGroupId() != null) {
            labels.put(GROUP_ID_LABEL, artifact.getSpec().getGroupId());
        }
        labels.put(ARTIFACT_ID_LABEL, artifact.getSpec().getArtifactId());
        labels.put(VERSION_LABEL, artifact.getSpec().getVersion());

    }

    public LabelSelector getLabelSelector(ArtifactId artifact) {

        Map<String,String> labels = new HashMap<>();
        if (artifact.getGroupId() != null) {
            labels.put(GROUP_ID_LABEL, artifact.getGroupId());
        }
        labels.put(ARTIFACT_ID_LABEL, artifact.getArtifactId());
        labels.put(VERSION_LABEL, artifact.getVersion());

        return new LabelSelectorBuilder()
                .addToMatchLabels(labels)
                .build();
    }

}
