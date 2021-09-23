/**
 * 
 */
package io.mosip.kernel.masterdata.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.dataaccess.hibernate.constant.HibernateErrorCode;
import io.mosip.kernel.masterdata.constant.RegistrationCenterErrorCode;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;

/**
 * @author Ramadurai Pandian
 *
 */
@Repository
@Transactional
public class MasterdataCreationUtil {

	private static final String LANGCODE_COLUMN_NAME = "langCode";

	private static final String ID_COLUMN_NAME = "id";

	private static final String CODE_COLUMN_NAME = "code";

	private static final String ISACTIVE_COLUMN_NAME = "isActive";

	private static final String NAME_COLUMN_NAME = "name";
	
	private static final String UPD_BY_COLUMN_NAME = "updatedBy";
	
	private static final String UPDA_DTIMES_COLUMN_NAME = "updatedDateTime";

	private static String contextUser = "superadmin";



	@Value("#{'${mosip.mandatory-languages:}'.concat('${mosip.optional-languages:}')}")
	private String supportedLang;

	/**
	 * Field for interface used to interact with the persistence context.
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Constructor for MasterdataSearchHelper having EntityManager
	 * 
	 * @param entityManager The entityManager
	 */
	public MasterdataCreationUtil(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	public <E, T> T createMasterData(Class<E> entity, T t)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String langCode = null, id = null;
		String primaryId = null;
		boolean activeDto = false, activePrimary = false;
		String primaryKeyCol = null;
		Class<?> dtoClass = t.getClass();
		boolean priSecIdentical = false;
		for (Field entField : entity.getDeclaredFields()) {
			primaryKeyCol = setPrimaryKeyColAndEntField(primaryKeyCol, entField);
		}
		for (Field field : dtoClass.getDeclaredFields()) {
			field.setAccessible(true);
			if (field.getName() != null && field.getName().equals(LANGCODE_COLUMN_NAME)) {
				langCode = (String) field.get(t);
			}

			if (field.getName() != null && field.getName().equals(primaryKeyCol)) {
				id = (String) field.get(t);
			}

			//NOTE: is_active flag update should be handled only with patch API
			if (field.getName() != null && field.getName().equals(ISACTIVE_COLUMN_NAME)) {
				activeDto = false; //(boolean) field.get(t);
			}
		}
		return callMethodBasedOnFilters(entity, t, langCode, id, primaryId, activeDto, activePrimary, primaryKeyCol,
				dtoClass, priSecIdentical);

	}

	private <T, E> T callMethodBasedOnFilters(Class<E> entity, T t, String langCode, String id, String primaryId,
			boolean activeDto, boolean activePrimary, String primaryKeyCol, Class<?> dtoClass, boolean priSecIdentical)
			throws NoSuchFieldException, IllegalAccessException {
		if(langCode == null)
			return t;

		if (supportedLang.contains(langCode))
			return t;

		throw new MasterDataServiceException(RegistrationCenterErrorCode.LANGUAGE_EXCEPTION.getErrorCode(),
				String.format(RegistrationCenterErrorCode.LANGUAGE_EXCEPTION.getErrorMessage(), langCode));
	}

	private String setPrimaryKeyColAndEntField(String primaryKeyCol, Field entField) {
		if (entField.isAnnotationPresent(Id.class)) {
			entField.setAccessible(true);
			if (entField.getName() != null && !entField.getName().equals(LANGCODE_COLUMN_NAME)) {
				primaryKeyCol = entField.getName();
			}
		}
		return primaryKeyCol;
	}

	private <T> void setIsActive(Class<?> dtoClass, boolean activeDto, T t, boolean priSecIdentical,Field isActive)
			throws NoSuchFieldException, IllegalAccessException {
		
		if (activeDto && priSecIdentical) {
			isActive = dtoClass.getDeclaredField(ISACTIVE_COLUMN_NAME);
			isActive.setAccessible(true);
			isActive.set(t, Boolean.TRUE);
		} else {
			isActive = dtoClass.getDeclaredField(ISACTIVE_COLUMN_NAME);
			isActive.setAccessible(true);
			isActive.set(t, Boolean.FALSE);
		}
	}



