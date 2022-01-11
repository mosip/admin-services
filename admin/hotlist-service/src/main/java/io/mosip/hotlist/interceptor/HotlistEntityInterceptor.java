package io.mosip.hotlist.interceptor;

import static io.mosip.hotlist.constant.HotlistErrorConstants.ENCRYPTION_DECRYPTION_FAILED;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.mosip.hotlist.entity.Hotlist;
import io.mosip.hotlist.entity.HotlistHistory;
import io.mosip.hotlist.exception.HotlistAppException;
import io.mosip.hotlist.exception.HotlistAppUncheckedException;
import io.mosip.hotlist.logger.HotlistLogger;
import io.mosip.hotlist.security.HotlistSecurityManager;
import io.mosip.kernel.core.logger.spi.Logger;

/**
 * The Class HotlistEntityInterceptor.
 *
 * @author Manoj SP
 */
@Component
public class HotlistEntityInterceptor extends EmptyInterceptor {

	private static final String ID_VALUE = "idValue";

	/** The mosip logger. */
	private transient Logger mosipLogger = HotlistLogger.getLogger(HotlistEntityInterceptor.class);

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4985336846122302850L;

	/** The Constant HOTLIST_ENTITY_INTERCEPTOR. */
	private static final String HOTLIST_ENTITY_INTERCEPTOR = "HotlistEntityInterceptor";

	/** The security manager. */
	@Autowired
	private transient HotlistSecurityManager securityManager;
	
	/**
	 * On save.
	 *
	 * @param entity the entity
	 * @param id the id
	 * @param state the state
	 * @param propertyNames the property names
	 * @param types the types
	 * @return true, if successful
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.EmptyInterceptor#onSave(java.lang.Object,
	 * java.io.Serializable, java.lang.Object[], java.lang.String[],
	 * org.hibernate.type.Type[])
	 */
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		try {
			if (entity instanceof Hotlist) {
				Hotlist hotlistEntity = (Hotlist) entity;
				String encryptedData = securityManager.encrypt(hotlistEntity.getIdValue());
				hotlistEntity.setIdValue(encryptedData);
				List<String> propertyNamesList = Arrays.asList(propertyNames);
				int indexOfData = propertyNamesList.indexOf(ID_VALUE);
				state[indexOfData] = encryptedData;
				return super.onSave(hotlistEntity, id, state, propertyNames, types);
			}
			if (entity instanceof HotlistHistory) {
				HotlistHistory hotlistEntity = (HotlistHistory) entity;
				String encryptedData = securityManager.encrypt(hotlistEntity.getIdValue());
				hotlistEntity.setIdValue(encryptedData);
				List<String> propertyNamesList = Arrays.asList(propertyNames);
				int indexOfData = propertyNamesList.indexOf(ID_VALUE);
				state[indexOfData] = encryptedData;
				return super.onSave(hotlistEntity, id, state, propertyNames, types);
			}
		} catch (HotlistAppException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_ENTITY_INTERCEPTOR, "onSave", "\n" + e.getMessage());
			throw new HotlistAppUncheckedException(ENCRYPTION_DECRYPTION_FAILED, e);
		}
		return super.onSave(entity, id, state, propertyNames, types);
	}

	/**
	 * On load.
	 *
	 * @param entity the entity
	 * @param id the id
	 * @param state the state
	 * @param propertyNames the property names
	 * @param types the types
	 * @return true, if successful
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.EmptyInterceptor#onLoad(java.lang.Object,
	 * java.io.Serializable, java.lang.Object[], java.lang.String[],
	 * org.hibernate.type.Type[])
	 */
	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		try {
			if (entity instanceof Hotlist) {
				List<String> propertyNamesList = Arrays.asList(propertyNames);
				int indexOfData = propertyNamesList.indexOf(ID_VALUE);
				state[indexOfData] = securityManager.decrypt((String) state[indexOfData]);
			}
		} catch (HotlistAppException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_ENTITY_INTERCEPTOR, "onLoad", "\n" + e.getMessage());
			throw new HotlistAppUncheckedException(ENCRYPTION_DECRYPTION_FAILED, e);
		}
		return super.onLoad(entity, id, state, propertyNames, types);
	}

	/**
	 * On flush dirty.
	 *
	 * @param entity the entity
	 * @param id the id
	 * @param currentState the current state
	 * @param previousState the previous state
	 * @param propertyNames the property names
	 * @param types the types
	 * @return true, if successful
	 */
	/* (non-Javadoc)
	 * @see org.hibernate.EmptyInterceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		try {
			if (entity instanceof Hotlist || entity instanceof HotlistHistory) {
				List<String> propertyNamesList = Arrays.asList(propertyNames);
				int indexOfData = propertyNamesList.indexOf(ID_VALUE);
				currentState[indexOfData] = securityManager.encrypt((String) currentState[indexOfData]);
				return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
			}
		} catch (HotlistAppException e) {
			mosipLogger.error(HotlistSecurityManager.getUser(), HOTLIST_ENTITY_INTERCEPTOR, "onSave", "\n" + e.getMessage());
			throw new HotlistAppUncheckedException(ENCRYPTION_DECRYPTION_FAILED, e);
		}
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}
}
