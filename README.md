
# Installation

Setup the plugin maven repository

```
buildscript {
    repositories {
        jcenter()
        maven {
            url  "http://dl.bintray.com/qrtt1/maven"
        }
    }

    dependencies {
        classpath "org.qty.gradle.aws.lambda:lambda.wrapper.plugin:0.0.+"
    }
}
```

Apply our plugin

```groovy
apply plugin: "org.qty.aws.wrapper.lambda"
```


