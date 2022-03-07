package io.mosip.kernel.masterdata.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import io.mosip.kernel.masterdata.constant.MasterDataConstant;
import io.mosip.kernel.masterdata.dto.response.FilterResult;
import io.mosip.kernel.masterdata.validator.FilterTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.mosip.kernel.masterdata.constant.ValidationErrorCode;
import io.mosip.kernel.masterdata.dto.FilterData;
import io.mosip.kernel.masterdata.dto.request.FilterDto;
import io.mosip.kernel.masterdata.dto.request.FilterValueDto;
import io.mosip.kernel.masterdata.dto.request.SearchFilter;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;

/**
 * Class that provides generic methods for implementation of filter values
 * search.
 *
 * @author Sagar Mahapatra
 * @author Ritesh Sinha
 * @author Urvil Joshi
 *
 * @since 1.0
 *
 */
@Repository
@Transactional(readOnly = true)
public class MasterDataFilterHelper {

	private static List<Class<?>> classes = null;

	@Autowired
	private MasterdataSearchHelper masterdataSearchHelper;

	@PostConstruct
	private static void init() {
		classes = new ArrayList<>();
		classes.add(LocalDateTime.class);
		classes.add(LocalDate.class);
		classes.add(LocalTime.class);
		classes.add(Short.class);
		classes.add(Integer.class);
		classes.add(Double.class);
		classes.add(Float.class);
	}

	private static final String LANGCODE_COLUMN_NAME = "langCode";
	private static final String ISDELETED_COLUMN_NAME = "isDeleted";
	private static final String FILTER_VALUE_UNIQUE = "unique";
	private static final String FILTER_VALUE_ALL = "all";
	private static final String WILD_CARD_CHARACTER = "%";
	private static final String FILTER_VALUE_EMPTY = "";

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${mosip.kernel.filtervalue.max_columns:20}")
	int filterValueMaxColumns;

	public MasterDataFilterHelper(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public <E, T> FilterResult<T> filterValues(Class<E> entity, FilterDto filterDto, FilterValueDto filterValueDto) {
		return filterValues(entity, filterDto, filterValueDto, null);
	}

	public <E, T> FilterResult<T> filterValues(Class<E> entity, FilterDto filterDto, FilterValueDto filterValueDto, List<String> zoneCodes) {
		String columnName = filterDto.getColumnName();
		String columnType = filterDto.getType();
		long totalCount = 0;

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<String> criteriaQueryByString = criteriaBuilder.createQuery(String.class);
		Root<E> root = criteriaQueryByString.from(entity);
		Path<Object> path = root.get(filterDto.getColumnName());

		CriteriaQuery<T> criteriaQueryByType = criteriaBuilder.createQuery((Class<T>) path.getJavaType());
		Root<E> rootType = criteriaQueryByType.from(entity);

		// check if column type is boolean then return true/false
		if (checkColNameTypeAndRootType(columnName, columnType, rootType)) {
			return valuesForStatusColumn();
		}

		columnTypeValidator(rootType, columnName);

		criteriaQueryByType.select(rootType.get(columnName));
		criteriaQueryByType.where(getFilterPredicate(criteriaBuilder, rootType, filterDto, filterValueDto, zoneCodes));
		criteriaQueryByType.orderBy(criteriaBuilder.asc(rootType.get(columnName)));
		// set true only when filter type is either unique / empty
		criteriaQueryByType.distinct((columnType.equals(FILTER_VALUE_UNIQUE) || columnType.equals(FILTER_VALUE_EMPTY)));

		if(filterValueDto.isTotalCountRequired()) {
			CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
			Root<E> countRootType = countQuery.from(entity);
			countQuery.select(columnType.equals(FILTER_VALUE_ALL) ?
					criteriaBuilder.count(countRootType.get(columnName)) :
					criteriaBuilder.countDistinct(countRootType.get(columnName)));
			countQuery.where(getFilterPredicate(criteriaBuilder, countRootType, filterDto, filterValueDto, zoneCodes));
			totalCount = entityManager.createQuery(countQuery).getSingleResult();
		}

		TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQueryByType);
		return new FilterResult(typedQuery.setMaxResults(filterValueDto.getPageFetch()>0 ?
				filterValueDto.getPageFetch() : filterValueMaxColumns).getResultList(), totalCount);
	}

	private <E> boolean checkColNameTypeAndRootType(String columnName, String columnType, Root<E> rootType) {
		return rootType.get(columnName).getJavaType().equals(Boolean.class) && (columnType.equals(FILTER_VALUE_UNIQUE)
				|| columnType.equals(FILTER_VALUE_ALL) || columnType.equals(FILTER_VALUE_EMPTY));
	}

	public <E> FilterResult filterValuesWithCode(Class<E> entity, FilterDto filterDto,
													 FilterValueDto filterValueDto, String fieldCodeColumnName) {
		return filterValuesWithCode(entity, filterDto, filterValueDto, fieldCodeColumnName, null);
	}

	public <E> FilterResult filterValuesWithCode(Class<E> entity, FilterDto filterDto,
													 FilterValueDto filterValueDto, String fieldCodeColumnName, List<String> zoneCodes) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		String columnName = filterDto.getColumnName();
		String columnType = filterDto.getType();
		long totalCount =0;

		CriteriaQuery<FilterData> criteriaQueryByType = criteriaBuilder.createQuery(FilterData.class);
		Root<E> rootType = criteriaQueryByType.from(entity);

		// if column type is Boolean then add value as true or false
		if (rootType.get(columnName).getJavaType().equals(Boolean.class) && (columnType.equals(FILTER_VALUE_UNIQUE)
				|| columnType.equals(FILTER_VALUE_ALL) || columnType.equals(FILTER_VALUE_EMPTY))) {
			return valuesForStatusColumnCode();
		}

