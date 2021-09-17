package io.mosip.kernel.masterdata.dto;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


@Component("authorizedRoles")
@ConfigurationProperties(prefix = "mosip.role.admin.masterdata")
@Getter
@Setter
public class AuthorizedRolesDto {

	////masterdata controller
    //private List<String> postgetapplicanttype;
    
    //private List<String> getapplicanttypeapplicantidlanguages;

    private List<String> getapplicationconfigs;
	
    private List<String> getconfigs;
	
    private List<String> getapplicationtypes;
	
    private List<String> getapplicationtypeslangcode;
	
    private List<String> getapplicationtypescodelangcode;
	
    private List<String> postapplicationtypes;
	
    private List<String> getgetbiometricattributesbyauthtype;
	
    private List<String> postbiometricattributes;
    
    private List<String> getbiometrictypes;
    
    private List<String> getbiometrictypeslangcode;
    
    private List<String> getbiometrictypescodelangcode;
    
    private List<String> postbiometrictypes;
    
    //private List<String> getblocklistedwordslangcode;
    
    private List<String> postblocklistedwordswords;
    
    private List<String> postblocklistedwords;
    
    private List<String> putblocklistedwords;
    
    private List<String> putblocklistedwordsdetails;
    
    private List<String> deleteblocklistedwordsword;
    
    private List<String> getblocklistedwordsall;

    private List<String> postblocklistedwordssearch;
    
    private List<String> postblocklistedwordsfiltervalues;
    
    private List<String> patchblocklistedwords;

    private List<String> getdeviceslanguagecode;
    
    private List<String> getdeviceslanguagecodedevicetype;
    
    private List<String> postdevices;
    
    private List<String> putdevices;
	
    private List<String> getdevicesmappeddevicesregcenterid;
    
    private List<String> postdevicessearch;
    
    private List<String> postdevicesfiltervalues;
    
    private List<String> putdevicesdecommissiondeviceid;
    
    private List<String> patchdevices;
    
    private List<String> getdeviceshistoriesidlangcodeeffdatetimes;
    
    private List<String> postdeviceprovider;
    
    private List<String> putdeviceprovider;
    
    private List<String> postdeviceprovidermanagementvalidate;

    private List<String> deletedevicederegisterdevicecode;
    
    private List<String> putdeviceupdatestatus;
    
    private List<String> getdevicespecifications;

    private List<String> getdevicespecificationsdevicetypecode;
    
    private List<String> postdevicespecifications;
    
    private List<String> putdevicespecifications;
    
    private List<String> deletedevicespecificationsid;
    
	private List<String> getdevicespecificationsall;
    
    private List<String> postdevicespecificationssearch;
    
    private List<String> postdevicespecificationsfiltervalues;
    
    private List<String> patchdevicespecifications;
    
    private List<String> postdevicetypes;
    
    private List<String> putdevicetypes;
    
    private List<String> getdevicetypesall;
    
    private List<String> postdevicetypessearch;
    
    private List<String> postdevicetypesfiltervalues;
    
    private List<String> patchdevicetypes;

    //private List<String> getdocumentcategories;
    
    private List<String> getdocumentcategorieslangcode;
    
    private List<String> getdocumentcategoriescode;

    private List<String> postdocumentcategories;
    
    private List<String> putdocumentcategories;
    
    private List<String> deletedocumentcategoriescode;
    
    private List<String> getdocumentcategoriesall;
	
	private List<String> postdocumentcategoriessearch;
	
	private List<String> postdocumentcategoriesfiltervalues;
	
	private List<String> patchdocumentcategories;
	
	//private List<String> getdocumenttypesdocumentcategorycode;
	
	private List<String> postdocumenttypes;
	
	private List<String> putdocumenttypes;
	
	private List<String> deletedocumenttypescode;
	
	private List<String> getdocumenttypesall;
	
	private List<String> postdocumenttypesfiltervalues;
	
	private List<String> postdocumenttypessearch;
	
	//private List<String> getdocumenttypeslangcode;
	
	private List<String> patchdocumenttypes;

	private List<String> getdocumenttypesmissingidslangcode;
	
//	private List<String> getdynamicfields;
	
//	private List<String> getdistinct;
	
	private List<String> postdynamicfields;
	
	private List<String> putdynamicfields ;
	
	private List<String> patchdynamicfieldsall;
	
	private List<String> patchdynamicfields;
	
	private List<String> deletedynamicfields;
	
	private List<String> deletedynamicfieldsid;
	
	private List<String> postdynamicfieldssearch;
	
//	private List<String> getexceptionalholidaysregistrationcenterid;
	
	private List<String> postfoundationaltrustprovider;
	
	private List<String> putfoundationaltrustprovider;
	
//	private List<String> getgendertypes;
	
//	private List<String> getgendertypeslangcode;
	
	private List<String> postgendertypes;
	
