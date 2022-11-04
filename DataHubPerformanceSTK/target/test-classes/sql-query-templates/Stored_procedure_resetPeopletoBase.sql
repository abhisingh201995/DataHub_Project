CREATE OR REPLACE PROCEDURE `${projectId}.${tenantName}_custom.resetPeopletoBase`()
BEGIN

DECLARE originalRows INT64 DEFAULT 1;
DECLARE maxPersonID INT64 DEFAULT 0;

-- delete the preson numbers we previously injected
delete from `${projectId}.${tenantName}_detail.people` where personNumber like 'PN-%';

-- create the original base records that will be replicated
CREATE OR REPLACE TABLE `${projectId}.${tenantName}_custom.cs_personIDBase`
AS
select personID FROM `${projectId}.${tenantName}_detail.people`;

-- get the maxpersonId used to clean up records previous injected
EXECUTE IMMEDIATE "select (max(personId)), (count(personId)) from `${projectId}.${tenantName}_custom.cs_personIDBase`" INTO maxPersonID, originalRows;

-- clear people tables
delete from `${projectId}.${tenantName}_detail.peopleAccrualProfile` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleAge` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleApprover` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleBadge` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleBaseWage` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleCostCenter` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleCustomData` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleCustomDate` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleDataAccessGroup` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleEmail` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleEmploymentStatus` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleEmploymentStatusByDate` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleEmploymentTerm` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleExpectedHour` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleExternalIdentifiers` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleFingerScan` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleFTE` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleGroupAssignment` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleJobTransfer` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleLicense` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleOvertime` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peoplePayRule` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peoplePostalAddress` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peoplePrimaryJob` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleTelContact` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleTimeEntryMethod` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.peopleUserAccountStatus` where personId > maxPersonID;

-- clear timecard tables
delete from `${projectId}.${tenantName}_detail.timecardAudit` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardDurationPaycodeEdit` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardDurationPaycodeEditComment` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardException` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardExceptionComment` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardHistoricalCorrection` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardHolidayCredit` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardHolidayCreditComment` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardPayCodeEdit` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardPayCodeEditComment` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardPunch` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardPunchComment` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardTotal` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardTotalExcludeCorrection` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardTotalOnlyCorrection` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardWorkShift` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardWorkShiftScheduledShift` where personId > maxPersonID;
delete from `${projectId}.${tenantName}_detail.timecardWorkShiftSpan` where personId > maxPersonID;

END;