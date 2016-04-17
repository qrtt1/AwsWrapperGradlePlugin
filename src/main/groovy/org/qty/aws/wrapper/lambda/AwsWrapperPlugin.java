package org.qty.aws.wrapper.lambda;

import jp.classmethod.aws.gradle.lambda.AWSLambdaPlugin;
import jp.classmethod.aws.gradle.s3.AmazonS3Plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class AwsWrapperPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(AmazonS3Plugin.class);
        project.getPluginManager().apply(AWSLambdaPlugin.class);
    }

}
