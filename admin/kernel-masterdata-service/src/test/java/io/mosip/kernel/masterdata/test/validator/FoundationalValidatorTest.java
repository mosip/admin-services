package io.mosip.kernel.masterdata.test.validator;

import io.mosip.kernel.masterdata.dto.registerdevice.DeviceData;
import io.mosip.kernel.masterdata.dto.registerdevice.DeviceInfo;
import io.mosip.kernel.masterdata.validator.registereddevice.FoundationalValidator;
import io.mosip.kernel.masterdata.validator.registereddevice.RegisteredDeviceConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class FoundationalValidatorTest {

    @Test
    public void testValidL1Certification() {
        DeviceData deviceData = new DeviceData();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setCertification(RegisteredDeviceConstant.L1);
        deviceData.setDeviceInfo(deviceInfo);
        deviceData.setFoundationalTrustProviderId("valid_provider_id");

        FoundationalValidator validator = new FoundationalValidator();
        boolean isValid = validator.isValid(deviceData, null);

        assertTrue(isValid);
    }

    @Test
    public void testMissingFoundationalTrustProviderIdForL1() {
        DeviceData deviceData = new DeviceData();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setCertification(RegisteredDeviceConstant.L1);
        deviceData.setDeviceInfo(deviceInfo);

        FoundationalValidator validator = new FoundationalValidator();
        boolean isValid = validator.isValid(deviceData, null);

        assertFalse(isValid);
    }

    @Test
    public void testValidL0Certification() {
        DeviceData deviceData = new DeviceData();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setCertification(RegisteredDeviceConstant.L0);
        deviceData.setDeviceInfo(deviceInfo);

        FoundationalValidator validator = new FoundationalValidator();
        boolean isValid = validator.isValid(deviceData, null);

        assertFalse(isValid);
    }

    @Test
    public void testMissingDeviceInfo() {
        DeviceData deviceData = new DeviceData();

        FoundationalValidator validator = new FoundationalValidator();
        boolean isValid = validator.isValid(deviceData, null);

        assertFalse(isValid);
    }

    @Test
    public void testMissingCertificationInDeviceInfo() {
        DeviceData deviceData = new DeviceData();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceData.setDeviceInfo(deviceInfo);

        FoundationalValidator validator = new FoundationalValidator();
        boolean isValid = validator.isValid(deviceData, null);

        assertFalse(isValid);
    }

    @Test(expected = RestClientException.class)
    public void testRestClientException() {
        DeviceData deviceData = new DeviceData();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setCertification(RegisteredDeviceConstant.L1);
        deviceData.setDeviceInfo(deviceInfo);
        deviceData.setFoundationalTrustProviderId("valid_provider_id");

        FoundationalValidator validator = Mockito.mock(FoundationalValidator.class);
        Mockito.when(validator.isValid(deviceData, null)).thenThrow(new RestClientException("Rest Client Exception"));

        validator.isValid(deviceData, null);
    }

    @Test
    public void testUnknownCertification() {
        DeviceData deviceData = new DeviceData();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setCertification("UNKNOWN");
        deviceData.setDeviceInfo(deviceInfo);
        deviceData.setFoundationalTrustProviderId("valid_provider_id");

        FoundationalValidator validator = new FoundationalValidator();
        boolean isValid = validator.isValid(deviceData, null);

        assertTrue(isValid);
    }

    @Test
    public void testEmptyTrustProviderIdWithMock() {
        DeviceData deviceData = new DeviceData();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setCertification(null);
        deviceData.setDeviceInfo(deviceInfo);

        FoundationalValidator validator = new FoundationalValidator();

        boolean isValid = validator.isValid(deviceData, null);

        assertFalse(isValid);
    }

}
