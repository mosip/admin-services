package io.mosip.hotlist.constant;

/**
 * The Enum AuditModules - Contains all the modules in Id Repository for Audit
 * purpose.
 *
 * @author Manoj SP
 */
public enum AuditModules {
	
	HOTLIST_SERVICE("ADM-HTL");
	

	/** The module id. */
	private final String moduleId;

	/**
	 * Gets the module id.
	 *
	 * @return the module id
	 */
	public String getModuleId() {
		return moduleId;
	}

	/**
	 * Gets the module name.
	 *
	 * @return the module name
	 */
	public String getModuleName() {
		return this.name();
	}

	/**
	 * Instantiates a new audit contants.
	 *
	 * @param moduleId the moduleId
	 */
	private AuditModules(String moduleId) {
		this.moduleId = moduleId;
	}
}