	private List<String> putgendertypes;
	
	private List<String> deletegendertypescode;
	
//	private List<String> getgendertypesvalidategendername;
	
	private List<String> getgendertypesall;
	
	private List<String> postgendertypessearch;
	
	private List<String> postgendertypesfiltervalues;
	
	private List<String> getholidays;
	
	private List<String> getholidaysholidayid;
	
	private List<String> getholidaysholidayidlangcode;
	
	private List<String> postholidays;
	
	private List<String> putholidays;
	
	private List<String> patchholidays;
	
	private List<String> deleteholidays;
	
	private List<String> getholidaysall;
	
	private List<String> postholidayssearch;
	
	private List<String> postholidaysfiltervalues;
	
	private List<String> getholidaysmissingidslangcode;
	
	//private List<String> getidtypeslangcode;
	
	private List<String> postidtypes;
	
	//private List<String> getindividualtypes;
	
	private List<String> getindividualtypesall;
	
	private List<String> postindividualtypessearch;
	
	private List<String> postindividualtypesfiltervalues;
	
	private List<String> postindividualtypes;
	
	private List<String> putindividualtypes;
	
	//private List<String> getlanguages;
	
	private List<String> postlanguages;
	
	private List<String> getputlanguages;
	
	private List<String> getpatchlanguages;
	
	private List<String> getdeletelanguagescode;
	
	//private List<String> getlocationslangcode;
	
	private List<String> getlocations;
	
	//private List<String> getlocationslocationcodelangcode;
	
	//private List<String> getlocationsinfolocationcode;
	
	//private List<String> getlocationslocationhierarchy;
	
	private List<String> putlocations;
	
	private List<String> patchlocations;
	
	private List<String> deletelocationslocationcode;
	
	//private List<String> getlocationsimmediatechildrenlocationcodelangcode;
	
	//private List<String> getlocationsvalidatelocationname;
	
	private List<String> getlocationsall;
	
	private List<String> postlocationssearch;
	
	private List<String> postlocationsfiltervalues;
	
	private List<String> getlocationslevellangcode;
	
	private List<String> getlocationsmissingidslangcode;
	
	//private List<String> getlocationhierarchylevelslevellangcode;

    private List<String> getlocationhierarchylevels;
	
	//private List<String> getlocationhierarchylevelslangcode;
	
	private List<String> getmachinesidlangcode;
	
	private List<String> getmachineslangcode;
	
	private List<String> getmachines;

    private List<String> deletemachinesid;
	
	private List<String> getmachinesmappedmachinesregcenterid;
	
	private List<String> postmachinessearch;
	
	private List<String> postmachinesfiltervalues;
	
	private List<String> putmachinesdecommissionmachineid;

    private List<String> postmachines;
	
	private List<String> putmachines;
	
	private List<String> patchmachines;
	
	private List<String> getmachineshistoriesidlangcode;
	
	private List<String> postmachinespecifications;

    private List<String> putmachinespecifications;
	
	private List<String> patchmachinespecifications;
	
	private List<String> deletemachinespecificationsid;
	
	private List<String> getmachinespecificationsall;
	
	private List<String> postmachinespecificationssearch;

    private List<String> postmachinespecificationsfiltervalues;
	
	private List<String> postmachinetypes;
	
	private List<String> putmachinetypes;
	
	private List<String> patchmachinetypes;
	
	private List<String> getmachinetypesall;
	
	private List<String> postmachinetypessearch;

    private List<String> postmachinetypesfiltervalues;
	
	private List<String> getmodulesidlangcode;
	
	private List<String> getmoduleslangcode;
	
	private List<String> postmosipdeviceservice;
	
	private List<String> putmosipdeviceservice;
	
	private List<String> postpacketrejectionreasonsreasoncategory;

    private List<String> postpacketrejectionreasonsreasonlist;
	
	private List<String> getpacketrejectionreasons;
	
	private List<String> getpacketrejectionreasonsreasoncategorycodelangcode;
	
	private List<String> postpacketresume;
	
	private List<String> postpacketsearch;
	
	private List<String> getpossiblevaluesfieldname;
	
	private List<String> postregistereddevices;
	
	private List<String> postregistereddevicesderegister;

    private List<String> putregistereddevicesupdatestatus;
	
	private List<String> getgetlocspecificregistrationcenterslangcode;
	
	private List<String> getgetregistrationcenterholidayslangcode;
	
	private List<String> getgetcoordinatespecificregistrationcenters;
	
	private List<String> getregistrationcentersidlangcode;
	
	private List<String> getregistrationcenters;

    private List<String> getregistrationcentersidlangcodehierarchylevel;
	
	private List<String> getregistrationcenterspagelangcode;
	
	private List<String> getregistrationcentersvalidateidlangcode;
	
	private List<String> deleteregistrationcentersregistrationcenterid;
	
