# Build Datahub test docker
FROM openjdk:8-jre

#Enviourment Variables
ENV WORKINGDIR=/opt/db-qa-tests

ENV baseUrl=https://us-datahub.eng02.kronosservices.net/datahub/admin/
ENV username=qa_superAdmin
ENV password=A1k9$s6i0
ENV browser=chrome
ENV browserMode=view
ENV GOOGLE_APPLICATION_CREDENTIALS=repd-e-eng-01-daeca4019a7d.json
ENV DB_USER=postgres
ENV DB_PASS=z0cjkmKrFikhJiFu
ENV DB_NAME=eng06
ENV CLOUD_SQL_CONNECTION_NAME=visionadmin
ENV Release=7.1.2
ENV codebase_branch=hotfix/7.1.2
ENV targetGcpDataProject=repd-d-eng-02

ENV RunnerXml=RunnerXml/Release_7_1_2.xml

RUN mkdir -p ${WORKINGDIR}
COPY ./repd-e-eng-01-daeca4019a7d.json ${WORKINGDIR}/repd-e-eng-01-daeca4019a7d.json
COPY ./target/DataHubTestAutomation-1.0-jar-with-dependencies.jar ${WORKINGDIR}/DataHubTestAutomation-1.0-jar-with-dependencies.jar
COPY ./RunnerXml ${WORKINGDIR}/RunnerXml
COPY ./TestExecutionReport ${WORKINGDIR}/TestExecutionReport
COPY ./TestScreenshots ${WORKINGDIR}/TestScreenshots
COPY ./run-tests.sh ${WORKINGDIR}/run-tests.sh


WORKDIR ${WORKINGDIR}

RUN ["chmod","+x","run-tests.sh"]
RUN ./run-tests.sh
