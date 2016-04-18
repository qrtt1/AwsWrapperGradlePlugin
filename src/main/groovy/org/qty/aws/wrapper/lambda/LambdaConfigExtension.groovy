package org.qty.aws.wrapper.lambda

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.qty.aws.wrapper.lambda.LambdaFunction


class LambdaConfigExtension {

    private Project project;
    private SourceBlock source = new SourceBlock();
    private NamedDomainObjectContainer<LambdaFunction> function;

    public LambdaConfigExtension(Project project) {
        this.project = project;
        this.function = project.container(LambdaFunction.class, { name -> new LambdaFunction(name) });
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public SourceBlock getSource() {
        return source;
    }

    public void setSource(SourceBlock source) {
        this.source = source;
    }

    public void source(Closure<Void> configure){
        configure.setDelegate(source)
        configure.call(configure)
    }
    
    NamedDomainObjectContainer<LambdaFunction> getFunction() {
        return function;
    }

    def function(Action<? super NamedDomainObjectContainer<LambdaFunction>> action) {
        action.execute(function);
    }
}