	private <E> int updatePrimaryToTrue(Class<E> entityClass, String id, String primaryKeyCol, boolean active) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaUpdate<E> update = criteriaBuilder.createCriteriaUpdate(entityClass);
		Root<E> root = update.from(entityClass);
		Predicate idPredicate = setId(criteriaBuilder, root, id, primaryKeyCol);
		predicates.add(idPredicate);
		update.where(predicates.toArray(new Predicate[] {}));
		update.set(root.get(ISACTIVE_COLUMN_NAME), active);
		Query executableQuery = entityManager.createQuery(update);
		return executableQuery.executeUpdate();
	}

	public <E, T> T updateMasterData(Class<E> entity, T t)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String langCode = null, id = null;
		boolean activePrimary = false, activeSecondary = false;
		String primaryKeyCol = null, nameCol = null, nameValue = null;
		Field isActive;
		Class<?> dtoClass = t.getClass();
		for (Field entField : entity.getDeclaredFields()) {
			if (entField.isAnnotationPresent(Id.class)) {
				entField.setAccessible(true);
				if (entField.getName() != null && !entField.getName().equals(LANGCODE_COLUMN_NAME)) {
					primaryKeyCol = entField.getName();
				}
				if (entField.getName() != null && !entField.getName().equals(NAME_COLUMN_NAME)) {
					nameCol = entField.getName();
				}
			}
		}
		for (Field field : dtoClass.getDeclaredFields()) {
			field.setAccessible(true);
			if (field.getName() != null && field.getName().equals(LANGCODE_COLUMN_NAME)) {
				langCode = (String) field.get(t);
			}

			if (field.getName() != null && field.getName().equals(primaryKeyCol)) {
				id = (String) field.get(t);
			}

			//NOTE: is_active flag update should be handled only with patch API
			/*if (field.getName() != null && field.getName().equals(ISACTIVE_COLUMN_NAME)) {
				activeDto = (boolean) field.get(t);
			}*/
		}
			return t;
	}

	private <E> E getResultSet(Class<E> entity, String langCode, String id, String idColumn) {
		E result = null;
		try {

			List<Predicate> predicates = new ArrayList<Predicate>();
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<E> selectQuery = criteriaBuilder.createQuery(entity);
			Root<E> rootQuery = selectQuery.from(entity);
			Predicate predicate = setLangCode(criteriaBuilder, rootQuery, langCode);
			Predicate idPredicate = setId(criteriaBuilder, rootQuery, id, idColumn);
			predicates.add(predicate);
			predicates.add(idPredicate);
			selectQuery.where(predicates.toArray(new Predicate[] {}));
			TypedQuery<E> executableQuery = entityManager.createQuery(selectQuery);
			result = executableQuery.getSingleResult();
		} catch (HibernateException hibernateException) {
			throw new DataAccessLayerException(HibernateErrorCode.HIBERNATE_EXCEPTION.getErrorCode(),
					hibernateException.getMessage(), hibernateException);
		} catch (Exception e) {
			if (e instanceof NoResultException) {
				return null;
			} else {
				throw new MasterDataServiceException(HibernateErrorCode.HIBERNATE_EXCEPTION.getErrorCode(),
						e.toString());
			}
		}
		return result;
	}

	/**
	 * Method to add the Language Code in the criteria query
	 * 
	 * @param builder  used to construct the criteria query
	 * @param root     root type in the from clause,always refers entity
	 * @param langCode language code
	 * @return {@link Predicate}
	 */
	private <E> Predicate setLangCode(CriteriaBuilder builder, Root<E> root, String langCode) {
		if (langCode != null && !langCode.isEmpty()) {
			Path<Object> langCodePath = root.get(LANGCODE_COLUMN_NAME);
			if (langCodePath != null) {
				return builder.equal(langCodePath, langCode);
			}
		}
		return null;
	}

	private <E> Predicate setId(CriteriaBuilder builder, Root<E> root, String id, String idColumn) {
		if (id != null && !id.isEmpty()) {
			Path<Object> idPath = root.get(idColumn);
			if (idPath != null) {
				return builder.equal(idPath, id);
			}
		}
		return null;
	}

	public <E> int updateMasterDataDeactivate(Class<E> entity, String code) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaUpdate<E> update = criteriaBuilder.createCriteriaUpdate(entity);
		Root<E> root = update.from(entity);
		Predicate codePredicate = setCode(criteriaBuilder, root, code);
		predicates.add(codePredicate);
		update.where(predicates.toArray(new Predicate[] {}));
		update.set(root.get(ISACTIVE_COLUMN_NAME), false);
		Query executableQuery = entityManager.createQuery(update);
		return executableQuery.executeUpdate();
	}

	public <E> int updateMasterDataStatus(Class<E> entity, String primaryKeyValue, boolean isActive, String column) {
		Authentication authN = SecurityContextHolder.getContext().getAuthentication();
		if (!EmptyCheckUtils.isNullEmpty(authN)) {
			contextUser = authN.getName();
		}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaUpdate<E> update = criteriaBuilder.createCriteriaUpdate(entity);
		Root<E> root = update.from(entity);
		Predicate codePredicate = setColumn(criteriaBuilder, root, primaryKeyValue, column);
		predicates.add(codePredicate);
		update.where(predicates.toArray(new Predicate[] {}));
		update.set(root.get(ISACTIVE_COLUMN_NAME), isActive);
		update.set(root.get(UPD_BY_COLUMN_NAME), contextUser);
		update.set(root.get(UPDA_DTIMES_COLUMN_NAME), LocalDateTime.now());
		Query executableQuery = entityManager.createQuery(update);
		return executableQuery.executeUpdate();
	}

	private <E> Predicate setColumn(CriteriaBuilder builder, Root<E> root, String primaryKeyValue, String column) {
		if (primaryKeyValue != null && !primaryKeyValue.isEmpty()) {
			Path<Object> langCodePath = root.get(column);
			if (langCodePath != null) {
				return builder.equal(langCodePath, primaryKeyValue);
			}
		}
		return null;
	}

	private <E> Predicate setCode(CriteriaBuilder builder, Root<E> root, String code) {
		if (code != null && !code.isEmpty()) {
			Path<Object> langCodePath = root.get(CODE_COLUMN_NAME);
			if (langCodePath != null) {
				return builder.equal(langCodePath, code);
			}
		}
		return null;
	}

	private String generateId() {
		return UUID.randomUUID().toString();
	}
}
