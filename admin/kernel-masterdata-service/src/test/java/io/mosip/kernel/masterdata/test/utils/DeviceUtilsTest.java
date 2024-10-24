package io.mosip.kernel.masterdata.test.utils;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.masterdata.entity.DeviceSpecification;
import io.mosip.kernel.masterdata.entity.DeviceType;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.DeviceSpecificationRepository;
import io.mosip.kernel.masterdata.repository.DeviceTypeRepository;
import io.mosip.kernel.masterdata.repository.RegistrationCenterRepository;
import io.mosip.kernel.masterdata.utils.DeviceUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class DeviceUtilsTest {

    @Mock
    private DeviceSpecificationRepository deviceSpecificationRepository;

    @Mock
    private DeviceTypeRepository deviceTypeRepository;

    @Mock
    private RegistrationCenterRepository centerRepository;

    @InjectMocks
    private DeviceUtils deviceUtils;

    @Test
    public void testGetDeviceSpec_Success() {
        List<DeviceSpecification> mockDeviceSpecs = List.of(new DeviceSpecification());

        Mockito.when(deviceSpecificationRepository.findAllDeviceSpecByIsActiveAndIsDeletedIsNullOrFalse())
                .thenReturn(mockDeviceSpecs);

        List<DeviceSpecification> deviceSpecs = deviceUtils.getDeviceSpec();

        assertEquals(mockDeviceSpecs, deviceSpecs);
    }

    @Test(expected = MasterDataServiceException.class)
    public void testGetDeviceSpec_DataAccessLayerException() {
        Mockito.when(deviceSpecificationRepository.findAllDeviceSpecByIsActiveAndIsDeletedIsNullOrFalse())
                .thenThrow(DataAccessLayerException.class);

        deviceUtils.getDeviceSpec();
    }

    @Test
    public void testGetDeviceTypes_Success() {
        List<DeviceType> mockDeviceTypes = List.of(new DeviceType());

        Mockito.when(deviceTypeRepository.findAllDeviceTypeByIsActiveAndIsDeletedFalseOrNull())
                .thenReturn(mockDeviceTypes);

        List<DeviceType> deviceTypes = deviceUtils.getDeviceTypes();

        assertEquals(mockDeviceTypes, deviceTypes);
    }

    @Test(expected = MasterDataServiceException.class)
    public void testGetDeviceTypes_DataAccessLayerException() {
        Mockito.when(deviceTypeRepository.findAllDeviceTypeByIsActiveAndIsDeletedFalseOrNull())
                .thenThrow(DataAccessLayerException.class);

        deviceUtils.getDeviceTypes();
    }

    @Test
    public void testGetAllRegistrationCenters_Success() {
        String langCode = "en_US";
        List<RegistrationCenter> mockCenters = List.of(new RegistrationCenter());

        Mockito.when(centerRepository.findAllByIsDeletedFalseOrIsDeletedIsNullAndLangCode(langCode))
                .thenReturn(mockCenters);

        List<RegistrationCenter> centers = deviceUtils.getAllRegistrationCenters(langCode);

        assertEquals(mockCenters, centers);
    }

    @Test(expected = MasterDataServiceException.class)
    public void testGetAllRegistrationCenters_DataAccessLayerException() {
        String langCode = "en_US";

        Mockito.when(centerRepository.findAllByIsDeletedFalseOrIsDeletedIsNullAndLangCode(langCode))
                .thenThrow(DataAccessLayerException.class);

        deviceUtils.getAllRegistrationCenters(langCode);
    }
}
