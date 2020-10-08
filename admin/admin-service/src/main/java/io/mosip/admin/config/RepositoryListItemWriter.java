package io.mosip.admin.config;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.AbstractMethodInvokingDelegator;
import org.springframework.batch.item.adapter.DynamicMethodInvocationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MethodInvoker;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
/**
 * This class will write the information in database
 * @author dhanendra
 *
 * @param <T>
 */
public class RepositoryListItemWriter<T> implements ItemWriter<T>, InitializingBean {

	private BaseRepository<?, ?> repository;
    private String methodName;

    public RepositoryListItemWriter() {
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
        System.out.println("Writing to the repository with " + items.size() + " items.");

        MethodInvoker invoker = this.createMethodInvoker(this.repository, this.methodName);
        Iterator i$ = items.iterator();

        while(i$.hasNext()) {
            Object object = i$.next();
            invoker.setArguments(new Object[]{object});
            this.doInvoke(invoker);
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
