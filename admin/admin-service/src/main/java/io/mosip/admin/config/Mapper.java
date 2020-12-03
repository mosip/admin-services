package io.mosip.admin.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.mosip.admin.bulkdataupload.entity.ApplicantValidDocument;
import io.mosip.admin.bulkdataupload.entity.Application;
import io.mosip.admin.bulkdataupload.entity.BiometricAttribute;
import io.mosip.admin.bulkdataupload.entity.BiometricType;
import io.mosip.admin.bulkdataupload.entity.BlacklistedWords;
import io.mosip.admin.bulkdataupload.entity.DaysOfWeek;
import io.mosip.admin.bulkdataupload.entity.Device;
import io.mosip.admin.bulkdataupload.entity.DeviceHistory;
import io.mosip.admin.bulkdataupload.entity.DeviceProviderHistory;
import io.mosip.admin.bulkdataupload.entity.DeviceRegister;
import io.mosip.admin.bulkdataupload.entity.DeviceRegisterHistory;
import io.mosip.admin.bulkdataupload.entity.DeviceSpecification;
import io.mosip.admin.bulkdataupload.entity.DeviceType;
import io.mosip.admin.bulkdataupload.entity.DocumentCategory;
import io.mosip.admin.bulkdataupload.entity.DocumentType;
import io.mosip.admin.bulkdataupload.entity.DynamicField;
import io.mosip.admin.bulkdataupload.entity.ExceptionalHoliday;
import io.mosip.admin.bulkdataupload.entity.FoundationalTrustProvider;
import io.mosip.admin.bulkdataupload.entity.FoundationalTrustProviderHistory;
import io.mosip.admin.bulkdataupload.entity.Gender;
import io.mosip.admin.bulkdataupload.entity.Holiday;
import io.mosip.admin.bulkdataupload.entity.IdType;
import io.mosip.admin.bulkdataupload.entity.IdentitySchema;
import io.mosip.admin.bulkdataupload.entity.IndividualType;
import io.mosip.admin.bulkdataupload.entity.Language;
import io.mosip.admin.bulkdataupload.entity.Location;
import io.mosip.admin.bulkdataupload.entity.LocationHierarchy;
import io.mosip.admin.bulkdataupload.entity.MOSIPDeviceService;
import io.mosip.admin.bulkdataupload.entity.MOSIPDeviceServiceHistory;
import io.mosip.admin.bulkdataupload.entity.Machine;
import io.mosip.admin.bulkdataupload.entity.MachineHistory;
import io.mosip.admin.bulkdataupload.entity.MachineSpecification;
import io.mosip.admin.bulkdataupload.entity.MachineType;
import io.mosip.admin.bulkdataupload.entity.ModuleDetail;
import io.mosip.admin.bulkdataupload.entity.ReasonCategory;
import io.mosip.admin.bulkdataupload.entity.ReasonList;
import io.mosip.admin.bulkdataupload.entity.RegExceptionalHoliday;
import io.mosip.admin.bulkdataupload.entity.RegWorkingNonWorking;
import io.mosip.admin.bulkdataupload.entity.RegisteredDevice;
import io.mosip.admin.bulkdataupload.entity.RegisteredDeviceHistory;
import io.mosip.admin.bulkdataupload.entity.RegistrationCenter;
import io.mosip.admin.bulkdataupload.entity.RegistrationCenterDevice;
import io.mosip.admin.bulkdataupload.entity.RegistrationCenterDeviceHistory;
import io.mosip.admin.bulkdataupload.entity.RegistrationCenterHistory;
import io.mosip.admin.bulkdataupload.entity.RegistrationCenterMachine;
import io.mosip.admin.bulkdataupload.entity.RegistrationCenterMachineDevice;
import io.mosip.admin.bulkdataupload.entity.RegistrationCenterMachineDeviceHistory;
import io.mosip.admin.bulkdataupload.entity.RegistrationCenterMachineHistory;
import io.mosip.admin.bulkdataupload.entity.RegistrationCenterType;
import io.mosip.admin.bulkdataupload.entity.RegistrationCenterUser;
import io.mosip.admin.bulkdataupload.entity.RegistrationCenterUserHistory;
import io.mosip.admin.bulkdataupload.entity.RegistrationCenterUserMachine;
import io.mosip.admin.bulkdataupload.entity.RegistrationCenterUserMachineHistory;
import io.mosip.admin.bulkdataupload.entity.RegistrationDeviceSubType;
import io.mosip.admin.bulkdataupload.entity.RegistrationDeviceType;
import io.mosip.admin.bulkdataupload.entity.SchemaDefinition;
import io.mosip.admin.bulkdataupload.entity.Template;
import io.mosip.admin.bulkdataupload.entity.TemplateFileFormat;
import io.mosip.admin.bulkdataupload.entity.TemplateType;
import io.mosip.admin.bulkdataupload.entity.Title;
import io.mosip.admin.bulkdataupload.entity.UserDetails;
import io.mosip.admin.bulkdataupload.entity.UserDetailsHistory;
import io.mosip.admin.bulkdataupload.entity.ValidDocument;
import io.mosip.admin.bulkdataupload.entity.Zone;
import io.mosip.admin.bulkdataupload.entity.ZoneUser;
/**
 * This class give the repository based on entity
 * @author dhanendra
 *
 */
