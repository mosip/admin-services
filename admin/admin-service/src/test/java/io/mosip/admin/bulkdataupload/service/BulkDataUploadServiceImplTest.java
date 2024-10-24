package io.mosip.admin.bulkdataupload.service;

import io.mosip.admin.bulkdataupload.dto.BulkDataResponseDto;
import io.mosip.admin.bulkdataupload.entity.BaseEntity;
import io.mosip.admin.bulkdataupload.entity.BulkUploadTranscation;
import io.mosip.admin.bulkdataupload.repositories.BulkUploadTranscationRepository;
import io.mosip.admin.bulkdataupload.service.impl.BulkDataUploadServiceImpl;
import io.mosip.admin.packetstatusupdater.exception.RequestException;
import io.mosip.admin.packetstatusupdater.util.AuditUtil;
import io.mosip.admin.packetstatusupdater.util.EventEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class BulkDataUploadServiceImplTest {

    @InjectMocks
    private BulkDataUploadServiceImpl service;

    @Mock
    private AuditUtil auditUtil;

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private MultipartFile file;

    @Mock
    private BulkUploadTranscationRepository bulkTranscationRepo;

    @Test (expected = RequestException.class)
    public void testImportDataFromFile_InvalidTable() throws Exception {
        String tableName = "TABLENAME";
        String operation = "insert";
        String category = "test_category";
        Map<String, Class> entityMap = new HashMap<>();
        entityMap.put(tableName, Object.class);
        Class<?> entityClass = Object.class;

        lenient().when(file.isEmpty()).thenReturn(false);
        lenient().when(file.getOriginalFilename()).thenReturn("test.csv");
        lenient().when(jobLauncher.run(any(Job.class), any())).thenReturn(null);
        BulkUploadTranscation transaction = new BulkUploadTranscation();

        ReflectionTestUtils.invokeMethod(service, "saveTranscationDetails", 0, operation, entityClass.getSimpleName(), category, "", "PROCESSING");

        BulkDataResponseDto response = ReflectionTestUtils.invokeMethod(service, "importDataFromFile", tableName, operation, category, new MultipartFile[]{file});

        assert response != null;
        assertEquals(response.getTranscationId(), transaction.getId());
        verify(auditUtil).setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD,
                "{category:'" + category + "',tablename:'" + tableName + "',operation:'" + operation + "'}"), null);
    }

    @Test
    public void testImportDataFromFile_emptyFile_throwsException() {
        String tableName = "test_table";
        String operation = "insert";
        String category = "test_category";
        lenient().when(file.isEmpty()).thenReturn(true);

        assertThrows(RequestException.class, () -> ReflectionTestUtils.invokeMethod(service, "importDataFromFile", tableName, operation, category, new MultipartFile[]{file}));
        verify(auditUtil).setAuditRequestDto(EventEnum.BULKDATA_INVALID_ARGUMENT, null);
    }

    @Test
    public void testImportDataFromFile_invalidFileFormat_throwsException() {
        String tableName = "test_table";
        String operation = "insert";
        String category = "test_category";
        lenient().when(file.isEmpty()).thenReturn(false);
        lenient().when(file.getOriginalFilename()).thenReturn("test.txt");

        assertThrows(RequestException.class, () -> ReflectionTestUtils.invokeMethod(service, "importDataFromFile", tableName, operation, category, new MultipartFile[]{file}));
        verify(auditUtil).setAuditRequestDto(EventEnum.BULKDATA_INVALID_ARGUMENT, null);
    }

    @Test
    public void testImportDataFromFile_nullFileArray_throwsException() {
        String tableName = "test_table";
        String operation = "insert";
        String category = "test_category";

        assertThrows(RequestException.class, () -> ReflectionTestUtils.invokeMethod(service, "importDataFromFile", tableName, operation, category, null));
        verify(auditUtil).setAuditRequestDto(EventEnum.BULKDATA_INVALID_ARGUMENT, null);
    }

    @Test
    public void testImportDataFromFile_invalidOperation_throwsException() {
        String tableName = "test_table";
        String operation = "invalid";
        String category = "test_category";

        assertThrows(RequestException.class, () -> ReflectionTestUtils.invokeMethod(service, "importDataFromFile", tableName, operation, category, new MultipartFile[]{file}));
        verify(auditUtil).setAuditRequestDto(EventEnum.BULKDATA_INVALID_OPERATION, null);
    }

    @Test (expected = RequestException.class)
    public void testImportDataFromFile_RequestException_updatesTransaction() {
        String tableName = "TABLENAME";
        String operation = "insert";
        String category = "test_category";
        Map<String, Class> entityMap = new HashMap<>();
        entityMap.put(tableName, Object.class);
        Class<?> entityClass = Object.class;

        lenient().when(file.isEmpty()).thenReturn(false);
        lenient().when(file.getOriginalFilename()).thenReturn("test.csv");
        BulkUploadTranscation transaction = new BulkUploadTranscation();

        ReflectionTestUtils.invokeMethod(service, "saveTranscationDetails", 0, operation, entityClass.getSimpleName(), category, "", "PROCESSING");

        ReflectionTestUtils.invokeMethod(service, "importDataFromFile", tableName, operation, category, new MultipartFile[]{file});

        verify(auditUtil).setAuditRequestDto(EventEnum.getEventEnumWithValue(EventEnum.BULKDATA_UPLOAD_COMPLETED,
                transaction.getId() + " --> FAILED: 0, MESSAGE: IOException"), null);
    }

    @Test
    public void testUpdateBulkUploadTransaction_setsFieldsCorrectly() {
        BulkUploadTranscation transaction = new BulkUploadTranscation();
        ReflectionTestUtils.invokeMethod(service, "updateBulkUploadTransaction", transaction);

        assertEquals("COMPLETED", transaction.getStatusCode());
        assertTrue(transaction.getUploadedDateTime().toInstant().isBefore(Instant.now()));
        assertEquals("JOB", transaction.getUpdatedBy());
        verify(bulkTranscationRepo).save(transaction);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testExcelItemReader_setsPropertiesInCorrectly() throws Exception {
        String filename = "test.xlsx";
        Class<?> clazz = Object.class;
        MultipartFile mockFile = mock(MultipartFile.class);
        lenient().when(mockFile.isEmpty()).thenReturn(false);
        lenient().when(mockFile.getOriginalFilename()).thenReturn(filename);

        PoiItemReader<Object> reader = ReflectionTestUtils.invokeMethod(service, "excelItemReader", mockFile, clazz);

        assert reader != null;
        assertEquals("Excel-Reader", reader.getName());
        verify(mockFile).getInputStream();
    }

    @Test
    public void testProcessor_insertOperation_setsCreatedFields() throws Exception {
        String operation = "insert";
        String user = "test_user";
        BaseEntity entity = new BaseEntity();

        ItemProcessor processor = ReflectionTestUtils.invokeMethod(service, "processor", operation, user);
        Object processedEntity = processor.process(entity);

        assert processedEntity != null;
        assertEquals(user, ((BaseEntity) processedEntity).getCreatedBy());
        assertTrue(((BaseEntity) processedEntity).getCreatedDateTime().isBefore(LocalDateTime.now()));
        assertFalse(((BaseEntity) processedEntity).getIsDeleted());
    }

}
