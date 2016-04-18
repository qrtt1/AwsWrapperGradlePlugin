package org.qty.aws.wrapper.lambda

import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.model.Model
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.RuleSource

class AwsWrapperRule extends RuleSource {

    @Model
    public LambdaConfigExtension lambdaConfig(ExtensionContainer container) {
        return container.getByType(LambdaConfigExtension.class)
    }

    @Mutate
    public void generateUpdateTask(ModelMap<Task> tasks, LambdaConfigExtension extension) {
        extension.function.each { function -> 
            tasks.create("updateLambdaFunction${function.name}", {
                group = "Amazon Lambda Updater tasks"
                description = "update Lambda Function [${function.name}]"
                doLast {
                    println "do nothing function ${function.name}"
                }
            })
        }
    }
}
