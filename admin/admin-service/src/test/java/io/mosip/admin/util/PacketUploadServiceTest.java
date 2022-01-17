package io.mosip.admin.util;

import com.amazonaws.util.StringInputStream;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.admin.TestBootApplication;
import io.mosip.admin.bulkdataupload.dto.MachineRegistrationCenterDto;
import io.mosip.admin.bulkdataupload.dto.PacketUploadStatus;
import io.mosip.admin.bulkdataupload.dto.PageDto;
import io.mosip.admin.bulkdataupload.dto.ValueDto;
import io.mosip.admin.bulkdataupload.service.PacketUploadService;
import io.mosip.commons.packet.dto.Packet;
import io.mosip.commons.packet.exception.PacketKeeperException;
import io.mosip.commons.packet.impl.OnlinePacketCryptoServiceImpl;
import io.mosip.commons.packet.keeper.PacketKeeper;
import io.mosip.kernel.core.http.ResponseWrapper;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestBootApplication.class)
public class PacketUploadServiceTest {

    @Autowired
    private PacketUploadService packetUploadService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("OnlinePacketCryptoServiceImpl")
    private OnlinePacketCryptoServiceImpl onlineCrypto;

    @Autowired
    private PacketKeeper packetKeeper;

    @Value("${mosip.admin.packetupload.packetsync.url}")
    private String packetSyncURL;

    @Value("${mosip.kernel.packet-reciever-api-url}")
    private String packetReceiverURL;

    @Value("${MACHINE_GET_API}")
    private String MACHINE_GET_API;

    @Value("${object.store.base.location:home}")
    private String baseLocation;

    @Value("${packet.manager.account.name:PACKET_MANAGER_ACCOUNT}")
    private String account;

    private MockRestServiceServer mockRestServiceServer;

    @Before
    public void setup() throws PacketKeeperException, IOException {
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();

        List<ValueDto> names = new ArrayList<>();
        names.add(new ValueDto("eng", "testuser"));
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", names);
        map.put("email", "email");
        map.put("phone", "2342424234");
        LinkedHashMap<String, Object> idjson = new LinkedHashMap<>();
        idjson.put("identity", map);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try(ZipOutputStream zos = new ZipOutputStream(bos)) {
            ZipEntry entry = new ZipEntry("ID.json");
            zos.putNextEntry(entry);
            zos.write(objectMapper.writeValueAsBytes(idjson));
        }

        Packet packet = new Packet();
        packet.setPacket(bos.toByteArray());
        Mockito.when(packetKeeper.getPacket(Mockito.any())).thenReturn(packet);
    }

    @Test
    public void getMachineListTest() throws JsonProcessingException {
        ResponseWrapper<PageDto<List<MachineRegistrationCenterDto>>> responseWrapper = new ResponseWrapper<>();
        MachineRegistrationCenterDto machineRegistrationCenterDto = new MachineRegistrationCenterDto();
        machineRegistrationCenterDto.setId("1111");
        machineRegistrationCenterDto.setRegCentId("111111");
        List<MachineRegistrationCenterDto> list = new ArrayList<>();
        list.add(machineRegistrationCenterDto);
        PageDto pageDto = new PageDto();
        pageDto.setPageNo(0);
        pageDto.setData(list);
        pageDto.setTotalPages(1);
        pageDto.setTotalItems(1);
        responseWrapper.setResponse(pageDto);
        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(
                        MACHINE_GET_API + "111111?pageNumber=0"))
                .andRespond(MockRestResponseCreators.withSuccess()
                        .body(objectMapper.writeValueAsString(responseWrapper))
                        .contentType(MediaType.APPLICATION_JSON_UTF8));

        List<MachineRegistrationCenterDto> result = packetUploadService.getMachineList("111111");
        Assert.assertNotNull(result);
        Assert.assertEquals(list.size(), result.size());
        Assert.assertEquals(list.get(0).getId(), result.get(0).getId());
    }

    @Test
    @WithUserDetails("packet-admin")
    public void syncAndUploadPacketTest() throws IOException, JSONException {
        ResponseWrapper<PageDto<List<MachineRegistrationCenterDto>>> responseWrapper = new ResponseWrapper<>();
        MachineRegistrationCenterDto machineRegistrationCenterDto = new MachineRegistrationCenterDto();
        machineRegistrationCenterDto.setId("10107");
        machineRegistrationCenterDto.setRegCentId("10003");
        List<MachineRegistrationCenterDto> list = new ArrayList<>();
        list.add(machineRegistrationCenterDto);
        PageDto pageDto = new PageDto();
        pageDto.setPageNo(0);
        pageDto.setData(list);
        pageDto.setTotalPages(1);
        pageDto.setTotalItems(1);
        responseWrapper.setResponse(pageDto);
        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(
                        MACHINE_GET_API + "10003?pageNumber=0"))
                .andRespond(MockRestResponseCreators.withSuccess()
                        .body(objectMapper.writeValueAsString(responseWrapper))
                        .contentType(MediaType.APPLICATION_JSON_UTF8));

        ResponseWrapper<String> syncResponse = new ResponseWrapper<>();
        syncResponse.setResponse("successful rid sync");
        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(packetSyncURL))
                .andRespond(MockRestResponseCreators.withSuccess()
                        .body(objectMapper.writeValueAsString(syncResponse))
                        .contentType(MediaType.APPLICATION_JSON_UTF8));

        ResponseWrapper<String> uploadResponse = new ResponseWrapper<>();
        uploadResponse.setResponse("successful");
        uploadResponse.setErrors(null);
        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo(packetReceiverURL))
                .andRespond(MockRestResponseCreators.withSuccess()
                        .body(objectMapper.writeValueAsString(uploadResponse))
                        .contentType(MediaType.APPLICATION_JSON_UTF8));

        Mockito.when(onlineCrypto.decrypt(Mockito.anyString(), Mockito.any())).thenReturn("decrypted-bytest".getBytes(StandardCharsets.UTF_8));
        Mockito.when(onlineCrypto.encrypt(Mockito.anyString(), Mockito.any())).thenReturn("encrypted-bytest".getBytes(StandardCharsets.UTF_8));

        PacketUploadStatus packetUploadStatus = packetUploadService.syncAndUploadPacket(
                new MockMultipartFile("10003101070000220211225191146-10003_10107-20211225191543",
                        "10003101070000220211225191146-10003_10107-20211225191543.zip",
                        "application/zip",
                        new StringInputStream("dsdgsdfgsdfgdfgdfgsdfgsdfgsdfgsdfgsdfgsdfg")),
                "10003",
                "APPROVED",
                "REGISTRATION_CLIENT",
                "NEW",
                "erwrwerwerwerwer");

        Assert.assertNotNull(packetUploadStatus);
        Assert.assertFalse(packetUploadStatus.isFailed());
    }
}
