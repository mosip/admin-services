package io.mosip.admin.bulkdataupload.batch;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;

import io.mosip.admin.bulkdataupload.entity.*;
import io.mosip.admin.config.Mapper;
import io.mosip.admin.config.MapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.AbstractMethodInvokingDelegator;
import org.springframework.batch.item.adapter.DynamicMethodInvocationException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MethodInvoker;

import io.mosip.admin.bulkdataupload.constant.BulkUploadErrorCode;
import io.mosip.admin.packetstatusupdater.exception.RequestException;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * This class will write the information in database
 * @author dhanendra
 *
 * @param <T>
 */
public class RepositoryListItemWriter<T> implements ItemWriter<T> {
	private static final Logger LOGGER =  LoggerFactory.getLogger(RepositoryListItemWriter.class);

    private String methodName;
    private EntityManager em;
    private EntityManagerFactory emf;
    private Class<?> entity;
    private Mapper mapper;
    private ApplicationContext applicationContext;
    private String repoBeanName;
    private String operation;

    public RepositoryListItemWriter() {
    }
    
    public RepositoryListItemWriter(EntityManager em,EntityManagerFactory emf,Class<?> entity,Mapper mapper,ApplicationContext applicationContext) {
    	this.em=em;
    	this.emf=emf;
    	this.entity=entity;
    	this.mapper=mapper;
    	this.applicationContext=applicationContext;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setRepoBeanName(String repoBeanName) {
        this.repoBeanName = repoBeanName;
    }

    public void write(List<? extends T> items) throws Exception {
        if(!CollectionUtils.isEmpty(items)) {
            this.doWrite(items);
        }
    }

    protected void doWrite(List<? extends T> items) throws Exception {
    	LOGGER.info("Writing to the repository with " + items.size() + " items.");
        try {
            BaseRepository baseRepository = (BaseRepository) applicationContext.getBean(this.repoBeanName);
            Iterator i$ = items.iterator();
            while(i$.hasNext()) {
                Object object = i$.next();
                PersistenceUnitUtil util = emf.getPersistenceUnitUtil();
                Object identifier = util.getIdentifier(object);
                T existingRecord = (T) em.find(entity, identifier);
                switch (this.operation) {
                    case "insert":
                        MethodInvoker invoker = this.createMethodInvoker(baseRepository, this.methodName);
                        if(existingRecord !=null) {
                            throw new RequestException(BulkUploadErrorCode.ENTRY_EXISTS_SAME_IDENTIFIER.getErrorCode(),
                                    "Entry already exists with this id >> " + identifier);
                        }
                        invoker.setArguments(new Object[]{object});
                        this.doInvoke(invoker);
                        break;

                    case "update":
                        BaseRepository baseRepositoryToUpdate = (BaseRepository) applicationContext.getBean(this.repoBeanName);
                        if(existingRecord == null) {
                            throw new RequestException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
                                    "No entry found with this id >> " + identifier);
                        }
                        ((BaseEntity)object).setCreatedBy(((BaseEntity)existingRecord).getCreatedBy());
                        ((BaseEntity)object).setCreatedDateTime(((BaseEntity)existingRecord).getCreatedDateTime());
                        ((BaseEntity)object).setIsDeleted(((BaseEntity)existingRecord).getIsDeleted());
                        baseRepositoryToUpdate.save(object);
                        break;

                    case "delete":
                        BaseRepository baseRepositoryToDelete = (BaseRepository) applicationContext.getBean(this.repoBeanName);
                        if(existingRecord == null) {
                            throw new RequestException(BulkUploadErrorCode.BULK_OPERATION_ERROR.getErrorCode(),
                                    "No entry found with this id >> " + identifier);
                        }
                        ((BaseEntity)object).setCreatedBy(((BaseEntity)existingRecord).getCreatedBy());
                        ((BaseEntity)object).setCreatedDateTime(((BaseEntity)existingRecord).getCreatedDateTime());
                        ((BaseEntity)object).setUpdatedBy(((BaseEntity)existingRecord).getUpdatedBy());
                        ((BaseEntity)object).setUpdatedDateTime(((BaseEntity)existingRecord).getUpdatedDateTime());
                        baseRepositoryToDelete.save(object);
                        break;
                }
                createHistoryRecord(object);
            }
        } catch (Throwable t) {
            LOGGER.error(BulkUploadErrorCode.BATCH_ERROR.getErrorCode(), t);
            throw new JobExecutionException(t.getMessage() + (t.getCause() != null ? t.getCause().getMessage() : ""));
        }
    }

