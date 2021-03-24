package io.mosip.hotlist.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Manoj SP
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotlistPK implements Serializable {

	private static final long serialVersionUID = 6682712291026310423L;
	
	public String idHash;

	public String idType;

}
