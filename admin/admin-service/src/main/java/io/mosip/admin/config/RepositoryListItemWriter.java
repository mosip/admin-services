package io.mosip.admin.config;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.AbstractMethodInvokingDelegator;
import org.springframework.batch.item.adapter.DynamicMethodInvocationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MethodInvoker;

import io.mosip.admin.bulkdataupload.constant.BulkUploadErrorCode;
import io.mosip.admin.bulkdataupload.entity.Device;
import io.mosip.admin.bulkdataupload.entity.DeviceHistory;
import io.mosip.admin.bulkdataupload.entity.Machine;
import io.mosip.admin.bulkdataupload.entity.MachineHistory;
import io.mosip.admin.bulkdataupload.entity.UserDetails;
import io.mosip.admin.bulkdataupload.entity.UserDetailsHistory;
import io.mosip.admin.bulkdataupload.entity.ZoneUser;
import io.mosip.admin.bulkdataupload.entity.ZoneUserHistory;
import io.mosip.admin.packetstatusupdater.exception.RequestException;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

/**
 * This class will write the information in database
 * @author dhanendra
 *
 * @param <T>
 */
public class RepositoryListItemWriter<T> implements ItemWriter<T>, InitializingBean {
	private static final Logger LOGGER =  LoggerFactory.getLogger(RepositoryListItemWriter.class);
	private BaseRepository<?, ?> repository;
    private String methodName;
    private EntityManager em;
    private EntityManagerFactory emf;
    private Class<?> entity;
    private Mapper mapper;
    private ApplicationContext applicationContext;
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

	public void setRepository(BaseRepository<?, ?> crudRepository) {
        this.repository = crudRepository;
    }

    public void write(List<? extends T> items) throws Exception {
        if(!CollectionUtils.isEmpty(items)) {
            this.doWrite(items);
        }
    }

    protected void doWrite(List<? extends T> items) throws Exception {
    	LOGGER.info("SESSIONID", "bulkupload", "masterdata", "Writing to the repository with " + items.size() + " items.");

        MethodInvoker invoker = this.createMethodInvoker(this.repository, this.methodName);
        Iterator i$ = items.iterator();
        MethodInvoker historyInvoker = null;
        while(i$.hasNext()) {
        	Object object = i$.next();
            PersistenceUnitUtil util = emf.getPersistenceUnitUtil();
			Object projectId = util.getIdentifier(object);
			T machin = (T) em.find(entity, projectId);
			if(machin !=null) {
				throw new RequestException(BulkUploadErrorCode.DUPLICATE_RECORD.getErrorCode(),
						BulkUploadErrorCode.DUPLICATE_RECORD.getErrorMessage());
			}
            invoker.setArguments(new Object[]{object});
            this.doInvoke(invoker);
            String repoBeanName1;
            BaseRepository baserepo1;
            switch(entity.getCanonicalName()) {
            case "io.mosip.admin.bulkdataupload.entity.ZoneUser":
        		repoBeanName1=mapper.getRepo(ZoneUserHistory.class);
        		baserepo1 = (BaseRepository) applicationContext.getBean(repoBeanName1);
        		ZoneUserHistory userHistory = new ZoneUserHistory();
        		MapperUtils.map(object, userHistory);
				MapperUtils.setBaseFieldValue(object, userHistory);
				userHistory.setEffDTimes(userHistory.getCreatedDateTime());
				baserepo1.save(userHistory);
            break;
            case "io.mosip.admin.bulkdataupload.entity.UserDetails":
        		repoBeanName1=mapper.getRepo(UserDetailsHistory.class);
        		baserepo1 = (BaseRepository) applicationContext.getBean(repoBeanName1);
        		UserDetailsHistory userDetailHistory = new UserDetailsHistory();
        		MapperUtils.map(object, userDetailHistory);
				MapperUtils.setBaseFieldValue(object, userDetailHistory);
				userDetailHistory.setEffDTimes(userDetailHistory.getCreatedDateTime());
				baserepo1.save(userDetailHistory);
            break;
            case "io.mosip.admin.bulkdataupload.entity.Machine":
        		repoBeanName1=mapper.getRepo(MachineHistory.class);
        		baserepo1 = (BaseRepository) applicationContext.getBean(repoBeanName1);
        		MachineHistory machineHistory = new MachineHistory();
        		MapperUtils.map(object, machineHistory);
				MapperUtils.setBaseFieldValue(object, machineHistory);
				machineHistory.setEffectDateTime(machineHistory.getCreatedDateTime());
				baserepo1.save(machineHistory);
            break;
            case "io.mosip.admin.bulkdataupload.entity.Device":
        		repoBeanName1=mapper.getRepo(DeviceHistory.class);
        		baserepo1 = (BaseRepository) applicationContext.getBean(repoBeanName1);
        		DeviceHistory deviceHistory = new DeviceHistory();
        		MapperUtils.map(object, deviceHistory);
				MapperUtils.setBaseFieldValue(object, deviceHistory);
				deviceHistory.setEffectDateTime(deviceHistory.getCreatedDateTime());
				baserepo1.save(deviceHistory);
			break;
			default:
			break;
            }
        }

    }

    public void afterPropertiesSet() throws Exception {
        Assert.state(this.repository != null, "A CrudRepository implementation is required");
    }

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