    private void createHistoryRecord(Object object) {
        String historyRepoBeanName;
        BaseRepository historyBaseRepo;
        switch(entity.getCanonicalName()) {
            case "io.mosip.admin.bulkdataupload.entity.ZoneUser":
                historyRepoBeanName=mapper.getRepo(ZoneUserHistory.class);
                historyBaseRepo = (BaseRepository) applicationContext.getBean(historyRepoBeanName);
                ZoneUserHistory userHistory = new ZoneUserHistory();
                MapperUtils.map(object, userHistory);
                MapperUtils.setBaseFieldValue(object, userHistory);
                userHistory.setEffDTimes(userHistory.getCreatedDateTime());
                historyBaseRepo.save(userHistory);
                break;
            case "io.mosip.admin.bulkdataupload.entity.UserDetails":
                historyRepoBeanName=mapper.getRepo(UserDetailsHistory.class);
                historyBaseRepo = (BaseRepository) applicationContext.getBean(historyRepoBeanName);
                UserDetailsHistory userDetailHistory = new UserDetailsHistory();
                MapperUtils.map(object, userDetailHistory);
                MapperUtils.setBaseFieldValue(object, userDetailHistory);
                userDetailHistory.setEffDTimes(userDetailHistory.getCreatedDateTime());
                historyBaseRepo.save(userDetailHistory);
                break;
            case "io.mosip.admin.bulkdataupload.entity.Machine":
                historyRepoBeanName=mapper.getRepo(MachineHistory.class);
                historyBaseRepo = (BaseRepository) applicationContext.getBean(historyRepoBeanName);
                MachineHistory machineHistory = new MachineHistory();
                MapperUtils.map(object, machineHistory);
                MapperUtils.setBaseFieldValue(object, machineHistory);
                machineHistory.setEffectDateTime(machineHistory.getCreatedDateTime());
                historyBaseRepo.save(machineHistory);
                break;
            case "io.mosip.admin.bulkdataupload.entity.Device":
                historyRepoBeanName=mapper.getRepo(DeviceHistory.class);
                historyBaseRepo = (BaseRepository) applicationContext.getBean(historyRepoBeanName);
                DeviceHistory deviceHistory = new DeviceHistory();
                MapperUtils.map(object, deviceHistory);
                MapperUtils.setBaseFieldValue(object, deviceHistory);
                deviceHistory.setEffectDateTime(deviceHistory.getCreatedDateTime());
                historyBaseRepo.save(deviceHistory);
                break;
            case "io.mosip.admin.bulkdataupload.entity.RegistrationCenter":
                historyRepoBeanName=mapper.getRepo(RegistrationCenterHistory.class);
                historyBaseRepo = (BaseRepository) applicationContext.getBean(historyRepoBeanName);
                RegistrationCenterHistory registrationCenterHistory = new RegistrationCenterHistory();
                MapperUtils.map(object, registrationCenterHistory);
                MapperUtils.setBaseFieldValue(object, registrationCenterHistory);
                registrationCenterHistory.setEffectivetimes(registrationCenterHistory.getCreatedDateTime());
                historyBaseRepo.save(registrationCenterHistory);
                break;
            default:
                break;
        }
    }

    /*public void afterPropertiesSet() throws Exception {
        Assert.state(this.repository != null, "A CrudRepository implementation is required");
    }*/

    private Object doInvoke(MethodInvoker invoker) throws Exception {
        try {
            invoker.prepare();
        } catch (ClassNotFoundException var3) {
            throw new DynamicMethodInvocationException(var3);
        } catch (NoSuchMethodException var4) {
            throw new DynamicMethodInvocationException(var4);
        }

        try {
            return invoker.invoke();
        } catch (InvocationTargetException var5) {
            if(var5.getCause() instanceof Exception) {
                throw (Exception)var5.getCause();
            } else {
                throw new AbstractMethodInvokingDelegator.InvocationTargetThrowableWrapper(var5.getCause());
            }
        } catch (IllegalAccessException var6) {
            throw new DynamicMethodInvocationException(var6);
        }
    }

    private MethodInvoker createMethodInvoker(Object targetObject, String targetMethod) {
        MethodInvoker invoker = new MethodInvoker();
        invoker.setTargetObject(targetObject);
        invoker.setTargetMethod(targetMethod);
        return invoker;
    }
}
