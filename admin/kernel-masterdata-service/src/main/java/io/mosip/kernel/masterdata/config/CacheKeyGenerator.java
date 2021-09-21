package io.mosip.kernel.masterdata.config;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

/**
 * @author GOVINDARAJ VELU
 *
 */
@Component
public class CacheKeyGenerator implements KeyGenerator {

	@SuppressWarnings("unchecked")
	@Override
	public Object generate(Object target, Method method, Object... params) {
		StringBuilder key = new StringBuilder();
		String delimiter = "-";
		for(int i=0; i<params.length; i++) {
			String k = null;
			if (params[i] instanceof List) {
				List<String> list = (List<String>) params[i];
				k = String
						.join(delimiter, // convert the list to string with delimiter
						list.stream() // convert to stream
						.sorted() //to sort the list
						.collect(Collectors.toSet())); //to removing duplicate from the list
			} else {
				k = params[i].toString();
			}
			key.append(k);
			if ((i+1) < params.length) {
				key.append(delimiter);
			}
		}
		return key.toString();
	}

}
