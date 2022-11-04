
package com.ukg.datahub.perf.reporting;

import com.ukg.datahub.perf.listeners.PerfTestReporterListener;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;

public class PerformanceReport {

    private static final String PR_PROP_TEST_PARAMETER = "testParameters";
    private static final String PR_PROP_OUTPUTS = "stepOutputs";
    private static final String PR_PROP_TEST_RESULT = "result";
    private static final String ERROR_MESSAGE = "error";
    private static final Logger log = LogManager.getLogger(PerformanceReport.class);

    // Reporting templates
    private static final String REPORT_TEMPLATE_PATH = "/ftl";
    private static final String REPORT_TEMPLATE_HTML = "TestReport.ftl";

    private String errorMessage;
    private TestResult result;
    private String owners ;
    private Map<String, Object> properties;
    private Map<String, List<Object>> parameters;
    private Map<String, Object> outputs;
    private Object testMetrics;
    private String templateName;
    private Template htmlTemplate;
    private String outputLocation = "Output";

    /**
     * Constructor.
     */
    public PerformanceReport() {

        properties = new LinkedHashMap<>();
        parameters = new LinkedHashMap<>();
        outputs = new LinkedHashMap<>();

    }

    /**
     * Sets if the test has passed or failed.
     *
     * @param passed true if test passed, false otherwise.
     * @return the {@link PerformanceReport}} object itself.
     */
    public PerformanceReport setPassed(boolean passed) {

        this.result = passed ? TestResult.PASSED : TestResult.FAILED;
        return this;

    }

    public PerformanceReport setError(String errorMessage){
        this.errorMessage = errorMessage;
        return this;
    }

    /**
     * Set the owners of the tests.
     * <p>
     * If the owners is not empty, calling this method will trigger the delivery of the report by email using
     * the owners as recipients.
     *
     * @param owners a comma separated list of email addresses.
     * @return the {@link PerformanceReport}} object itself.
     */
    public PerformanceReport setOwners(String owners) {

        this.owners = owners;
        return this;

    }

    /**
     * Add a property to the report.
     *
     * @param property the property property.
     * @param value    the property value.
     * @param <T>      the type of the value.
     * @return the {@link PerformanceReport}} object itself.
     */
    public <T> PerformanceReport addProperty(TestProperties property, T value) {
        if (property != null) {
            addProperty(property.getValue(), value);
        }
        return this;
    }

    /**
     * Add a parameter.
     *
     * @param property the name of the parameter to add.
     * @param values   one or more values associated with the parameter.
     * @param <T>      the value type.
     * @return the {@link PerformanceReport}} object itself.
     */
    public <T> PerformanceReport addParameter(TestProperties property, T... values) {
        if (property != null) {
            addProperty(property.getValue(), values);
        }
        return this;
    }

    /**
     * Add a property to the report.
     *
     * @param property the property property.
     * @param value    the property value.
     * @param <T>      the type of the value.
     * @return the {@link PerformanceReport}} object itself.
     */
    public <T> PerformanceReport addProperty(String property, T value) {
        if (StringUtils.isNotEmpty(property)) {
            properties.put(property, value);
        }
        return this;
    }

    /**
     * Add a parameter.
     *
     * @param property the name of the parameter to add.
     * @param values   one or more values associated with the parameter.
     * @param <T>      the value type.
     * @return the {@link PerformanceReport}} object itself.
     */
    public <T> PerformanceReport addParameter(String property, T... values) {
        if (StringUtils.isNotEmpty(property)) {
            parameters.put(property, Arrays.asList(values));
        }
        return this;
    }

    /**
     * Add a parameter representing a number of bytes (file size, memory usage, ...).
     *
     * @param name  the name of the parameter to add.
     * @param value a size in bytes.
     * @return the {@link PerformanceReport}} object itself.
     */
    public PerformanceReport addBytesParameter(String name, long value) {

        parameters.put(name, Arrays.asList(new String[]{bytesSizeToString(value)}));
        return this;

    }

    /**
     * Add an output value.
     *
     * @param name  name of the output value.
     * @param value the value itself.
     * @param <T>   the type of the value.
     * @return the {@link PerformanceReport}} object itself.
     */
    public <T> PerformanceReport addOutput(String name, T value) {

        if (value instanceof Duration) {
            // freemarker templates are not able to properly format a duration, we are applying the formatting now.
            outputs.put(name, durationToString((Duration) value));
        } else {
            outputs.put(name, value);
        }
        return this;

    }

    /**
     * Adding an output only if test passed.
     * <p>
     * This method uses a {@link Supplier} to access the value to be added and the supplier will be used only
     * if the test passed. If the test failed, the output will not appears in the report (neither the name nor the
     * value).
     * <p>
     * The method {@link PerformanceReport#setPassed(boolean)} must be called before calling this method.
     *
     * @param name     the name of the output value.
     * @param supplier a supplier of the value that will be invoked only if the test passed.
     * @param <T>      the type of the value.
     * @return the {@link PerformanceReport} object itself.
     */
    public <T> PerformanceReport addOutputIfPassed(String name, Supplier<T> supplier) {

        if (result.equals(TestResult.PASSED)) {
            return addOutput(name, supplier.get());
        } else {
            return this;
        }

    }

