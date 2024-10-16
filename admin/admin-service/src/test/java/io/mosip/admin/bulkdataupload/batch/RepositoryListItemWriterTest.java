package io.mosip.admin.bulkdataupload.batch;

import io.mosip.admin.config.Mapper;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnitUtil;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.item.Chunk;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class RepositoryListItemWriterTest {

    @InjectMocks
    private RepositoryListItemWriter<T> writer;

    @Mock
    private EntityManager em;

    @Mock
    private EntityManagerFactory emf;

    @Mock
    private PersistenceUnitUtil util;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private BaseRepository baseRepository;

    @Mock
    private Mapper mapper;

    private T entity1;
    private T entity2;

    @Before
    public void setUp() {
        writer = new RepositoryListItemWriter<>(em, emf, T.class, mapper, applicationContext);
        writer.setMethodName("save");
        writer.setRepoBeanName("mockRepo");
        writer.setOperation("insert");

        entity1 = new T();
        entity2 = new T();
    }

    @Test
    public void testWrite_EmptyChunk() throws Exception {
        Chunk<T> emptyChunk = new Chunk<>(new ArrayList<>());
        writer.write(emptyChunk);

        assertEquals(0, emptyChunk.getItems().size());
        assertEquals(0, emptyChunk.getSkips().size());
    }

    @Test
    public void testWrite_InsertSuccess() {
        List<T> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity2);

        assertNotNull(entities);

        lenient().when(applicationContext.getBean("mockRepo")).thenReturn(baseRepository);
        lenient().when(util.getIdentifier(entity1)).thenReturn(1);
        lenient().when(util.getIdentifier(entity2)).thenReturn(2);
        lenient().when(em.find(T.class, 1)).thenReturn(null);
        lenient().when(em.find(T.class, 2)).thenReturn(null);
    }

    @Test
    public void testWrite_InsertDuplicate() {
        List<T> entities = new ArrayList<>();
        entities.add(entity1);
        Chunk<T> chunk = new Chunk<>(entities);

        assertNotNull(entities);

        lenient().when(applicationContext.getBean("mockRepo")).thenReturn(baseRepository);
        lenient().when(util.getIdentifier(entity1)).thenReturn(1);
        lenient().when(em.find(T.class, 1)).thenReturn(entity1);

        assertThrows(JobExecutionException.class, () -> writer.write(chunk));

        verify(applicationContext).getBean("mockRepo");
        verify(baseRepository, never()).save(entity1);
    }

    @Test
    public void testWrite_UpdateSuccess() {
        List<T> entities = new ArrayList<>();
        entities.add(entity1);

        assertNotNull(entities);

        writer.setOperation("update");
        lenient().when(applicationContext.getBean("mockRepo")).thenReturn(baseRepository);
        lenient().when(util.getIdentifier(entity1)).thenReturn(1);
        lenient().when(em.find(T.class, 1)).thenReturn(entity1);
    }

    @Test
    public void testWrite_UpdateNotFound() {
        List<T> entities = new ArrayList<>();
        entities.add(entity1);
        Chunk<T> chunk = new Chunk<>(entities);

        writer.setOperation("update");
        lenient().when(applicationContext.getBean("mockRepo")).thenReturn(baseRepository);
        lenient().when(util.getIdentifier(entity1)).thenReturn(1);
        lenient().when(em.find(T.class, 1)).thenReturn(null);

        assertThrows(JobExecutionException.class, () -> writer.write(chunk));

        verify(applicationContext).getBean("mockRepo");
        verify(baseRepository, never()).save(entity1);
    }

    @Test
    public void testWrite_DeleteSuccess() {
        List<T> entities = new ArrayList<>();
        entities.add(entity1);

        assertNotNull(entities);

        writer.setOperation("delete");
        lenient().when(applicationContext.getBean("mockRepo")).thenReturn(baseRepository);
        lenient().when(util.getIdentifier(entity1)).thenReturn(1);
        lenient().when(em.find(T.class, 1)).thenReturn(entity1);
    }

    @Test
    public void testWrite_DeleteNotFound() {
        List<T> entities = new ArrayList<>();
        entities.add(entity1);
        Chunk<T> chunk = new Chunk<>(entities);

        writer.setOperation("delete");
        lenient().when(applicationContext.getBean("mockRepo")).thenReturn(baseRepository);
        lenient().when(util.getIdentifier(entity1)).thenReturn(1);
        lenient().when(em.find(T.class, 1)).thenReturn(null);

        assertThrows(JobExecutionException.class, () -> writer.write(chunk));

        verify(applicationContext).getBean("mockRepo");
        verify(baseRepository, never()).save(entity1);
    }

    @Test
    public void testWrite_RepositoryException() {
        List<T> entities = new ArrayList<>();
        entities.add(entity1);
        Chunk<T> chunk = new Chunk<>(entities);

        writer.setOperation("insert");
        lenient().when(applicationContext.getBean("mockRepo")).thenReturn(baseRepository);
        lenient().when(util.getIdentifier(entity1)).thenReturn(1);

        assertThrows(JobExecutionException.class, () -> writer.write(chunk));
    }

    @Test
    public void testWrite_UnexpectedException() {
        List<T> entities = new ArrayList<>();
        entities.add(entity1);
        Chunk<T> chunk = new Chunk<>(entities);

        writer.setOperation("insert");
        lenient().when(applicationContext.getBean("mockRepo")).thenReturn(baseRepository);
        lenient().when(util.getIdentifier(entity1)).thenReturn(1);

        assertThrows(JobExecutionException.class, () -> writer.write(chunk));
    }

}
