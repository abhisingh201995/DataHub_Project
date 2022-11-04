# Build Datahub test docker
FROM openjdk:8-jre

#Enviourment Variables
ENV WORKINGDIR=/opt/db-qa-tests
RUN mkdir -p ${WORKINGDIR}
WORKDIR ${WORKINGDIR}


RUN ["chmod","+x","run-tests.sh"]
RUN ./test.sh $testSuitFileName $GCPDataProject