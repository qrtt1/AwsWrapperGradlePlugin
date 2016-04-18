package org.qty.aws.wrapper.lambda

import org.gradle.testkit.runner.GradleRunner

import org.junit.Rule
import org.junit.rules.TemporaryFolder

import static org.gradle.testkit.runner.TaskOutcome.*
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

import org.gradle.api.Project
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

class AwsWrapperPluginTest extends Specification {

    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile

    def setup() {
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
    
    def testLambdaConfig() {
        given:
        buildFile << """
            lambdaConfig {
                source {
                    bucketName = 'abc'
                    key = 'def.zip'
                    // OR
                    file = 'a_local_file.zip'
                }
                function {
                    Function1
                    Function2
                }
            }
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("tasks")
            .build()

        then:
        println result.output
        result.task(":tasks").outcome == SUCCESS
        
    }
}
