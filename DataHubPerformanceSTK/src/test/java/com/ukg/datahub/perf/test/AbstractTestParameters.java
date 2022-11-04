
package com.ukg.datahub.perf.test;

import javax.validation.constraints.NotEmpty;

/**
 * This class provides common parameters needed for performance test execution.
 */
public abstract class AbstractTestParameters {

    /**
     * The {@link String} test-id of test.
     */

    public String testId;

    /**
     * The {@link String} test description.
     */
    @NotEmpty(message = "testDescription can not be null/empty.")
    public String testDescription;

    /**
     * The {@link String} test assertions.
     */
    protected String assertions;

    public AbstractTestParameters() {
    }

    public AbstractTestParameters(String testId, String testDescription) {
        this.testId = testId;
        this.testDescription = testDescription;
    }

    /**
     * @return {@link AbstractTestParameters#testId}
     */
    public String getTestId() {
        return testId;
    }

    /**
     * @return {@link AbstractTestParameters#testDescription}
     */
    public String getTestDescription() {
        return testDescription;
    }

    /**
     * @return {@link AbstractTestParameters#assertions}}
     */
    public String getAssertions() {
        return assertions;
    }

    @Override
    public String toString() {
        return "AbstractTestParameters{" +
                "testId='" + testId + '\'' +
                ", testDescription='" + testDescription + '\'' +
                ", assertions='" + assertions + '\'' +
                '}';
    }
}