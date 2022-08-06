package io.mosip.admin.util;

import io.mosip.admin.packetstatusupdater.util.RestClient;
import io.mosip.kernel.core.util.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Component
public class Utility {

    private static final Logger logger = LoggerFactory.getLogger(Utility.class);

    @Value("${mosip.kernel.config.server.file.storage.uri}")
    private String configServerFileStorageURL;

    @Value("${mosip.admin.identityMappingJson}")
    private String identityJson;

    @Autowired
    private RestClient restClient;

    @Autowired
    private Environment env;

    private static final String IDENTITY = "identity";
    private static final String VALUE = "value";

    private static String regProcessorIdentityJson = "";

    public String getMappingJson() throws Exception {
        if (StringUtils.isBlank(regProcessorIdentityJson)) {
            regProcessorIdentityJson=restClient.getApi(configServerFileStorageURL + identityJson, String.class);
        }
        return regProcessorIdentityJson;
    }

    @SuppressWarnings("unchecked")
    public JSONObject getJSONObject(JSONObject jsonObject, Object key)  {
        if(jsonObject == null)
            return null;
        LinkedHashMap identity = (LinkedHashMap) jsonObject.get(key);
        return identity != null ? new JSONObject(identity) : null;
    }
    @SuppressWarnings("unchecked")
    public <T> T getJSONValue(JSONObject jsonObject, Object key)  {
        T value = (T) jsonObject.get(key);
        return value;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public JSONArray getJSONArray(JSONObject jsonObject, Object key) {
        ArrayList value = (ArrayList) jsonObject.get(key);
        if (value == null)
            return null;
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(value);

        return jsonArray;

    }
    @SuppressWarnings("rawtypes")
    public  JSONObject getJSONObjectFromArray(JSONArray jsonObject, int key) {
        LinkedHashMap identity = (LinkedHashMap) jsonObject.get(key);
        return identity != null ? new JSONObject(identity) : null;
    }


}
