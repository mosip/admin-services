package io.mosip.kernel.masterdata.service.impl;

import io.mosip.kernel.core.exception.BaseUncheckedException;
import io.mosip.kernel.core.util.JsonUtils;
import io.mosip.kernel.masterdata.dto.AddressDto;
import io.mosip.kernel.masterdata.service.AddressService;
import io.mosip.kernel.masterdata.service.LocationService;
import org.json.JSONObject;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {

    @Value("${opencrvs.locations.url}")
    private String locationsUrl;
    @Value("${opencrvs.data.lang.code.default}")
    private String defaultLangCode;
    @Value("${opencrvs.data.lang.code.mapping}")
    private String langCodeMapping;
    @Value("${opencrvs.data.address.line.mapping}")
    private String addressLineMapping;
    @Value("${opencrvs.data.address.location.mapping}")
    private String addressLocationMapping;
    @Value("${opencrvs.data.address.line.joiner}")
    private String addressLineJoiner;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    KieSession kieSession;

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");


    @Autowired
    private KieContainer kieContainer;

    public AddressDto getLatestAddress(AddressDto dto) {

        try {
            Instant start = Instant.now();
            Map<String, Object> addressJson = dto.getAddress();
            kieSession.insert(addressJson);
            kieSession.insert(dto.getEffectiveDate());
            kieSession.fireAllRules();
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
            LOGGER.debug("Time taken: "+ timeElapsed.toMillis() +" milliseconds");
            // kieSession.dispose();
            dto.setAddress(addressJson);
        } catch (Exception e) {
            LOGGER.error("Exception occured - ", e);
            throw new BaseUncheckedException("KER-LOC-001", e.getMessage());
        }
        return dto;
    }

    public AddressDto getLatestFHIRAddress(AddressDto dto) {

        try {
            Map<String, Object> addressJson = new HashMap<>();
            getAddressLinesFromAddress(dto.getAddress(), addressJson);
            getAddressLocationFromAddress(dto.getAddress(), addressJson);
            LOGGER.debug("getLatestFHIRAddress - ", addressJson);

            // Map<String, Object> addressJson = new ObjectMapper().readValue(location, Map.class);
            LOGGER.info(addressJson.toString());
            kieSession.insert(addressJson);
            kieSession.insert(dto.getEffectiveDate());
            kieSession.fireAllRules();
            LOGGER.debug("getLatestFHIRAddress::Modified Address: " + JsonUtils.javaObjectToJsonString(addressJson));
            // kieSession.dispose();
            dto.setAddress(addressJson);
        } catch (Exception e) {
            LOGGER.error("Exception occured - " + e.getStackTrace());
            throw new BaseUncheckedException("KER-LOC-001", e.getMessage());
        }
        return dto;
    }

    public void getAddressLinesFromAddress(Map<String, Object> address, Map<String, Object> addressJson){
        String toReturn = "";
        for (String mappingLine : addressLineMapping.split("\\|")) {
            String mosipLineNumber = mappingLine.split(":")[0];
            String opencrvsLines = mappingLine.split(":")[1];
            int opencrvsStartingLineNumber = Integer.parseInt(opencrvsLines.split("-")[0]) - 1;
            int opencrvsEndingLineNumber = Integer.parseInt(opencrvsLines.split("-")[1]) - 1;
            String lineValue = "";
            for(int i=opencrvsStartingLineNumber;i<=opencrvsEndingLineNumber; i++){
                if (!((List<String>) address.get("line")).get(i).isBlank()) {
                    if (!lineValue.isBlank()) lineValue +=
                            addressLineJoiner.replaceAll("\"", "");
                    lineValue += ((List<String>) address.get("line")).get(i);
                }
            }
            Map<String, String> langValMap = new HashMap<>();
            langValMap.put(defaultLangCode, lineValue.trim());
            addressJson.put("addressLine" + mosipLineNumber, langValMap);
        }
    }

    public void getAddressLocationFromAddress(Map<String, Object> address, Map<String, Object> addressJson){
        for (String mappingLocation : addressLocationMapping.split("\\|")) {
            String mosipLocation = mappingLocation.split(":")[0];
            String opencrvsLocation = mappingLocation.split(":")[1];
            String opencrvsLocationIfId = mappingLocation.split(":")[2];
            if("id".equals(opencrvsLocationIfId)){
                String value = fetchAddressValueFromId((String)address.get(opencrvsLocation));
                Map<String, String> langValMap = new HashMap<>();
                langValMap.put(defaultLangCode, value);
                addressJson.put(mosipLocation, langValMap);
            } else {
                Map<String, String> langValMap = new HashMap<>();
                String value = address.get(opencrvsLocation) != null? address.get(opencrvsLocation).toString(): null;
                langValMap.put(defaultLangCode, value);
                addressJson.put(mosipLocation, langValMap);
            }
        }
    }

    public String fetchAddressValueFromId(String id){
        try{
            JSONObject json = new JSONObject(
                    restTemplate.getForObject(locationsUrl + "/" + id, String.class));
            return json.getString("name");
        } catch(Exception e){
            throw new BaseUncheckedException("KER-ADD-001", e.getMessage(), e);
        }
    }
}
