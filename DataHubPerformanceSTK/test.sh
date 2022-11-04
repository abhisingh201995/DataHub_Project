echo "************STarting QE Tests***************"
java -Dgcp-data-project="$2" -cp ".;DataHubPerformanceSTK-1.0-SNAPSHOT-jar-with-dependencies.jar" org.testng.TestNG $1