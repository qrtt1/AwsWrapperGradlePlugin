package org.qty.aws.wrapper.lambda

import org.gradle.testkit.runner.GradleRunner

import org.junit.Rule
import org.junit.rules.TemporaryFolder

import static org.gradle.testkit.runner.TaskOutcome.*
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

import org.gradle.api.Project
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.UnexpectedBuildFailure;
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import com.google.gson.Gson;

import spock.lang.Specification

class AwsWrapperPluginTest extends Specification {

    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    
    def setup() {
        PluginInstaller.install();
        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << """
            buildscript {
                repositories {
                    jcenter()
                }
                dependencies {
                    classpath 'com.amazonaws:aws-java-sdk-lambda:1.+'
                    classpath 'com.amazonaws:aws-java-sdk-s3:1.10.8'
                }
            
                repositories {
                    mavenLocal()
                }
                dependencies {
                    classpath "org.qty.gradle.aws.lambda:lambda.wrapper.plugin:0.0.1-SNAPSHOT"
                }
            }
            apply plugin: "org.qty.aws.wrapper.lambda"
        """
    }
    
    def testLambdaConfigSyntax() {
        given:
            buildFile << """
                lambdaConfig {
                    source {
                        bucketName = 'abc'
                        key = 'def.zip'
                    }
                    function {
                        Function1
                        Function2
                    }
                }
            """

        when:
            def result = invokeGradle()
            
        then:
            result.task(":tasks").outcome == SUCCESS
            result.output.contains("updateLambdaFunctionFunction1")
            result.output.contains("updateLambdaFunctionFunction2")
    }
    
    def testWrongSourceSettings() {
        given:
            buildFile << """
                lambdaConfig {
                    source {
                        bucketName = 'abc'
                        key = 'def.zip'

                        /* 
                          source settings should be either local-file or s3-file.
                        */

                        file = 'a_local_file.zip'
                    }
                }
            """

        when:
            invokeGradle()
            
        then:
            thrown UnexpectedBuildFailure
    }
    
    def testWrongSourceSettingsPartialS3BucketOnly() {
        given:
            buildFile << """
                lambdaConfig {
                    source {
                        bucketName = 'def.zip'
                    }
                }
            """
        when:
            invokeGradle()
            
        then:
            thrown UnexpectedBuildFailure
    }
    
    def testWrongSourceSettingsPartialS3KeyOnly() {
        given:
            buildFile << """
                lambdaConfig {
                    source {
                        key = 'def.zip'
                    }
                }
            """
        when:
            invokeGradle()
            
        then:
            thrown UnexpectedBuildFailure
    }
    
    def invokeGradle() {
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("tasks")
            .build()
        return result
    }
}
