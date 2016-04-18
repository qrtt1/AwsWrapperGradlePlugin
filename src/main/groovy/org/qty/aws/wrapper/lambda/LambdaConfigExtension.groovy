package org.qty.aws.wrapper.lambda

import org.gradle.api.Project

class LambdaConfigExtension {

    private Project project;

    public LambdaConfigExtension(Project project) {
        this.project = project;
    }
}
