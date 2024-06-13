package io.mosip.kernel.syncdata.test.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.authmanager.model.AuthNResponse;
import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.signatureutil.model.SignatureResponse;
import io.mosip.kernel.core.signatureutil.spi.SignatureUtil;
import io.mosip.kernel.syncdata.constant.SyncAuthErrorCode;
import io.mosip.kernel.syncdata.exception.RequestException;
import io.mosip.kernel.syncdata.service.impl.SyncAuthTokenServiceImpl;
import io.mosip.kernel.syncdata.test.TestBootApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestBootApplication.class)
@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
public class SyncAuthTokenControllerTest {

    private SignatureResponse signResponse;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SignatureUtil signingUtil;

    @Mock
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Mock
    private SyncAuthTokenServiceImpl syncAuthTokenService;

    private RequestWrapper<String> requestWrapper;

    @Before
    public void setup() {
        signResponse = new SignatureResponse();
        signResponse.setData("asdasdsadf4e");
        signResponse.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
        lenient().when(signingUtil.sign(Mockito.anyString())).thenReturn(signResponse);

        requestWrapper = new RequestWrapper<>();
        requestWrapper.setRequest("asdfasdfasdfads");
        requestWrapper.setRequesttime(LocalDateTime.now(ZoneOffset.UTC));
        requestWrapper.setId("");
        requestWrapper.setVersion("0.1");
    }

    @Test
    public void getTokenWithUserIdPwdSuccess() {
        try {
            lenient().when(syncAuthTokenService.getAuthToken(Mockito.anyString()))
                    .thenReturn("testestsetst");
            mockMvc.perform(post("/authenticate/useridpwd").contentType(MediaType.APPLICATION_JSON).
                    content(objectMapper.writeValueAsString(requestWrapper))).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTokenWithUserIdPwdFailure() {
        try {
            lenient().when(syncAuthTokenService.getAuthToken(Mockito.anyString())).thenThrow(new RequestException(SyncAuthErrorCode.INVALID_REQUEST.getErrorCode(),
                    SyncAuthErrorCode.INVALID_REQUEST.getErrorMessage()));
            MvcResult mvcResult = mockMvc.perform(post("/authenticate/useridpwd").contentType(MediaType.APPLICATION_JSON).
                    content(objectMapper.writeValueAsString(requestWrapper))).andExpect(status().isOk()).andReturn();

        ResponseWrapper<String> responseWrapper = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<ResponseWrapper<String>>() {});

            assertEquals(SyncAuthErrorCode.INVALID_REQUEST.getErrorCode(), responseWrapper.getErrors().get(0).getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendOTPSuccess() {
        try {
            ResponseWrapper<AuthNResponse> resp = new ResponseWrapper<>();
            AuthNResponse authNResponse = new AuthNResponse();
            authNResponse.setStatus("Success");
            authNResponse.setMessage("Otp sent successfully");
            resp.setResponse(authNResponse);
            lenient().when(syncAuthTokenService.sendOTP(Mockito.anyString())).thenReturn(resp);
            mockMvc.perform(post("/authenticate/sendotp").contentType(MediaType.APPLICATION_JSON).
                    content(objectMapper.writeValueAsString(requestWrapper))).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendOTPFailure() {
        try {
            lenient().when(syncAuthTokenService.sendOTP(Mockito.anyString())).thenThrow(new RequestException(SyncAuthErrorCode.INVALID_REQUEST.getErrorCode(),
                    SyncAuthErrorCode.INVALID_REQUEST.getErrorMessage()));
            MvcResult mvcResult = mockMvc.perform(post("/authenticate/sendotp").contentType(MediaType.APPLICATION_JSON).
                    content(objectMapper.writeValueAsString(requestWrapper))).andExpect(status().isOk()).andReturn();

            ResponseWrapper<String> responseWrapper = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                    new TypeReference<ResponseWrapper<String>>() {});

            assertEquals(SyncAuthErrorCode.INVALID_REQUEST.getErrorCode(), responseWrapper.getErrors().get(0).getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