    public <T> PerformanceReport addOutputData(Map<String, T> outputData) {
        if (outputData == null)
            return this;

        for (Map.Entry<String, T> entry : outputData.entrySet()) {
            addOutput(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Set the test metrics value.
     *
     * @param testMetrics
     * @return the {@link PerformanceReport}} object itself.
     */
    public <T> PerformanceReport setTestMetrics(Object testMetrics) {
        this.testMetrics = testMetrics;
        return this;
    }

    public <T> PerformanceReport template(String templateName) {
        this.templateName = templateName;
        return this;
    }

    public <T> PerformanceReport outputLocation(String outputLocation) {
        this.outputLocation = outputLocation;
        return this;
    }

    public TestResult getResult() {
        return result;
    }

    public Object getMetrics() {
        return testMetrics;
    }

    /**
     * Publication of the report.
     * <p>
     * If {@link PerformanceReport#owners} is not empty, the report will be delivered by email to the owners,
     * otherwise the report will be saved as a temporary file and the path of the path logged on the terminal.
     *
     * @return An HTML representation string of the published performance report.
     */
    public String publish() {
        String report = getPerformanceReportHtml();
        String publishReport = "TRUE";

        if (Boolean.valueOf(publishReport) && StringUtils.isNotEmpty(owners)) {
            //publishByEmail(report);
        }
        if (StringUtils.isNotEmpty(outputLocation) || StringUtils.isEmpty(owners)) {
            publishLocally(report);
        }
        return report;
    }

    public String getPerformanceReportHtml() {
        HashMap<String, Object> model = new HashMap<>(properties);
        model.put(PR_PROP_TEST_RESULT, result);
        model.put(PR_PROP_TEST_PARAMETER, parameters);
        model.put(PR_PROP_OUTPUTS, outputs);
        model.put(ERROR_MESSAGE,errorMessage);
        return processReportTemplate(model);
    }

   /* *//**
     * @param html an HTML representation of the report.
     *//*
    private void publishByEmail(String html) {
        String testId = properties.get(TestProperties.PR_PROP_TEST_ID.getValue()).toString();

        EmailNotificationHelper notifier = new EmailNotificationHelper(html, owners, "Test Results - " + testId);
        notifier.sendEmail();
    }*/

    /**
     * Publish the report as a file.
     *
     * @param html an HTML representation of the report.
     */

    private void publishLocally(String html) {

        String testId = properties.get(TestProperties.PR_PROP_TEST_ID.getValue()).toString();
        String outputPath = StringUtils.isEmpty(outputLocation) ? "" : outputLocation + File.separator;
        try {
            File file = File.createTempFile(outputPath + "DataHub-report-" + testId, ".html");
            Files.write(file.toPath(), html.getBytes(StandardCharsets.UTF_8));
            System.out.println(("TEST REPORT #{}: {}" + file.getAbsolutePath()));
        } catch (IOException e) {
            log.error("Unable to save the report locally (reason={}).", e.getMessage());
        }
    }

    /**
     * Rendering of the report using the given context.
     *
     * @param model all variables used by the template.
     * @return An HTML representation of the performances report.
     */
    private String processReportTemplate(Map<String, Object> model) {

        try {
            StringWriter stringWriter = new StringWriter();
            getHtmlTemplate().process(model, stringWriter);
            return stringWriter.toString();
        } catch (TemplateException | IOException e) {
            String error = String.format("Report generation failed: %s.", e.getMessage());
            log.error(error);
            System.out.println(error);
            return error;
        }
    }

    /**
     * Load the report template from the resources.
     *
     * @return The loaded template.
     * @throws IOException
     */
    private synchronized Template getHtmlTemplate() throws IOException {

        if (htmlTemplate == null) {
            String templateToUse = StringUtils.isEmpty(templateName) ? REPORT_TEMPLATE_HTML : templateName;
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
            configuration.setClassForTemplateLoading(this.getClass(), REPORT_TEMPLATE_PATH);
            htmlTemplate = configuration.getTemplate(templateToUse);
        }
        return htmlTemplate;

    }

    /**
     * Converts a size in bytes to a human readable string.
     * <p>
     * RESULT
     * <p>
     * 0:        0 B
     * 27:       27 B
     * 999:      999 B
     * 1000:     1000 B
     * 1023:     1023 B
     * 1024:    1.0 KiB
     * 1728:    1.7 KiB
     * 110592:  108.0 KiB
     * 7077888:    6.8 MiB
     * 452984832:  432.0 MiB
     * 28991029248:   27.0 GiB
     * 1855425871872:    1.7 TiB
     * 9223372036854775807:    8.0 EiB   (Long.MAX_VALUE)
     *
     * @param bytes a size in bytes.
     * @return a string representation of the size using the binary units (1K = 1024).
     * @see "https://programming.guide/java/formatting-byte-size-to-human-readable-format.html"
     */
    private static String bytesSizeToString(long bytes) {

        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());

    }

    /**
     * Formats a duration as a string.
     *
     * @param value the duration to format.
     * @return A string representation of the duration such as '1h 30m 27s".
     */
    private static String durationToString(Duration value) {

        StringBuilder sb = new StringBuilder();
        if (value.toHours() > 0) {
            sb.append(value.toHours()).append("h ");
        }
        if (value.toMinutes() % 60 > 0) {
            sb.append(value.toMinutes() % 60).append("m ");
        }
        if (value.getSeconds() % 60 > 0) {
            sb.append(value.getSeconds() % 60).append("s ");
        }
        if (value.getSeconds() < 60 && value.getNano() / 1000 > 0) {
            sb.append(value.getNano() / 1000).append("ms");
        }
        return sb.toString();

    }

    @Override
    public String toString() {
        return "PerformanceReport [result=" + result + ", owners=" + owners + ", properties=" + properties
                + ", parameters=" + parameters + ", testMetrics=" + testMetrics + ", outputs=" + outputs + ", templateName=" + templateName
                + ", htmlTemplate=" + htmlTemplate + ", outputLocation=" + outputLocation + "]";
    }
}
