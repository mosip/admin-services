package io.mosip.kernel.syncdata.httpfilter;

import io.mosip.kernel.syncdata.entity.AuthUser;
import io.mosip.kernel.syncdata.entity.AuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

/**
 * @author Abhishek Kumar
 * @since 1.0.0
 */
public class SecurityFilter extends GenericFilterBean {

	private static final Logger SFLOGGER = LoggerFactory.getLogger(SecurityFilter.class);

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		try {
			List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("1001,1002");
			AuthUser authUser = new AuthUser("defaultadmin@mosip.io", "Mosip Admin", "[PROTECTED]", authorities);
			Authentication authN = new AuthenticationToken(authUser, null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authN);
		} catch (Exception e) {
			SFLOGGER.error(e.getMessage());
		}
		filterChain.doFilter(request, response);
	}
}
