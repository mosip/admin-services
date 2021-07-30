package io.mosip.kernel.masterdata.test.controller;

import io.mosip.kernel.masterdata.entity.DynamicField;
import io.mosip.kernel.masterdata.entity.Location;
import io.mosip.kernel.masterdata.repository.DynamicFieldRepository;
import io.mosip.kernel.masterdata.repository.LocationHierarchyRepository;
import io.mosip.kernel.masterdata.repository.LocationRepository;
import io.mosip.kernel.masterdata.service.PossibleValuesService;
import io.mosip.kernel.masterdata.test.TestBootApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
@AutoConfigureMockMvc
public class PossibleValuesControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private PossibleValuesService possibleValuesService;

    @MockBean
    private DynamicFieldRepository dynamicFieldRepository;

    @MockBean
    private LocationHierarchyRepository locationHierarchyRepository;

    @MockBean
    private LocationRepository locationRepository;

    private List<DynamicField> dynamicFields = new ArrayList<DynamicField>();
    private List<Location> locations = new ArrayList<>();

    @Before
    public void setup() {
        DynamicField bloodTypeField = new DynamicField();
        bloodTypeField.setDataType("simpleType");
        bloodTypeField.setDescription("test");
        bloodTypeField.setId("1233");
        bloodTypeField.setIsActive(true);
        bloodTypeField.setLangCode("eng");
        bloodTypeField.setName("bloodType");
        bloodTypeField.setValueJson("{\"value\":\"Optv\",\"code\":\"BT1\"}");
        dynamicFields.add(bloodTypeField);
        DynamicField bloodTypeField2 = new DynamicField();
        bloodTypeField2.setDataType("simpleType");
        bloodTypeField2.setDescription("test");
        bloodTypeField2.setId("1233");
        bloodTypeField2.setIsActive(true);
        bloodTypeField2.setLangCode("eng");
        bloodTypeField2.setName("bloodType");
        bloodTypeField2.setValueJson("{\"value\":\"Ontv\",\"code\":\"BT2\"}");
        dynamicFields.add(bloodTypeField2);
    }

    @Test
    @WithUserDetails("global-admin")
    public void testDefaultField() throws Exception {
        Mockito.when(locationHierarchyRepository.findByheirarchyLevalNameAndLangCode(Mockito.anyString(),
                Mockito.anyString())).thenReturn(1);
        Mockito.when(locationRepository.getAllLocationsByLangCodeAndLevel(Mockito.anyString(), Mockito.anyShort()))
                .thenReturn(locations);
        mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/Country?langCode=eng")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("global-admin")
    public void testDynamicField() throws Exception {
        Mockito.when(dynamicFieldRepository.findAllDynamicFieldValuesByNameAndLangCode(Mockito.anyString(),
                Mockito.anyString())).thenReturn(dynamicFields);
        mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/bloodType?langCode=eng"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("global-admin")
    public void testDefaultFieldWithNull() throws Exception {
        Mockito.when(locationHierarchyRepository.findByheirarchyLevalNameAndLangCode(Mockito.anyString(),
                Mockito.anyString())).thenReturn(null);
        Mockito.when(locationRepository.getAllLocationsByLangCodeAndLevel(Mockito.anyString(), Mockito.anyShort()))
                .thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/Country?langCode=eng")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("global-admin")
    public void testDynamicFieldWithNull() throws Exception {
        Mockito.when(dynamicFieldRepository.findAllDynamicFieldValuesByNameAndLangCode(Mockito.anyString(),
                Mockito.anyString())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/bloodType?langCode=eng"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("global-admin")
    public void testDefaultFieldWithEmpty() throws Exception {
        Mockito.when(locationHierarchyRepository.findByheirarchyLevalNameAndLangCode(Mockito.anyString(),
                Mockito.anyString())).thenReturn(0);
        Mockito.when(locationRepository.getAllLocationsByLangCodeAndLevel(Mockito.anyString(), Mockito.anyShort()))
                .thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/Country?langCode=eng")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("global-admin")
    public void testDynamicFieldWithEmpty() throws Exception {
        Mockito.when(dynamicFieldRepository.findAllDynamicFieldValuesByNameAndLangCode(Mockito.anyString(),
                Mockito.anyString())).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/possiblevalues/bloodType?langCode=eng"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
