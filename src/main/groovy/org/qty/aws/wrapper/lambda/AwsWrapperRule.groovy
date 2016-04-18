package org.qty.aws.wrapper.lambda

import jp.classmethod.aws.gradle.lambda.AWSLambdaUpdateFunctionCodeTask
import jp.classmethod.aws.gradle.lambda.S3File;

import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.model.Model
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.model.Validate

import com.google.common.base.Preconditions

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
            
            File path = new File(source.file).getAbsoluteFile()
            Preconditions.checkState(path.exists(),
                "lambdaConfig.source{} file not found: " + path)
            
        } else {
            Preconditions.checkState(source.bucketName != null,
                    "lambdaConfig.source{} property bucketName cannot be null ")
            Preconditions.checkState(source.key != null,
                    "lambdaConfig.source{} property key cannot be null ")
        }
    }

    @Mutate
    public void generateUpdateTask(ModelMap<Task> tasks, LambdaConfigExtension extension) {
        SourceBlock source = extension.source;
        
        extension.function.each { function -> 
            tasks.create("updateLambdaFunction${function.name}", AWSLambdaUpdateFunctionCodeTask.class, {
                group = "Amazon Lambda Updater tasks"
                description = "update Lambda Function [${function.name}]"
                
                if(source.file != null) {
                    file = new File(source.file)
                } else {
                    s3File = new S3File()
                    s3File.bucketName = source.bucketName
                    s3File.key = source.key
                }
                
                functionName = function.name
            })
        }

        tasks.create("updateAllLambdaFunctions", {
            group = "Amazon Lambda Updater tasks"
            description = "update All Lambda Functions"
            dependsOn: extension.function.collect { function -> "updateLambdaFunction${function.name}" }
        })
    }
}
