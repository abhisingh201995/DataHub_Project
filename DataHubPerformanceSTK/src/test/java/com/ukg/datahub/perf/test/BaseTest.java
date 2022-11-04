package com.ukg.datahub.perf.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.ukg.datahub.perf.listeners.PerfTestReporterListener;
import com.ukg.datahub.perf.utils.TestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Listeners({PerfTestReporterListener.class})
public abstract class BaseTest<T extends AbstractTestParameters> {
    private Class<T> clazz;
    private static ObjectMapper objectMapper;
    protected Logger logger = LogManager.getLogger(this.getClass());

    public BaseTest() {
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).
                getActualTypeArguments()[0];
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }


    /**
     * This method contains Tests Data provider implementation which have handling for testng.xml parameters and
     * Command line input parameters.
     *
     * @param iTestContext
     * @param testMethod
     * @return
     */
    @DataProvider(name = "testPayloadDataProvider")
    public Object[][] getPayloadDataProvider(ITestContext iTestContext, Method testMethod) {
        Object[][] dataArray;

        // Test input parameters from command line handling
        T[] testParametersFromCommandLine = getTestParametersFromCommandLine(testMethod);
        if (testParametersFromCommandLine != null) {
            dataArray = new Object[testParametersFromCommandLine.length][];
            int index = 0;
            for (T testParameter : testParametersFromCommandLine) {
                dataArray[index++] = new Object[]{testParameter};
            }
            return dataArray;
        }
        // Test input parameters from testng xml file handling
        Map<String, String> testNgParameters = iTestContext.getCurrentXmlTest().getAllParameters();
        Optional<ITestNGMethod> testNgMethod = Arrays.stream(iTestContext.getAllTestMethods())
                .filter(tm -> tm.getMethodName().equalsIgnoreCase(testMethod.getName()))
                .findAny();
        if (testNgMethod.isPresent()) {
            testNgParameters.put("testDescription", testNgMethod.get().getDescription());
        }

        String testId = System.getProperty("test-id");
        if (StringUtils.isNotEmpty(testId)) {
            testNgParameters.put("testId", testId);
        }

        AbstractTestParameters testParameters = objectMapper.convertValue(testNgParameters, getParameterizedTypeClazz());
        return new Object[][]{{testParameters}};
    }

    /**
     * This method read test method parameters from command line
     * input parse, validate them and return array of class objects.
     *
     * @param testMethod Test method which is invoked.
     * @return
     */
    private T[] getTestParametersFromCommandLine(Method testMethod) {
        String methodName = testMethod.getName();
        String className = testMethod.getDeclaringClass().getSimpleName();
        String testInput = System.getProperty(className + "." + methodName + ".input");
        if (StringUtils.isEmpty(testInput)) {
            return null;
        }

        try {
            Class<T[]> arrayClass = (Class<T[]>)
                    Class.forName("[L" + clazz.getName() + ";");

            T[] commandLineParams = objectMapper.readValue(testInput,
                    arrayClass);
            if (commandLineParams.length == 0) {
                return null;
            }
            for (T parameter : commandLineParams) {
                TestUtils.validateClassData(parameter);
            }
            return commandLineParams;
        } catch (ClassNotFoundException | IOException e) {
            logger.error(e);
        }
        return null;
    }

    private Class<T> getParameterizedTypeClazz() {
        return clazz;
    }

    /**
     * @param testParameters test input parameters
     * @param testOutput     test output parameters
     * @param testStatus     testStatus, true if success else false
     */
    protected void setTestOutput(T testParameters, Object testOutput, boolean testStatus) {
        ITestResult result = Reporter.getCurrentTestResult();
        String className = result.getMethod().getTestClass().getName();
        String methodName = result.getMethod().getMethodName();
        result.setAttribute(className + "." + methodName, testOutput);
        result.setAttribute(className + "." + methodName + ".testStatus", testStatus);
    }

    /**
     * This method will generate a unique 5 digit test Id
     *
     * @return
     */
    public String generateUniquetestId() {
        Random r = new Random(System.currentTimeMillis());
        return String.valueOf(10000 + r.nextInt(20000));
    }
}
