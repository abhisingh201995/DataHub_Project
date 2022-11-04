CREATE OR REPLACE PROCEDURE `${projectId}.${tenantName}_custom.populateTimeCard`()
BEGIN

insert into `${projectId}.${tenantName}_detail.timecardAudit`
(SELECT partitionDate, n.newPersonID, p.* except( partitionDate, personId)
  FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
  JOIN `${projectId}.${tenantName}_detail.timecardAudit` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.timecardDurationPaycodeEdit`
(SELECT hourWorkedId, partitionDate,  n.newPersonID AS personId, p.* except(hourWorkedId, partitionDate, personId)
  FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
  JOIN `${projectId}.${tenantName}_detail.timecardDurationPaycodeEdit` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.timecardDurationPaycodeEditComment`
(SELECT hourWorkedId, partitionDate,  n.newPersonID AS personId,p.* except(hourWorkedId, partitionDate, personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardDurationPaycodeEditComment` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.timecardException`
(SELECT exceptionId, partitionDate,  n.newPersonID AS personId, p.* except(exceptionId , partitionDate, personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardException` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.timecardExceptionComment`
(SELECT exceptionId, partitionDate,  n.newPersonID AS personId, p.* except(exceptionId , partitionDate, personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardExceptionComment` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.timecardHistoricalCorrection`
(SELECT historicalCorrectionId, partitionDate, originalDate,  n.newPersonID AS personId, p.* except( historicalCorrectionId, partitionDate, originalDate, personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardHistoricalCorrection` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.timecardHolidayCredit`
(SELECT holidayCreditId, partitionDate,  n.newPersonID AS personId, p.* except(holidayCreditId, partitionDate, personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardHolidayCredit` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.timecardHolidayCreditComment`
(SELECT holidayCreditId, partitionDate,  n.newPersonID AS personId, p.* except(holidayCreditId, partitionDate, personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardHolidayCreditComment` p on n.personID = p.personId);


insert into `${projectId}.${tenantName}_detail.timecardPayCodeEdit`
(SELECT payCodeEditId, itemId, partitionDate,  n.newPersonID AS personId, p.* except(payCodeEditId, itemId, partitionDate, personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardPayCodeEdit` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.timecardPayCodeEditComment`
(SELECT payCodeEditId, partitionDate,  n.newPersonID AS personId, p.* except(payCodeEditId, partitionDate, personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardPayCodeEditComment` p on n.personID = p.personId);


insert into `${projectId}.${tenantName}_detail.timecardPunch`
(SELECT punchId, partitionDate,  n.newPersonID AS personId, p.* except( punchId, partitionDate, personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardPunch` p on n.personID = p.personId);


insert into `${projectId}.${tenantName}_detail.timecardPunchComment`
(SELECT punchId, partitionDate,  n.newPersonID AS personId, p.* except( punchId, partitionDate, personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardPunchComment` p on n.personID = p.personId);


insert into `${projectId}.${tenantName}_detail.timecardTotal`
(SELECT  n.newPersonID AS personId, partitionDate, p.* except(personId, partitionDate)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardTotal` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.timecardTotalExcludeCorrection`
(SELECT  n.newPersonID AS personId, partitionDate, p.* except(personId, partitionDate)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardTotalExcludeCorrection` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.timecardTotalOnlyCorrection`
(SELECT  n.newPersonID AS personId, partitionDate, p.* except(personId, partitionDate)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardTotalOnlyCorrection` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.timecardWorkShift`
(SELECT workedShiftId, partitionDate ,  n.newPersonID AS personId,  p.* except(workedShiftId, partitionDate,personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardWorkShift` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.timecardWorkShiftScheduledShift`
(SELECT workedShiftId, partitionDate ,  n.newPersonID AS personId,  p.* except(workedShiftId, partitionDate,personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardWorkShiftScheduledShift` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.timecardWorkShiftSpan`
(SELECT workedShiftId, partitionDate ,  n.newPersonID AS personId,  p.* except(workedShiftId, partitionDate,personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.timecardWorkShiftSpan` p on n.personID = p.personId);

END;