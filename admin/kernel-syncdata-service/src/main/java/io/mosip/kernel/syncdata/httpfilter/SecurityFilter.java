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
 * A servlet filter that sets up authentication for incoming HTTP requests in the MOSIP syncdata module.
 * <p>
 * This filter applies a predefined authentication context to every request by setting a static
 * {@link AuthenticationToken} with a default {@link AuthUser} in the Spring Security context.
 * The authentication details, including user information and authorities, are pre-configured and
 * cached as static final fields to optimize performance by avoiding repeated object creation.
 * </p>
 * <p>
 * The filter is designed to be lightweight and efficient, making it suitable for high-throughput
 * environments. It logs errors for debugging purposes and ensures proper error propagation by
 * re-throwing exceptions.
 * </p>
 *
 * @author Abhishek Kumar
 * @since 1.0.0
 */
public class SecurityFilter extends GenericFilterBean {
	/**
	 * Logger instance for logging errors and debugging information.
	 */
	private static final Logger SFLOGGER = LoggerFactory.getLogger(SecurityFilter.class);
	/**
	 * Predefined list of authorities for the default user, cached for performance.
	 */
	private static final List<GrantedAuthority> AUTHORITIES = AuthorityUtils.commaSeparatedStringToAuthorityList("1001,1002");
	/**
	 * Predefined authentication user, cached to avoid repeated instantiation.
	 */
	private static final AuthUser AUTH_USER = new AuthUser("defaultadmin@mosip.io", "Mosip Admin", "[PROTECTED]", AUTHORITIES);
	/**
	 * Predefined authentication token, cached for reuse across requests.
	 */
	private static final Authentication AUTH_TOKEN = new AuthenticationToken(AUTH_USER, null, AUTHORITIES);
	/**
	 * Processes incoming HTTP requests by setting a predefined authentication context
	 * and passing the request through the filter chain.
	 * <p>
	 * This method sets a static {@link AuthenticationToken} in the Spring Security
	 * {@link SecurityContextHolder} to authenticate the request. The authentication token
	 * is pre-configured with a default {@link AuthUser} and authorities, ensuring minimal
	 * overhead by reusing cached objects. If an error occurs during processing, it is logged
	 * and re-thrown to ensure proper error handling by the servlet container.
	 * </p>
	 *
	 * @param req        the incoming {@link ServletRequest}, typically an {@link HttpServletRequest}
	 * @param res        the {@link ServletResponse}, typically an {@link HttpServletResponse}
	 * @param filterChain the {@link FilterChain} to continue processing the request
	 * @throws ServletException if a servlet-specific error occurs during processing
	 * @throws IOException      if an I/O error occurs while processing the request
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			SecurityContextHolder.getContext().setAuthentication(AUTH_TOKEN);
			filterChain.doFilter(req, res);
		} catch (Exception e) {
			SFLOGGER.error("SecurityFilter error: {}", e.getMessage());
			throw e;
		}
	}
}