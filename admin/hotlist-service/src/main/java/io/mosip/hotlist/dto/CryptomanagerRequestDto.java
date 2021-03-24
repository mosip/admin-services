/*
 * 
 * 
 * 
 * 
 */
package io.mosip.hotlist.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Crypto-Manager-Request model
 * 
 * @author Manoj SP
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptomanagerRequestDto {

	private String applicationId;

	private String referenceId;

	private LocalDateTime timeStamp;

	private String data;

	private String salt;

	private String aad;

	private Boolean prependThumbprint;
}