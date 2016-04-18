package org.qty.aws.wrapper.lambda

import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.language.assembler.tasks.Assemble;
import org.gradle.model.Model
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.model.Validate

import com.google.common.base.Preconditions;;

class AwsWrapperRule extends RuleSource {

    @Model
    public LambdaConfigExtension lambdaConfig(ExtensionContainer container) {
        return container.getByType(LambdaConfigExtension.class)
    }
    
    @Validate
    public void validateSourceBlock(LambdaConfigExtension extension){
        SourceBlock source = extension.source;
        Preconditions.checkState(source != null, "lambdaConfig.source{} cannot be null.")
        
        if(source.file != null) {
            Preconditions.checkState(source.bucketName == null && source.key == null, 
                "lambdaConfig.source{} should be one of the (file, bucketName with key)")
        }
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