	private List<String> getregistrationcenterslangcodehierarchylevelnames;
	
	private List<String> getregistrationcentersall;

    private List<String> postregistrationcenterssearch;
	
	private List<String> postregistrationcentersfiltervalues;
	
	private List<String> postregistrationcenters;
	
	private List<String> putregistrationcenters;
	
	private List<String> putregistrationcentersdecommission;
	
	private List<String> patchregistrationcenters;

    private List<String> putregistrationcenterslanguage;
	
	private List<String> putregistrationcentersnonlanguage;
	
	private List<String> getregistrationcentersmissingids;
	
	private List<String> getgetzonespecificregistrationcenterslangcode;
	
	private List<String> getregistrationcenterdevicehistoryregcenteriddeviceid;
	
	private List<String> getregistrationcentershistoryregistrationcenterid;

    private List<String> postregistrationcentertypes;
	
	private List<String> putregistrationcentertypes;
	
	private List<String> deleteregistrationcentertypes;
	
	private List<String> getregistrationcentertypesall;
	
	private List<String> postregistrationcentertypesfiltervalues;
	
	private List<String> postregistrationcentertypessearch;

    private List<String> patchregistrationcentertypes;
	
	private List<String> getregistrationcentertypesmissingidslangcode;
	
	private List<String> getgetregistrationmachineusermappinghistory;
	
	private List<String> postidschema;
	
	private List<String> putidschema;
	
	private List<String> putidschemapublish;

    private List<String> deleteidschema;
	
	private List<String> getidschemaall;
	
	private List<String> getidschemalatest;
	
	//private List<String> gettemplates;
	
	//private List<String> gettemplateslangcode;
	
	//private List<String> gettemplateslangcodetemplatetypecode;

    private List<String> posttemplates;
	
	private List<String> puttemplates;
	
	private List<String> deletetemplatesid;
	
	private List<String> gettemplatestemplatetypecodescode;
	
	private List<String> gettemplatesall;
	
	private List<String> posttemplatessearch;
	
	private List<String> posttemplatesfiltervalues;
	
	private List<String> patchtemplates;
	
	private List<String> gettemplatesmissingidslangcode;
	
	private List<String> posttemplatefileformats;
	
	private List<String> puttemplatefileformats;
	
	private List<String> deletetemplatefileformatscode;
	
	private List<String> gettemplatefileformatscodelangcode;
	
	private List<String> gettemplatefileformatslangcode;
	
	private List<String> patchtemplatefileformats;
	
	private List<String> posttemplatetypes;
	
	//private List<String> gettemplatetypescodelangcode;
	
	//private List<String> gettemplatetypeslangcode;
	
	//private List<String> gettitle;
	
	//private List<String> gettitlelangcode;
	
	private List<String> posttitle;
	
	private List<String> puttitle;
	
	private List<String> puttitlecode;
	
	private List<String> gettitleall;
	
	private List<String> posttitlesearch;
	
	private List<String> posttitlefiltervalues;
	
	private List<String> postuispec;
	
	private List<String> putuispec;
	
	private List<String> postuispecpublish;
	
	private List<String> deleteuispec;
	
	private List<String> getuispecall;
	
	//private List<String> getuispecdomainlatest;
	
	private List<String> getusersid;
	
	private List<String> getusers;
	
	private List<String> getusercentermapping;
	
	private List<String> putusercentermapping;
	
	private List<String> patchusercentermapping;
	
	private List<String> deleteusersid;
	
	private List<String> getuserdetails;
	
	private List<String> getuserssearch;
	
	private List<String> getusersideffdtimes;
	
	private List<String> postvaliddocuments;
	
	private List<String> deletevaliddocumentsdoccategorycode;
	
	//private List<String> getvaliddocumentslanguagecode;
	
	private List<String> getvaliddocumentsall;
	
	private List<String> postvaliddocumentssearch;
	
	private List<String> postvaliddocumentsfiltervalues;
	
	private List<String> putvaliddocumentsmapdoccategorycode;
	
	private List<String> putvaliddocumentsunmapdoccategorycode;
	
	//private List<String> getvaliddocumentsdoccategorycode;
	
	//private List<String> getweekdaysregistrationcenteridlangcode;
	
	//private List<String> getworkingdaysregistrationcenteridlangcode;
	
	//private List<String> getworkingdayslangcode;
	
	private List<String> getzoneshierarchylangcode;
	
	//private List<String> getzonesleafslangcode;
	
	private List<String> getzoneszonename;
	
	private List<String> getzonesauthorize;
	
	private List<String> postzonesfiltervalues;
	
	private List<String> putzoneuser;
	
	private List<String> postzoneuser;
	
	private List<String> deletezoneuseruseridzonecode;
	
	private List<String> getzoneuserhistoryuserid;
	
	private List<String> patchzoneuser;

	private List<String> postzoneusersearch;

}