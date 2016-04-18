package org.qty.aws.wrapper.lambda;

import org.gradle.api.NamedDomainObjectFactory;

public class FunctionElementFactory implements NamedDomainObjectFactory<LambdaFunction>{

    @Override
    public LambdaFunction create(String name) {
        return new LambdaFunction(name);
    }

}
