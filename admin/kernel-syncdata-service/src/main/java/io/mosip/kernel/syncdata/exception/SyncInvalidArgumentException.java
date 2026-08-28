package io.mosip.kernel.syncdata.exception;

import io.mosip.kernel.core.exception.BaseUncheckedException;
import io.mosip.kernel.core.exception.ServiceError;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SyncInvalidArgumentException extends BaseUncheckedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6611958753480242869L;
	/**
	 * This variable holds the MosipErrors list.
	 */
	private final List<ServiceError> list;

	/**
	 * @param list The error list.
	 */
	public SyncInvalidArgumentException(List<ServiceError> list) {
		super(constructMessage(list));
		this.list = list;
	}

	/**
	 * Getter for error list.
	 * 
	 * @return The error list.
	 */
	public List<ServiceError> getList() {
		return list;
	}

	private static String constructMessage(List<ServiceError> list) {
		return list.stream()
				.map(ServiceError::getMessage)
				.filter(Objects::nonNull)
				.collect(Collectors.joining(", "));
	}

}
