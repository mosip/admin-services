package io.mosip.kernel.masterdata.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "openapi")
public class OpenApiProperties {

	private InfoProperty info;
	public InfoProperty getInfo() {
		return info;
	}
	public void setInfo(InfoProperty info) {
		this.info = info;
	}
}

class InfoProperty {
	private String title;
	private String description;
	private String version;
	private LicenseProperty license;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public LicenseProperty getLicense() {
		return license;
	}
	public void setLicense(LicenseProperty license) {
		this.license = license;
	}
}

class LicenseProperty {
	private String name;
	private String url;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}