@Component
public class Mapper {


	private Map<Class,String> repositoryMap=new HashMap<Class, String>();
	
    private  Map<String,Class> entityMap=new HashMap<String, Class>();

    public void init() {
    	loadEntity();
    	loadRepo();
    }
    private void loadRepo() {
    	repositoryMap.put(Gender.class, "genderTypeRepository");
    	repositoryMap.put( ApplicantValidDocument.class, "applicantValidDocumentRepository");
		repositoryMap.put(Application.class, "applicationRepository");
		repositoryMap.put(BiometricAttribute.class, "biometricAttributeRepository");
		repositoryMap.put(BiometricType.class,"biometricTypeRepository");
		repositoryMap.put(BlacklistedWords.class,"blacklistedWordsRepository");
		repositoryMap.put(Device.class, "deviceRepository");
		repositoryMap.put(DeviceHistory.class,"deviceHistoryRepository");
		repositoryMap.put(DeviceProviderHistory.class, "deviceProviderRepository");
		repositoryMap.put(DeviceRegister.class ,"deviceRegisterRepository");
		repositoryMap.put(DeviceRegisterHistory.class,"deviceRegisterHistoryRepository");
		repositoryMap.put(DeviceSpecification.class, "deviceSpecificationRepository");
		repositoryMap.put(DeviceType.class,"deviceTypeRepository");
		repositoryMap.put(DocumentCategory.class,"documentCategoryRepository");
		repositoryMap.put(DocumentType.class,"documentTypeRepository");
		repositoryMap.put(DynamicField.class,"dynamicFieldRepository");
		repositoryMap.put(ExceptionalHoliday.class,"exceptionalHolidayRepository");
		repositoryMap.put(FoundationalTrustProvider.class,"foundationalTrustProviderRepository");
		repositoryMap.put(FoundationalTrustProviderHistory.class,"foundationalTrustProviderRepository");
		repositoryMap.put(Holiday.class,"holidayRepository");
		repositoryMap.put(IdentitySchema.class, "identitySchemaRepository");
		repositoryMap.put(IdType.class,"idTypeRepository");
		repositoryMap.put(IndividualType.class,"individualTypeRepository");
		repositoryMap.put(Language.class, "languageRepository");
		repositoryMap.put(Location.class,"locationRepository");
		repositoryMap.put(LocationHierarchy.class,"locationHierarchyRepository");
		repositoryMap.put(Machine.class,"machineRepository");
		repositoryMap.put(MachineHistory.class,"machineHistoryRepository");
		repositoryMap.put(MachineSpecification.class,"machineSpecificationRepository");
		repositoryMap.put(MachineType.class,"machineTypeRepository");
		repositoryMap.put(ModuleDetail.class,"moduleRepository");
		repositoryMap.put(MOSIPDeviceService.class,"mosipDeviceServiceRepository");
		repositoryMap.put(MOSIPDeviceServiceHistory.class,"mosipDeviceServiceHistoryRepository");
		repositoryMap.put(ReasonCategory.class,"reasonCategoryRepository");
		repositoryMap.put(ReasonList.class,"reasonListRepository");
		repositoryMap.put(RegExceptionalHoliday.class, "RegExceptionalHolidayRepo");
		repositoryMap.put(RegisteredDevice.class,"registeredDeviceRepository");
		repositoryMap.put(RegisteredDeviceHistory.class,"registeredDeviceHistoryRepository");
		repositoryMap.put(RegistrationCenter.class,"registrationCenterRepository");
		repositoryMap.put(RegistrationCenterDevice.class,"registrationCenterDeviceRepository");
		repositoryMap.put(RegistrationCenterDeviceHistory.class,"registrationCenterDeviceHistoryRepository");
		repositoryMap.put(RegistrationCenterHistory.class,"registrationCenterHistoryRepository");
		repositoryMap.put(RegistrationCenterMachine.class,"registrationCenterMachineRepository");
		repositoryMap.put(RegistrationCenterMachineDevice.class,"registrationCenterMachineDeviceRepository");
		repositoryMap.put(RegistrationCenterMachineDeviceHistory.class,"registrationCenterMachineDeviceRepository");
		repositoryMap.put(RegistrationCenterMachineHistory.class,"registrationCenterMachineDeviceHistoryRepository");
		repositoryMap.put(RegistrationCenterType.class,"registrationCenterTypeRepository");
		repositoryMap.put(RegistrationCenterUser.class,"registrationCenterUserRepository");
		repositoryMap.put(RegistrationCenterUserHistory.class,"registrationCenterUserHistoryRepository");
		repositoryMap.put(RegistrationCenterUserMachineHistory.class, "registrationCenterUserMachineHistoryRepository");
		repositoryMap.put(RegistrationDeviceSubType.class,"registrationDeviceSubTypeRepository");
		repositoryMap.put(RegistrationDeviceType.class,"registrationDeviceTypeRepository");
		repositoryMap.put(RegWorkingNonWorking.class, "workingDaysRepo");
		repositoryMap.put(Template.class,"templateRepository");
		repositoryMap.put(TemplateFileFormat.class,"templateFileFormatRepository");
		repositoryMap.put(TemplateType.class,"templateTypeRepository");
		repositoryMap.put(Title.class,"titleRepository");
		repositoryMap.put(UserDetails.class,"userDetailsRepository");
		repositoryMap.put(UserDetailsHistory.class,"userDetailsHistoryRepository");
		repositoryMap.put(ValidDocument.class,"validDocumentRepository");
		repositoryMap.put(Zone.class,"zoneRepository");
		repositoryMap.put(ZoneUser.class,"zoneUserRepository");
		repositoryMap.put(DaysOfWeek.class, "daysOfWeekRepo");
    }
    public String getRepo(Class<?> clzz) {
    	return repositoryMap.get(clzz);
    }
	private void loadEntity() {
		
		entityMap.put("applicant_valid_document", ApplicantValidDocument.class);
		entityMap.put("appl_form_type", Application.class);
		entityMap.put("biometric_attribute", BiometricAttribute.class);
		entityMap.put("biometric_type",BiometricType.class );
		entityMap.put("blacklisted_words", BlacklistedWords.class);
		entityMap.put("daysofweek_list", DaysOfWeek.class);
		entityMap.put("device_master", Device.class);
		entityMap.put("device_master_h", DeviceHistory.class);
		entityMap.put("device_provider_h", DeviceProviderHistory.class);
		entityMap.put("registered_device_master",DeviceRegister.class );
		entityMap.put("registered_device_master_h", DeviceRegisterHistory.class);
		entityMap.put("device_spec", DeviceSpecification.class);
		entityMap.put("device_type", DeviceType.class);
		entityMap.put("doc_category", DocumentCategory.class);
		entityMap.put("doc_type", DocumentType.class);
		entityMap.put("dynamic_field", DynamicField.class);
		entityMap.put("reg_exceptional_holiday", ExceptionalHoliday.class);
		entityMap.put("foundational_trust_provider", FoundationalTrustProvider.class);
		entityMap.put("foundational_trust_provider_h", FoundationalTrustProviderHistory.class);
		entityMap.put("gender", Gender.class);
		entityMap.put("loc_holiday", Holiday.class);
		entityMap.put("identity_schema", IdentitySchema.class);
		entityMap.put("id_type", IdType.class);
		entityMap.put("individual_type", IndividualType.class);
		entityMap.put("language", Language.class);
		entityMap.put("location", Location.class);
		entityMap.put("loc_hierarchy_list", LocationHierarchy.class);
		entityMap.put("machine_master", Machine.class);
		entityMap.put("machine_master_h", MachineHistory.class);
		entityMap.put("machine_spec", MachineSpecification.class);
		entityMap.put("machine_type", MachineType.class);
		entityMap.put("module_detail", ModuleDetail.class);
		entityMap.put("mosip_device_service", MOSIPDeviceService.class);
		entityMap.put("mosip_device_service_h", MOSIPDeviceServiceHistory.class);
		entityMap.put("reason_category", ReasonCategory.class);
		entityMap.put("reason_list", ReasonList.class);
		entityMap.put("reg_exceptional_holiday", RegExceptionalHoliday.class);
		entityMap.put("registered_device_master", RegisteredDevice.class);
		entityMap.put("registered_device_master_h", RegisteredDeviceHistory.class);
		entityMap.put("registration_center", RegistrationCenter.class);
		entityMap.put("reg_center_device", RegistrationCenterDevice.class);
		entityMap.put("reg_center_device_h", RegistrationCenterDeviceHistory.class);
		entityMap.put("registration_center_h", RegistrationCenterHistory.class);
		entityMap.put("reg_center_machine", RegistrationCenterMachine.class);
		entityMap.put("reg_center_machine_device", RegistrationCenterMachineDevice.class);
		entityMap.put("reg_center_machine_device_h", RegistrationCenterMachineDeviceHistory.class);
		entityMap.put("reg_center_machine_h", RegistrationCenterMachineHistory.class);
		entityMap.put("reg_center_type", RegistrationCenterType.class);
		entityMap.put("reg_center_user", RegistrationCenterUser.class);
		entityMap.put("reg_center_user_h", RegistrationCenterUserHistory.class);
		entityMap.put("reg_center_user_machine", RegistrationCenterUserMachine.class);
		entityMap.put("reg_center_user_machine_h", RegistrationCenterUserMachineHistory.class);
		entityMap.put("reg_device_sub_type", RegistrationDeviceSubType.class);
		entityMap.put("reg_device_type", RegistrationDeviceType.class);
		entityMap.put("reg_working_nonworking", RegWorkingNonWorking.class);
		entityMap.put("schema_def", SchemaDefinition.class);
		entityMap.put("template", Template.class);
		entityMap.put("template_file_format", TemplateFileFormat.class);
		entityMap.put("template_type", TemplateType.class);
		entityMap.put("title", Title.class);
		entityMap.put("user_detail", UserDetails.class);
		entityMap.put("user_detail_h", UserDetailsHistory.class);
		entityMap.put("valid_document", ValidDocument.class);
		entityMap.put("zone", Zone.class);
		entityMap.put("zone_user", ZoneUser.class);
	
	}
	
	public Class<?> getEntity(String tableName) {
		return entityMap.get(tableName);
	}
}
