package io.mosip.kernel.syncdata.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MosipEnvironment implements EnvironmentAware {

	@Autowired
	private Environment environment;

	private String locationHierarchyUrl = "mosip.kernel.idobjectvalidator.masterdata.locationhierarchylevels.rest.uri";

	@Override
	public void setEnvironment(final Environment environment) {
		this.environment = environment;
	}

	public String getLocationHierarchyUrl() {
		return environment.getProperty(locationHierarchyUrl);
	}

}