package io.mosip.admin.bulkdataupload.batch;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.batch.support.DefaultPropertyEditorRegistrar;
import org.springframework.beans.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CustomExcelRowMapper<T>  extends DefaultPropertyEditorRegistrar implements RowMapper<T>, BeanFactoryAware, InitializingBean {
    private String name;
    private Class<? extends T> type;
    private BeanFactory beanFactory;
    private final ConcurrentMap<CustomExcelRowMapper.DistanceHolder, ConcurrentMap<String, String>> propertiesMatched = new ConcurrentHashMap();
    private int distanceLimit = 5;
    private boolean strict = true;
    private ConversionService conversionService;
    private Validator validator;

    public CustomExcelRowMapper(ConversionService conversionService,Validator validator ) {
        this.conversionService = conversionService;
        this.validator = validator;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setDistanceLimit(int distanceLimit) {
        this.distanceLimit = distanceLimit;
    }

    public void setPrototypeBeanName(String name) {
        this.name = name;
    }

    public void setTargetType(Class<? extends T> type) {
        this.type = type;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.state(this.name != null || this.type != null, "Either name or type must be provided.");
        Assert.state(this.name == null || this.type == null, "Both name and type cannot be specified together.");
    }

    public T mapRow(RowSet rs) throws BindException {
        T copy = this.getBean();
        DataBinder binder = this.createBinder(copy);
        binder.setConversionService(this.conversionService);
        binder.bind(new MutablePropertyValues(this.getBeanProperties(copy, rs.getProperties())));

	    Set<ConstraintViolation<T>> violations = validator.validate(copy);
	    if (!violations.isEmpty()) {
	      throw new ConstraintViolationException(violations);
	    }

        
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        } else {
            return copy;
        }
    }

    protected DataBinder createBinder(Object target) {
        DataBinder binder = new DataBinder(target);
        binder.setIgnoreUnknownFields(!this.strict);
        this.initBinder(binder);
        this.registerCustomEditors(binder);
        return binder;
    }

    protected void initBinder(DataBinder binder) {
    }

    private T getBean() {
        return this.name != null ? (T) this.beanFactory.getBean(this.name) : BeanUtils.instantiateClass(this.type);
    }

    private Properties getBeanProperties(Object bean, Properties properties) {
        Class<?> cls = bean.getClass();
        CustomExcelRowMapper.DistanceHolder distanceKey = new CustomExcelRowMapper.DistanceHolder(cls, this.distanceLimit);
        if (!this.propertiesMatched.containsKey(distanceKey)) {
            this.propertiesMatched.putIfAbsent(distanceKey, new ConcurrentHashMap());
        }

        Map<String, String> matches = new HashMap(this.propertiesMatched.get(distanceKey));
        Set<String> keys = new HashSet(properties.keySet());
        Iterator var7 = keys.iterator();

        while(var7.hasNext()) {
            String key = (String)var7.next();
            if (matches.containsKey(key)) {
                this.switchPropertyNames(properties, key, matches.get(key));
            } else {
                String name = this.findPropertyName(bean, key);
                if (name != null) {
                    if (matches.containsValue(name)) {
                        throw new NotWritablePropertyException(cls, name, "Duplicate match with distance <= " + this.distanceLimit + " found for this property in input keys: " + keys + ". (Consider reducing the distance limit or changing the input key names to get a closer match.)");
                    }

                    matches.put(key, name);
                    this.switchPropertyNames(properties, key, name);
                }
            }
        }

        this.propertiesMatched.replace(distanceKey, new ConcurrentHashMap(matches));
        return properties;
    }

    private String findPropertyName(Object bean, String key) {
        if (bean == null) {
            return null;
        } else {
            Class<?> cls = bean.getClass();
            int index = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(key);
            String prefix;
            String suffix;
            String name;
            if (index > 0) {
                prefix = key.substring(0, index);
                suffix = key.substring(index + 1);
                name = this.findPropertyName(bean, prefix);
                if (name == null) {
                    return null;
                } else {
                    Object nestedValue = this.getPropertyValue(bean, name);
                    String nestedPropertyName = this.findPropertyName(nestedValue, suffix);
                    return nestedPropertyName != null ? name + "." + nestedPropertyName : null;
                }
            } else {
                name = null;
                int distance = 0;
                index = key.indexOf(91);
                if (index > 0) {
                    prefix = key.substring(0, index);
                    suffix = key.substring(index);
                } else {
                    prefix = key;
                    suffix = "";
                }

                for(; name == null && distance <= this.distanceLimit; ++distance) {
                    String[] candidates = PropertyMatches.forProperty(prefix, cls, distance).getPossibleMatches();
                    if (candidates.length == 1) {
                        String candidate = candidates[0];
                        if (candidate.equals(prefix)) {
                            name = key;
                        } else {
                            name = candidate + suffix;
                        }
                    }
                }

                return name;
            }
        }
    }

    private Object getPropertyValue(Object bean, String nestedName) {
        BeanWrapperImpl wrapper = new BeanWrapperImpl(bean);
        wrapper.setAutoGrowNestedPaths(true);
        Object nestedValue = wrapper.getPropertyValue(nestedName);
        if (nestedValue == null) {
            nestedValue = BeanUtils.instantiateClass(wrapper.getPropertyType(nestedName));
            wrapper.setPropertyValue(nestedName, nestedValue);
        }

        return nestedValue;
    }

    private void switchPropertyNames(Properties properties, String oldName, String newName) {
        String value = properties.getProperty(oldName);
        properties.remove(oldName);
        properties.setProperty(newName, value);
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    private static class DistanceHolder {
        private final Class<?> cls;
        private final int distance;

        DistanceHolder(Class<?> cls, int distance) {
            this.cls = cls;
            this.distance = distance;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (this.getClass() != obj.getClass()) {
                return false;
            } else {
                CustomExcelRowMapper.DistanceHolder other = (CustomExcelRowMapper.DistanceHolder)obj;
                if (this.cls == null) {
                    if (other.cls != null) {
                        return false;
                    }
                } else if (!this.cls.equals(other.cls)) {
                    return false;
                }

                return this.distance == other.distance;
            }
        }

        public int hashCode() {
            int result = 1;
            result = 31 * result + (this.cls == null ? 0 : this.cls.hashCode());
            result = 31 * result + this.distance;
            return result;
        }
    }
}
