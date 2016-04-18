package org.qty.aws.wrapper.lambda;

public class LambdaFunction {

    private String name;

    public LambdaFunction() {
    }

    public LambdaFunction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
