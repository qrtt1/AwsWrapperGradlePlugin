package org.qty.aws.wrapper.lambda;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class BeforeLambdaUpdateTask extends DefaultTask {
    
    public final static String NAME = "beforeLambdaUpdate";

    public BeforeLambdaUpdateTask() {
        setGroup(AwsWrapperPlugin.GROUP);
    }

    @TaskAction
    public void action() {

    }

}
