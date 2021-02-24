### -- ---------------------------------------------------------------------------------------------------------
### -- Script Name		: MOSIP ALL DB Artifacts deployment script
### -- Deploy Module 	: MOSIP Admin Services Module
### -- Purpose    		: To deploy MOSIP Admin Services Modules Database DB Artifacts.       
### -- Create By   		: Ram Bhatt
### -- Created Date		: Feb-2021
### -- 
### -- Modified Date        Modified By         Comments / Remarks
### -- -----------------------------------------------------------------------------------------------------------

#! bin/bash
echo "`date` : You logged on to DB deplyment server as : `whoami`"
echo "`date` : MOSIP Database objects deployment started...."

echo "=============================================================================================================="
bash ./mosip_hotlist/mosip_hotlist_db_deploy.sh ./mosip_ida/mosip_hotlist_deploy.properties
echo "=============================================================================================================="

echo "`date` : MOSIP DB Deployment for Admin Services databases is completed, Please check the logs at respective logs directory for more information"
 
