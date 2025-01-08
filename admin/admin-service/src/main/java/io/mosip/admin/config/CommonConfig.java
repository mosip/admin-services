package io.mosip.admin.config;

import jakarta.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.web.filter.CommonsRequestLoggingFilter;

import io.mosip.admin.bulkdataupload.repositories.DocumentCategoryRepository;
import io.mosip.admin.bulkdataupload.repositories.DocumentTypeRepository;
import io.mosip.admin.httpfilter.ReqResFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * 
 * @author Srinivasan
 *
 */
@Configuration
public class CommonConfig {

	@Autowired
	private DocumentCategoryRepository documentCategoryRepository;
	
	@Autowired
	private DocumentTypeRepository documentTypeRepository;
	
	private List<String> docCatCodes;
	
	private List<String> docTypeCodes;
	
	private List<String> getDocCatCodes(){
		if(docCatCodes.isEmpty()) {
			docCatCodes = documentCategoryRepository.findAllByIsDeletedFalseOrIsDeletedIsNull();
		}
		return docCatCodes;
	}
	
	private List<String> getDocTypeCodes(){
		if(docTypeCodes.isEmpty()) {
			docTypeCodes = documentTypeRepository.findAllByIsDeletedFalseOrIsDeletedIsNull();
		}
		return docTypeCodes;
	}
	
	@Scheduled(fixedRateString = "#{60 * 60 * 1000 * ${mosip.admin.doccodes-cleanup.fixed-rate}}")
	private void clearDocCodes() {
		docCatCodes.clear();
		docTypeCodes.clear();
	}
	
	@Bean
	public CommonsRequestLoggingFilter logFilter() {
		CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
		filter.setIncludeQueryString(true);
		filter.setIncludePayload(true);
		filter.setMaxPayloadLength(10000);
		filter.setIncludeHeaders(false);
		filter.setAfterMessagePrefix("REQUEST DATA : ");
		return filter;
	}

	@Bean
	public FilterRegistrationBean<Filter> registerReqResFilter() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(getReqResFilter());
		filterRegistrationBean.setOrder(2);
		return filterRegistrationBean;
	}

	@Bean
	public Filter getReqResFilter() {
		return new ReqResFilter();
	}

	@Bean
	public Properties packetProperties() {
		Properties properties = new Properties();
		try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("globalMessages.properties")) {
			properties.load(inputStream);
		} catch (IOException e) { }
		return properties;
	}
	
	@Bean("DocCatCodes")
	@Scope(value = "prototype")
	public List<String> docCatCodes(){
		return getDocCatCodes();
	}
	
	@Bean("DocTypeCodes")
	@Scope(value = "prototype")
	public List<String> docTypeCodes(){
		return getDocTypeCodes();
	}
}
