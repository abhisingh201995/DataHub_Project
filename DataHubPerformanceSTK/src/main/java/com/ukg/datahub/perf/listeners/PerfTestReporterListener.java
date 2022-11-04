
package com.ukg.datahub.perf.listeners;

import com.Utilities.GcpHelper;
import com.Utilities.Helper;
import com.ukg.datahub.perf.helpers.SystemValueHandler;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ITestResult;
import org.testng.internal.Utils;
import org.testng.reporters.EmailableReporter2;
import org.testng.reporters.RuntimeBehavior;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PerfTestReporterListener extends EmailableReporter2 {

    String dateTime = LocalDateTime.now().toString().replace(":", "").replace(".", "");
    String fileName = "Perf-test-Report-" + dateTime + ".html";
    protected Logger logger = LogManager.getLogger(this.getClass());

    private final StringBuilder buffer = new StringBuilder();

    @Override
    protected void writeSuiteSummary() {
        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        NumberFormat decimalFormat = NumberFormat.getNumberInstance();
        int totalPassedTests = 0;
        int totalSkippedTests = 0;
        int totalFailedTests = 0;
        int totalRetriedTests = 0;
        long totalDuration = 0L;
        this.writer.println("<table>");
        this.writer.print("<tr>");
        this.writer.print("<th>Test</th>");
        this.writer.print("<th># Passed</th>");
        this.writer.print("<th># Skipped</th>");
        this.writer.print("<th># Failed</th>");
        this.writer.print("<th>Time</th>");
        this.writer.print("<th>Included Groups</th>");
        this.writer.print("<th>Excluded Groups</th>");
        this.writer.println("</tr>");
        int testIndex = 0;
        Iterator var10 = this.suiteResults.iterator();

        while (var10.hasNext()) {
            SuiteResult suiteResult = (SuiteResult) var10.next();
            this.writer.print("<tr><th colspan=\"8\">");
            this.writer.print(Utils.escapeHtml(suiteResult.getSuiteName()));
            this.writer.println("</th></tr>");

            for (Iterator var12 = suiteResult.getTestResults().iterator(); var12.hasNext(); ++testIndex) {
                TestResult testResult = (TestResult) var12.next();
                int passedTests = testResult.getPassedTestCount();
                int skippedTests = testResult.getSkippedTestCount();
                int failedTests = testResult.getFailedTestCount();
                long duration = testResult.getDuration();
                this.writer.print("<tr");
                if (testIndex % 2 == 1) {
                    this.writer.print(" class=\"stripe\"");
                }

                this.writer.print(">");
                this.buffer.setLength(0);
                this.writeTableData(this.buffer.append("<a href=\"#t").append(testIndex).append("\">").append(Utils.escapeHtml(testResult.getTestName())).append("</a>").toString());
                this.writeTableData(integerFormat.format((long) passedTests), "num");
                this.writeTableData(integerFormat.format((long) skippedTests), skippedTests > 0 ? "num attn" : "num");
                this.writeTableData(integerFormat.format((long) failedTests), failedTests > 0 ? "num attn" : "num");
                this.writeTableData(DurationFormatUtils.formatDurationHMS(duration), "num");
                this.writeTableData(testResult.getIncludedGroups());
                this.writeTableData(testResult.getExcludedGroups());
                this.writer.println("</tr>");
                totalPassedTests += passedTests;
                totalSkippedTests += skippedTests;
                totalFailedTests += failedTests;
                totalDuration += duration;
            }

           /* boolean testsInParallel = XmlSuite.ParallelMode.TESTS.equals(suiteResult.());
            if (testsInParallel) {
                Optional<TestResult> maxValue = suiteResult.getTestResults().stream().max(Comparator.comparing(EmailableReporter2.TestResult::getDuration));
                if (maxValue.isPresent()) {
                    totalDuration = Math.max(totalDuration, maxValue.get().getDuration());
                }
            }*/
        }

        if (testIndex > 1) {
            this.writer.print("<tr>");
            this.writer.print("<th>Total</th>");
            this.writeTableHeader(integerFormat.format(totalPassedTests), "num");
            this.writeTableHeader(integerFormat.format(totalSkippedTests), totalSkippedTests > 0 ? "num attn" : "num");
            this.writeTableHeader(integerFormat.format(totalRetriedTests), totalRetriedTests > 0 ? "num attn" : "num");
            this.writeTableHeader(integerFormat.format(totalFailedTests), totalFailedTests > 0 ? "num attn" : "num");
            this.writeTableHeader(DurationFormatUtils.formatDurationHMS(totalDuration), "num");
            this.writer.print("<th colspan=\"2\"></th>");
            this.writer.println("</tr>");
        }

        this.writer.println("</table>");
    }

    @Override
    protected void writeScenarioSummary() {
        this.writer.print("<table id='summary'>");
        this.writer.print("<thead>");
        this.writer.print("<tr>");
        this.writer.print("<th>Class</th>");
        this.writer.print("<th>Method</th>");
        this.writer.print("<th>Start</th>");
        this.writer.print("<th>Time</th>");
        this.writer.print("</tr>");
        this.writer.print("</thead>");
        int testIndex = 0;
        int scenarioIndex = 0;
        Iterator var3 = this.suiteResults.iterator();

        while (var3.hasNext()) {
            SuiteResult suiteResult = (SuiteResult) var3.next();
            this.writer.print("<tbody><tr><th colspan=\"4\">");
            this.writer.print(Utils.escapeHtml(suiteResult.getSuiteName()));
            this.writer.print("</th></tr></tbody>");

            for (Iterator var5 = suiteResult.getTestResults().iterator(); var5.hasNext(); ++testIndex) {
                TestResult testResult = (TestResult) var5.next();
                this.writer.printf("<tbody id=\"t%d\">", testIndex);
                String testName = Utils.escapeHtml(testResult.getTestName());
                int startIndex = scenarioIndex;
                scenarioIndex += this.writeScenarioSummary(testName + " &#8212; failed (configuration methods)", testResult.getFailedConfigurationResults(), "failed", scenarioIndex);
                scenarioIndex += this.writeScenarioSummary(testName + " &#8212; failed", testResult.getFailedTestResults(), "failed", scenarioIndex);
                scenarioIndex += this.writeScenarioSummary(testName + " &#8212; skipped (configuration methods)", testResult.getSkippedConfigurationResults(), "skipped", scenarioIndex);
                scenarioIndex += this.writeScenarioSummary(testName + " &#8212; skipped", testResult.getSkippedTestResults(), "skipped", scenarioIndex);
                scenarioIndex += this.writeScenarioSummary(testName + " &#8212; passed", testResult.getPassedTestResults(), "passed", scenarioIndex);
                if (scenarioIndex == startIndex) {
                    this.writer.print("<tr><th colspan=\"4\" class=\"invisible\"/></tr>");
                }

                this.writer.println("</tbody>");
            }
        }

        this.writer.println("</table>");
    }

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        try {
            outputDirectory = System.getProperty("user.dir") + "\\" + "test-output";
            this.writer = this.createWriter(outputDirectory);
        } catch (Exception e) {
            logger.error("Report generation Failed");
            return;
        }

        Iterator var4 = suites.iterator();

        while (var4.hasNext()) {
            ISuite suite = (ISuite) var4.next();
            this.suiteResults.add(new EmailableReporter2.SuiteResult(suite));
        }

        this.writeDocumentStart();
        this.writeHead();
        this.writeBody();
        this.writeDocumentEnd();
        this.writer.close();

        GcpHelper gcpHelper = new GcpHelper("repd-e-eng-01");
        gcpHelper.uploadObject(SystemValueHandler.fetchExecutionProperty("testResultBucket"), fileName, outputDirectory + "\\" + fileName);
        String gcpFileLoc = "https://storage.cloud.google.com/qa-performance-test-report/" + System.getProperty("ReportFileName");

        String message = "{'text':'Automated Test Executed successfully,view detailed report on " + gcpFileLoc + "'}";
        logger.info(message);
        //Sending file details on MS Teams
        new Helper("repd-e-eng-01").sendMessageOnTeams(message);
    }

    protected PrintWriter createWriter(String outdir) throws IOException {
        (new File(outdir)).mkdirs();
        String jvmArg = RuntimeBehavior.getDefaultEmailableReport2Name();
        if (jvmArg != null && !jvmArg.trim().isEmpty()) {
            this.fileName = jvmArg;
        }

        System.setProperty("ReportFileName", fileName);
        System.setProperty("OutputDir", outdir);
        return new PrintWriter(Files.newBufferedWriter((new File(outdir, this.fileName)).toPath(), StandardCharsets.UTF_8));
    }

    private int writeScenarioSummary(String description, List<ClassResult> classResults, String cssClassPrefix, int startingScenarioIndex) {
        int scenarioCount = 0;
        if (!classResults.isEmpty()) {
            this.writer.print("<tr><th colspan=\"4\">");
            this.writer.print(description);
            this.writer.print("</th></tr>");
            int scenarioIndex = startingScenarioIndex;
            int classIndex = 0;

            for (Iterator var8 = classResults.iterator(); var8.hasNext(); ++classIndex) {
                ClassResult classResult = (ClassResult) var8.next();
                String cssClass = cssClassPrefix + (classIndex % 2 == 0 ? "even" : "odd");
                this.buffer.setLength(0);
                int scenariosPerClass = 0;
                int methodIndex = 0;

                for (Iterator var13 = classResult.getMethodResults().iterator(); var13.hasNext(); ++methodIndex) {
                    MethodResult methodResult = (MethodResult) var13.next();
                    List<ITestResult> results = methodResult.getResults();
                    int resultsCount = results.size();

                    assert resultsCount > 0;

                    ITestResult firstResult = results.iterator().next();
                    String methodName = Utils.escapeHtml(firstResult.getMethod().getMethodName());
                    long start = firstResult.getStartMillis();
                    long duration = firstResult.getEndMillis() - start;
                    if (duration < 0) {
                        duration = 0;
                    }
                    if (methodIndex > 0) {
                        this.buffer.append("<tr class=\"").append(cssClass).append("\">");
                    }

                    this.buffer.append("<td><a href=\"#m").append(scenarioIndex).append("\">")
                            .append(methodName).append("</a></td>")
                            .append("<td rowspan=\"").append(resultsCount).append("\">")
                            .append(new Date(start)).append("</td>")
                            .append("<td rowspan=\"").append(resultsCount).append("\">")
                            .append(DurationFormatUtils.formatDurationHMS(duration)).append("</td></tr>");
                    ++scenarioIndex;

                    for (int i = 1; i < resultsCount; ++i) {
                        this.buffer.append("<tr class=\"").append(cssClass).append("\">").append("<td><a href=\"#m").append(scenarioIndex).append("\">").append(methodName).append("</a></td></tr>");
                        ++scenarioIndex;
                    }

                    scenariosPerClass += resultsCount;
                }

                this.writer.print("<tr class=\"");
                this.writer.print(cssClass);
                this.writer.print("\">");
                this.writer.print("<td rowspan=\"");
                this.writer.print(scenariosPerClass);
                this.writer.print("\">");
                this.writer.print(Utils.escapeHtml(classResult.getClassName()));
                this.writer.print("</td>");
                this.writer.print(this.buffer);
            }

            scenarioCount = scenarioIndex - startingScenarioIndex;
        }
        return scenarioCount;
    }
}
