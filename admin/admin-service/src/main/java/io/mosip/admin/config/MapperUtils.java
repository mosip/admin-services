package io.mosip.admin.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

import jakarta.persistence.EmbeddedId;

import io.mosip.admin.bulkdataupload.entity.BaseEntity;
import io.mosip.kernel.core.dataaccess.exception.DataAccessLayerException;
import io.mosip.kernel.core.util.EmptyCheckUtils;



public class MapperUtils {
	private MapperUtils() {
		super();
	}

	
	private static final String SOURCE_NULL_MESSAGE = "source should not be null";
	private static final String DESTINATION_NULL_MESSAGE = "destination should not be null";

	/**
	 * This flag is used to restrict copy null values.
	 */
	private static Boolean mapNullValues = Boolean.TRUE;
	public static <S, D> D map(final S source, D destination) {
		Objects.requireNonNull(source, SOURCE_NULL_MESSAGE);
		Objects.requireNonNull(destination, DESTINATION_NULL_MESSAGE);
		try {
			mapValues(source, destination);
		} catch (IllegalAccessException | InstantiationException e) {
			throw new DataAccessLayerException("KER-MSD-991", "Exception in mapping vlaues from source : "
					+ source.getClass().getName() + " to destination : " + destination.getClass().getName(), e);
		}
		return destination;
	}
	
	private static <S, D> void mapValues(S source, D destination)
			throws IllegalAccessException, InstantiationException {

		mapFieldValues(source, destination);// this method simply map values if field name and type are same
		mapToEntity(source, destination);
		
	}
	private static <S, D> void mapToEntity(S source, D destination)
			throws InstantiationException, IllegalAccessException {
		Field[] fields = destination.getClass().getDeclaredFields();
		setBaseFieldValue(source, destination);// map super class values
		for (Field field : fields) {
			/**
			 * Map DTO matching field values to super class field values
			 */
			if (field.isAnnotationPresent(EmbeddedId.class)) {
				Object id = field.getType().newInstance();
				mapFieldValues(source, id);
				field.setAccessible(true);
				field.set(destination, id);
				field.setAccessible(false);
				break;
			}
		}
	}
	public static <S, D> void setBaseFieldValue(S source, D destination) {
		Objects.requireNonNull(source, SOURCE_NULL_MESSAGE);
		Objects.requireNonNull(destination, DESTINATION_NULL_MESSAGE);
		String sourceSupername = source.getClass().getSuperclass().getName();// super class of source object
		String destinationSupername = destination.getClass().getSuperclass().getName();// super class of destination
		// object
		String baseEntityClassName = BaseEntity.class.getName();// base entity fully qualified name
		String objectClassName = Object.class.getName();// object class fully qualified name
		// if source is an entity
		if (sourceSupername.equals(baseEntityClassName) && destinationSupername.equals(baseEntityClassName)) {
			Field[] sourceFields = source.getClass().getSuperclass().getDeclaredFields();
			Field[] destinationFields = destination.getClass().getSuperclass().getDeclaredFields();
			mapFieldValues(source, destination, sourceFields, destinationFields);
		} 
		else if (sourceSupername.equals(baseEntityClassName) && !destinationSupername.equals(baseEntityClassName)) {
			Field[] sourceFields = source.getClass().getSuperclass().getDeclaredFields();
			Field[] destinationFields = destination.getClass().getDeclaredFields();
			mapFieldValues(source, destination, sourceFields, destinationFields);
		} else if (destinationSupername.equals(baseEntityClassName) && !sourceSupername.equals(baseEntityClassName)) {
			// if destination is an entity
			Field[] sourceFields = source.getClass().getDeclaredFields();
			Field[] destinationFields = destination.getClass().getSuperclass().getDeclaredFields();
			mapFieldValues(source, destination, sourceFields, destinationFields);
		} else {
			if (!sourceSupername.equals(objectClassName) && !destinationSupername.equals(objectClassName)) {
				Field[] sourceFields = source.getClass().getSuperclass().getDeclaredFields();
				Field[] destinationFields = destination.getClass().getSuperclass().getDeclaredFields();
				mapFieldValues(source, destination, sourceFields, destinationFields);
			}
		}

	}
	public static <S, D> void mapFieldValues(S source, D destination) {

		Objects.requireNonNull(source, SOURCE_NULL_MESSAGE);
		Objects.requireNonNull(destination, DESTINATION_NULL_MESSAGE);
		Field[] sourceFields = source.getClass().getDeclaredFields();
		Field[] destinationFields = destination.getClass().getDeclaredFields();

		mapFieldValues(source, destination, sourceFields, destinationFields);

	}
	private static <D, S> void mapFieldValues(S source, D destination, Field[] sourceFields,
			Field[] destinationFields) {
		try {
			for (Field sfield : sourceFields) {
				// Do not set values either static or final
				if (Modifier.isStatic(sfield.getModifiers()) || Modifier.isFinal(sfield.getModifiers())) {
					continue;
				}

				// make field accessible possibly private
				sfield.setAccessible(true);

				for (Field dfield : destinationFields) {

					Class<?> sourceType = sfield.getType();
					Class<?> destinationType = dfield.getType();

					// map only those field whose name and type is same
					if (sfield.getName().equals(dfield.getName()) && sourceType.equals(destinationType)) {

						// for normal field values
						dfield.setAccessible(true);
						setFieldValue(source, destination, sfield, dfield);
						break;
					}
				}
			}
		} catch (IllegalAccessException e) {

			throw new DataAccessLayerException("ADM-BLK-993", "Exception raised while mapping values form "
					+ source.getClass().getName() + " to " + destination.getClass().getName(), e);
		}
	}
	private static <S, D> void setFieldValue(S source, D destination, Field sf, Field dtf)
			throws IllegalAccessException {
		// check whether user wants to map null values into destination object or not
		if (!mapNullValues && EmptyCheckUtils.isNullEmpty(sf.get(source))) {
			return;
		}
		dtf.set(destination, sf.get(source));
		dtf.setAccessible(false);
		sf.setAccessible(false);
	}
}
