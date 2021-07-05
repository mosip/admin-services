package io.mosip.kernel.masterdata.utils;

public enum CacheName {

	BLOCK_LISTED_WORDS("blocklisted-words"),
	DOCUMENT_CATEGORY("document-category"),
	DOCUMENT_TYPE("document-type"),
	DYNAMIC_FIELD("dynamic-field"),
	EXCEPTIONAL_HOLIDAY("exceptional-holiday"),
	GENDER_TYPE("gender-type"),
	ID_TYPE("id-type"),
	INDIVIDUAL_TYPE("individual-type"),
	LANGUAGES("languages"),
	LOCATIONS("locations"),
	LOCATION_HIERARCHY("location-hierarchy"),
	TEMPLATES("templates"),
	TEMPLATE_TYPE("template-type"),
	TITLES("titles"),
	UI_SPEC("ui-spec"),
	VALID_DOCUMENT("valid-document"),
	WORKING_DAY("working-day"),
	ZONES("zones");
	
	public final String name;
	
	CacheName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	
	public static CacheName cacheNameByName(String name) {
		for (CacheName cacheName : values()) {
			if (cacheName.name.equals(name)) {
				return cacheName;
			}
		}
		return null;
	}
	
}
