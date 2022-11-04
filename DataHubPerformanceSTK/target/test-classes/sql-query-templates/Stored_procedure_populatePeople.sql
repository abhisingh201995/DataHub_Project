CREATE OR REPLACE PROCEDURE `${projectId}.${tenantName}_custom.populatePeople`(rowsToGenerate INT64)
BEGIN

/*  Declare variables used in calculations and processing of Records */
-- DECLARE rowsToGenerate INT64 DEFAULT 10000;

DECLARE originalRows INT64 DEFAULT 1;
DECLARE batchesToProcess INT64 DEFAULT 1;
DECLARE batch INT64 DEFAULT 1;

DECLARE maxPersonID INT64 DEFAULT 0;
DECLARE personPrefix INT64 DEFAULT 10000;
DECLARE loopOffset INT64 DEFAULT 10000;

/*  Create custom temporary tables used during the processing for generation of new records */
CREATE TABLE IF NOT EXISTS `${projectId}.${tenantName}_custom.${personConvertUniqueName}`
( personID INT64 NOT NULL,
  newPersonID INT64 NOT NULL,
  newPersonNumber STRING
)OPTIONS(
    expiration_timestamp=TIMESTAMP "2023-01-01 00:00:00" );

truncate table `${projectId}.${tenantName}_custom.${personConvertUniqueName}`;

-- delete from `${projectId}.${tenantName}_detail.people` where personNumber like 'PN-%';

CREATE OR REPLACE TABLE `${projectId}.${tenantName}_custom.cs_personIDBase`
AS
select personID FROM `${projectId}.${tenantName}_detail.people`;

EXECUTE IMMEDIATE "select (max(personId)), (count(personId)) from `${projectId}.${tenantName}_custom.cs_personIDBase`" INTO maxPersonID, originalRows;

IF (maxPersonID < 10000) THEN
  SET personPrefix = 10000;
ELSEIF (maxPersonID < 50000) THEN
  SET personPrefix = 50000;
ELSEIF (maxPersonID < 100000) THEN
  SET personPrefix = 100000;
ELSEIF (maxPersonID < 1000000) THEN
  SET personPrefix = 1000000;
ELSE
  SET personPrefix = 10000000;
END IF;

SET loopOffset = personPrefix;

set batchesToProcess = Cast(trunc(rowsToGenerate/originalRows) as integer);

if batchesToProcess > 1300 THEN
set batchesToProcess = 1300;
end if;

LOOP
  IF batch > batchesToProcess THEN
    LEAVE;
  END IF;

  INSERT INTO `${projectId}.${tenantName}_custom.${personConvertUniqueName}`
  ( select personID, Cast((personPrefix + personID) AS INTEGER), Concat('PN-', Cast((personPrefix + personID) as string))
    FROM `${projectId}.${tenantName}_custom.cs_personIDBase`
  );

  SET batch = batch + 1;
  SET personPrefix = personPrefix + loopOffset;

END LOOP;

/*  Check Number of Records */
-- SELECT COUNT(personId) from `${projectId}.${tenantName}_custom.${personConvertUniqueName}`;

/*  Generate the new records */

delete from `${projectId}.${tenantName}_detail.people` where personId > maxPersonID;

insert into `${projectId}.${tenantName}_detail.people`
(
  SELECT  snapShotDate, exceptionPresentSwt, n.newPersonID as personId, n.newPersonNumber as personNumber, p.* except( snapShotDate, exceptionPresentSwt,personId, personNumber)
  FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
  JOIN `${projectId}.${tenantName}_detail.people` p on n.personID = p.personId
);

insert into `${projectId}.${tenantName}_detail.peopleAccrualProfile`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleAccrualProfile` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleAge`
(SELECT partitionDate,  n.newPersonId AS personId, p.* except(partitionDate, personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleAge` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleApprover`
(SELECT  n.newPersonId, n.newPersonNumber, p.* except(personId,personNumber)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleApprover` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleBadge`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleBadge` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleBaseWage`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleBaseWage` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleCostCenter`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleCostCenter` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleCustomData`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleCustomData` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleCustomDate`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleCustomDate` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleDataAccessGroup`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleDataAccessGroup` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleEmail`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleEmail` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleEmploymentStatus`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleEmploymentStatus` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleEmploymentStatusByDate`
(SELECT partitionDate , n.newPersonId,  p.* except(partitionDate,personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleEmploymentStatusByDate` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleEmploymentTerm`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleEmploymentTerm` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleExpectedHour`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleExpectedHour` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleExternalIdentifiers`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleExternalIdentifiers` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleFingerScan`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleFingerScan` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleFTE`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleFTE` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleGroupAssignment`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleGroupAssignment` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleJobTransfer`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleJobTransfer` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleLicense`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleLicense` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleOvertime`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleOvertime` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peoplePayRule`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peoplePayRule` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peoplePostalAddress`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peoplePostalAddress` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peoplePrimaryJob`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peoplePrimaryJob` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleTelContact`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleTelContact` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleTimeEntryMethod`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleTimeEntryMethod` p on n.personID = p.personId);

insert into `${projectId}.${tenantName}_detail.peopleUserAccountStatus`
(SELECT n.newPersonId, p.* except(personId)
FROM `${projectId}.${tenantName}_custom.${personConvertUniqueName}` n
JOIN `${projectId}.${tenantName}_detail.peopleUserAccountStatus` p on n.personID = p.personId);

END;