		columnTypeValidator(rootType, columnName);

		criteriaQueryByType.multiselect(rootType.get(fieldCodeColumnName), rootType.get(columnName));
		criteriaQueryByType.where(getFilterPredicate(criteriaBuilder, rootType, filterDto, filterValueDto, zoneCodes));
		criteriaQueryByType.orderBy(criteriaBuilder.asc(rootType.get(columnName)));
		// set true only when filter type is either unique / empty
		criteriaQueryByType.distinct((columnType.equals(FILTER_VALUE_UNIQUE) || columnType.equals(FILTER_VALUE_EMPTY)));

		if(filterValueDto.isTotalCountRequired()) {
			CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
			Root<E> countRootType = countQuery.from(entity);
			countQuery.select(columnType.equals(FILTER_VALUE_ALL) ?
					criteriaBuilder.count(countRootType.get(fieldCodeColumnName)) :
					criteriaBuilder.countDistinct(countRootType.get(fieldCodeColumnName)));
			countQuery.where(getFilterPredicate(criteriaBuilder, countRootType, filterDto, filterValueDto, zoneCodes));
			totalCount = entityManager.createQuery(countQuery).getSingleResult();
		}

		TypedQuery<FilterData> typedQuery = entityManager.createQuery(criteriaQueryByType);
		return new FilterResult(typedQuery.setMaxResults(filterValueDto.getPageFetch()>0 ?
				filterValueDto.getPageFetch() : filterValueMaxColumns).getResultList(), totalCount);
	}

	private <E> Predicate getFilterPredicate(CriteriaBuilder criteriaBuilder,  Root<E> rootType,
											 FilterDto filterDto, FilterValueDto filterValueDto, List<String> zoneCodes) {
		List<Predicate> predicates = new ArrayList<>();

		Predicate caseSensitivePredicate = criteriaBuilder.and(criteriaBuilder
				.like(criteriaBuilder.lower(rootType.get(filterDto.getColumnName())), criteriaBuilder.lower(
						criteriaBuilder.literal(WILD_CARD_CHARACTER + filterDto.getText() + WILD_CARD_CHARACTER))));

		if (!(rootType.get(filterDto.getColumnName())).getJavaType().equals(Boolean.class)) {
			predicates.add(caseSensitivePredicate);

			if(filterValueDto.getLanguageCode() != null && !filterValueDto.getLanguageCode().equals("all")) {
				predicates.add(criteriaBuilder.equal(rootType.get(LANGCODE_COLUMN_NAME),
						filterValueDto.getLanguageCode()));
			}
		}

		//deleted entries should be filtered out
		//Predicate isDeletedPredicate = criteriaBuilder.or(criteriaBuilder.equal(rootType.get(ISDELETED_COLUMN_NAME),false),
		//		criteriaBuilder.isNull(rootType.get(ISDELETED_COLUMN_NAME)));
		//predicates.add(isDeletedPredicate);

		buildOptionalFilter(criteriaBuilder, rootType, filterValueDto.getOptionalFilters(), predicates, zoneCodes);
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	private <E> void columnTypeValidator(Root<E> root, String columnName) {
		if (classes.contains(root.get(columnName).getJavaType())) {
			throw new MasterDataServiceException(ValidationErrorCode.FILTER_COLUMN_NOT_SUPPORTED.getErrorCode(),
					ValidationErrorCode.FILTER_COLUMN_NOT_SUPPORTED.getErrorMessage());

		}
	}

	private FilterResult valuesForStatusColumnCode() {
		FilterData trueFilterData = new FilterData("", "true");
		FilterData falseFilterData = new FilterData("", "false");
		List<FilterData> filterDataList = new ArrayList<>();
		filterDataList.add(trueFilterData);
		filterDataList.add(falseFilterData);
		return new FilterResult(filterDataList, 2);
	}

	private FilterResult valuesForStatusColumn() {
		List<String> filterDataList = new ArrayList<>();
		filterDataList.add("true");
		filterDataList.add("false");
		return new FilterResult(filterDataList, 2);
	}


	//Should be "AND" operation b/w the provided optional filters
	private <E> void buildOptionalFilter(CriteriaBuilder builder, Root<E> root,
										 final List<SearchFilter> optionalFilters, List<Predicate> predicates, List<String> zoneCodes) {
		Predicate zonePredicate = null;
		if(zoneCodes != null && !zoneCodes.isEmpty()) {
			List<Predicate> zonePredicates = zoneCodes.stream().map(i -> masterdataSearchHelper.buildFilters(builder, root,
							new SearchFilter(i, null, null, MasterDataConstant.ZONE_CODE, FilterTypeEnum.EQUALS.name())))
					.filter(Objects::nonNull).collect(Collectors.toList());
			zonePredicate = builder.or(zonePredicates.toArray(new Predicate[zonePredicates.size()]));
		}

		Predicate optionalPredicate = null;
		if (optionalFilters != null && !optionalFilters.isEmpty()) {
			List<Predicate> optionalPredicates = optionalFilters.stream().map(i -> masterdataSearchHelper.buildFilters(builder, root, i))
					.filter(Objects::nonNull).collect(Collectors.toList());
			optionalPredicate = builder.and(optionalPredicates.toArray(new Predicate[optionalPredicates.size()]));
		}

		if(zonePredicate == null && optionalPredicate == null)
			return;

		if(zonePredicate != null && optionalPredicate != null) {
			predicates.add(builder.and(zonePredicate, optionalPredicate));
			return;
		}

		predicates.add((zonePredicate != null) ? zonePredicate : optionalPredicate);
	}


}
