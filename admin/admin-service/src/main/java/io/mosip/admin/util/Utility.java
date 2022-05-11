package io.mosip.admin.util;

import io.mosip.kernel.core.util.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
public class Utility {

    private static final Logger logger = LoggerFactory.getLogger(Utility.class);

    @Value("${mosip.kernel.config.server.file.storage.uri}")
    private String configServerFileStorageURL;

    @Value("${mosip.admin.identityMappingJson}")
    private String residentIdentityJson;

    @Autowired
    private RestClient restClient;

    @Autowired
    private Environment env;

    private static final String IDENTITY = "identity";
    private static final String VALUE = "value";

    private static String regProcessorIdentityJson = "";



    @PostConstruct
    private void loadRegProcessorIdentityJson() throws Exception {
        regProcessorIdentityJson = restClient.getForObject(configServerFileStorageURL + residentIdentityJson, String.class);
        logger.info("loadRegProcessorIdentityJson completed successfully");
    }

    public JSONObject getMappingJson() throws Exception {
        JSONObject regProcessorIdentityJSONObj=null;
        if (StringUtils.isBlank(regProcessorIdentityJson)) {
            regProcessorIdentityJson=restClient.getForObject(configServerFileStorageURL + residentIdentityJson, String.class);
        }
        regProcessorIdentityJSONObj=new JSONObject(regProcessorIdentityJson);
        return regProcessorIdentityJSONObj;
    }
}
