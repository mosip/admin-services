package io.mosip.hotlist.constant;

/**
 * The Enum RestServiceContants - defines modules for which rest calls are made
 * from id repository. The value of constant is used to build the rest call
 * request.
 *
 * @author Manoj SP
 */
public enum RestServicesConstants {

	AUDIT_MANAGER_SERVICE("mosip.hotlist.audit"),

	CRYPTO_MANAGER_ENCRYPT("mosip.hotlist.encryptor"),

	CRYPTO_MANAGER_DECRYPT("mosip.hotlist.decryptor");

	/** The service name. */
	private final String serviceName;

	/**
	 * Instantiates a new rest service contants.
	 *
	 * @param serviceName the service name
	 */
	private RestServicesConstants(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Gets the service name.
	 *
	 * @return the service name
	 */
	public String getServiceName() {
		return serviceName;
	}
}