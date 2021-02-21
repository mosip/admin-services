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
import io.mosip.hotlist.security.HotlistSecurityManager;

@Component
public class HotlistEntityInterceptor extends EmptyInterceptor {

	/** The mosip logger. */
//	private transient Logger mosipLogger = IdRepoLogger.getLogger(IdRepoEntityInterceptor.class);

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4985336846122302850L;

	/** The security manager. */
	@Autowired
	private HotlistSecurityManager securityManager;
	
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
				int indexOfData = propertyNamesList.indexOf("idValue");
				state[indexOfData] = encryptedData;
				return super.onSave(hotlistEntity, id, state, propertyNames, types);
			}
			if (entity instanceof HotlistHistory) {
				HotlistHistory hotlistEntity = (HotlistHistory) entity;
				String encryptedData = securityManager.encrypt(hotlistEntity.getIdValue());
				hotlistEntity.setIdValue(encryptedData);
				List<String> propertyNamesList = Arrays.asList(propertyNames);
				int indexOfData = propertyNamesList.indexOf("idValue");
				state[indexOfData] = encryptedData;
				return super.onSave(hotlistEntity, id, state, propertyNames, types);
			}
		} catch (HotlistAppException e) {
//			mosipLogger.error(IdRepoSecurityManager.getUser(), ID_REPO_ENTITY_INTERCEPTOR, "onSave", "\n" + e.getMessage());
			throw new HotlistAppUncheckedException(ENCRYPTION_DECRYPTION_FAILED, e);
		}
		return super.onSave(entity, id, state, propertyNames, types);
	}

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
				int indexOfData = propertyNamesList.indexOf("idValue");
				state[indexOfData] = securityManager.decrypt((String) state[indexOfData]);
			}
		} catch (HotlistAppException e) {
//			mosipLogger.error(IdRepoSecurityManager.getUser(), ID_REPO_ENTITY_INTERCEPTOR, "onLoad", "\n" + e.getMessage());
			throw new HotlistAppUncheckedException(ENCRYPTION_DECRYPTION_FAILED, e);
		}
		return super.onLoad(entity, id, state, propertyNames, types);
	}

	/* (non-Javadoc)
	 * @see org.hibernate.EmptyInterceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		try {
			if (entity instanceof Hotlist || entity instanceof HotlistHistory) {
				List<String> propertyNamesList = Arrays.asList(propertyNames);
				int indexOfData = propertyNamesList.indexOf("idValue");
				currentState[indexOfData] = securityManager.encrypt((String) currentState[indexOfData]);
				return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
			}
		} catch (HotlistAppException e) {
//			mosipLogger.error(IdRepoSecurityManager.getUser(), ID_REPO_ENTITY_INTERCEPTOR, "onSave", "\n" + e.getMessage());
			throw new HotlistAppUncheckedException(ENCRYPTION_DECRYPTION_FAILED, e);
		}
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}
}
