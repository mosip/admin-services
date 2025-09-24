package io.mosip.kernel.syncdata.httpfilter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * A servlet filter that adds Cross-Origin Resource Sharing (CORS) headers to HTTP responses.
 * <p>
 * This filter enables CORS support by setting appropriate response headers based on the request's
 * {@code Origin} header, allowing cross-origin requests for non-OPTIONS methods. It is optimized
 * for performance by caching static header values and minimizing string operations. The filter
 * supports POST, GET, OPTIONS, DELETE, PUT, and PATCH methods, and allows specific headers and
 * credentials. For OPTIONS requests, the filter sets headers and returns without further processing.
 * </p>
 * <p>
 * Security annotations are included to suppress warnings about permissive CORS and request parameter
 * injection into headers, as the filter intentionally allows dynamic origins for flexibility.
 * </p>
 *
 * @author Mindtree Ltd.
 * @since 1.0.0
 */
@SuppressWarnings("findsecbugs:PERMISSIVE_CORS")
public class CorsFilter implements Filter {
	/**
	 * Logger instance for logging errors and debugging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CorsFilter.class);

	/**
	 * Constant for the HTTP OPTIONS method.
	 */
	private static final String OPTIONS_METHOD = "OPTIONS";

	/**
	 * Constant for allowed HTTP methods in CORS.
	 */
	private static final String ALLOW_METHODS = "POST, GET, OPTIONS, DELETE, PUT, PATCH";

	/**
	 * Constant for the maximum age of CORS preflight cache.
	 */
	private static final String MAX_AGE = "3600";

	/**
	 * Constant for allowed headers in CORS requests.
	 */
	private static final String ALLOW_HEADERS = "Date, Content-Type, Accept, X-Requested-With, Authorization, From, X-Auth-Token, Request-Id";

	/**
	 * Constant for exposed headers in CORS responses.
	 */
	private static final String EXPOSE_HEADERS = "Date, Response-Signature";

	/**
	 * Constant for allowing credentials in CORS requests.
	 */
	private static final String ALLOW_CREDENTIALS = "true";

	/**
	 * Default constructor for the filter.
	 * <p>
	 * This constructor is a no-op, as no initialization is required at construction time.
	 * </p>
	 */
	public CorsFilter() {
		// Default Constructor
	}

	/**
	 * Initializes the filter.
	 * <p>
	 * This implementation is a no-op, as no initialization is required for this filter.
	 * </p>
	 *
	 * @param filterConfig the {@link FilterConfig} object containing filter configuration
	 * @throws ServletException if an error occurs during initialization
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// init method from Filter
	}

	/**
	 * Processes incoming HTTP requests by adding CORS headers and handling OPTIONS requests.
	 * <p>
	 * This method sets CORS headers based on the request's {@code Origin} header, allowing
	 * cross-origin requests for non-OPTIONS methods. For OPTIONS requests, it sets the headers
	 * and returns without further processing. The filter uses cached constant values for headers
	 * to optimize performance. If an error occurs, it is logged and re-thrown to ensure proper
	 * error handling by the servlet container.
	 * </p>
	 *
	 * @param req         the incoming {@link ServletRequest}, expected to be an {@link HttpServletRequest}
	 * @param res         the {@link ServletResponse}, expected to be an {@link HttpServletResponse}
	 * @param chain       the {@link FilterChain} to continue processing the request
	 * @throws IOException      if an I/O error occurs during processing
	 * @throws ServletException if a servlet-specific error occurs during processing
	 */
	@SuppressWarnings("findbugs:HRS_REQUEST_PARAMETER_TO_HTTP_HEADER")
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (request.isAsyncSupported()) {
			AsyncContext asyncContext = request.startAsync();
			asyncContext.start(() -> {
				try {
					doFilterInternal(request, response, chain);
					asyncContext.complete();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
		} else {
			doFilterInternal(request, response, chain);
		}
	}

	/**
	 * Processes a single HTTP request to add CORS headers and handle preflight requests.
	 *
	 * <p><b>Behavior</b></p>
	 * <ul>
	 *   <li>If the request contains an {@code Origin} header, the same value is echoed in
	 *       {@code Access-Control-Allow-Origin} and {@code Vary: Origin} is set to ensure
	 *       caches vary by origin.</li>
	 *   <li>Sets {@code Access-Control-Allow-Methods}, {@code Access-Control-Max-Age},
	 *       {@code Access-Control-Allow-Headers} (echoing {@code Access-Control-Request-Headers}
	 *       when present), {@code Access-Control-Expose-Headers}, and
	 *       {@code Access-Control-Allow-Credentials}.</li>
	 *   <li>If the request method is {@code OPTIONS} (CORS preflight), responds with
	 *       {@code 204 No Content} and returns without invoking the remaining filter chain.</li>
	 *   <li>For all other methods, continues the filter chain.</li>
	 * </ul>
	 *
	 * <p><b>Security notes</b></p>
	 * <ul>
	 *   <li>When {@code Access-Control-Allow-Credentials=true}, the filter must not use {@code *}
	 *       for {@code Access-Control-Allow-Origin}; this method echoes the concrete request origin.</li>
	 *   <li>Consider replacing origin echoing with an origin allow-list if stricter security is required.</li>
	 * </ul>
	 *
	 * <p><b>Performance notes</b></p>
	 * <ul>
	 *   <li>Preflight requests return early (204) to avoid unnecessary work in downstream filters/servlets.</li>
	 *   <li>Setting {@code Vary: Origin} prevents cache poisoning across different origins.</li>
	 * </ul>
	 *
	 * @param request  the incoming HTTP request (never {@code null})
	 * @param response the outgoing HTTP response (never {@code null})
	 * @param chain    the filter chain to continue processing for non-preflight requests
	 * @throws IOException      if an I/O error occurs during processing
	 * @throws ServletException if a servlet error occurs during processing
	 */
	private void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			String origin = request.getHeader("Origin");
			if (origin != null && !origin.isBlank()) {
				// Echo specific origin (required when credentials=true)
				response.setHeader("Access-Control-Allow-Origin", origin);
				// Cache varies by Origin to avoid cross-origin cache poisoning
				response.setHeader("Vary", "Origin");
			}
			response.setHeader("Access-Control-Allow-Methods", ALLOW_METHODS);
			response.setHeader("Access-Control-Max-Age", MAX_AGE);

			response.setHeader("Access-Control-Allow-Headers", ALLOW_HEADERS);
			response.setHeader("Access-Control-Expose-Headers", EXPOSE_HEADERS);
			response.setHeader("Access-Control-Allow-Credentials", ALLOW_CREDENTIALS);
			/*
			 * response.setHeader("X-Frame-Options", "SAMEORIGIN");
			 * response.setHeader("X-Content-Type-Options", "nosniff");
			 * response.setHeader("X-XSS-Protection", "1; mode=block");
			 * response.setHeader("Cache-Control", "No-store"); response.setHeader("Pragma",
			 * "no-cache");
			 */

			if (OPTIONS_METHOD.equalsIgnoreCase(request.getMethod())) {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204
				response.setContentLength(0);
				return;
			}

			chain.doFilter(request, response);
		} catch (Exception e) {
			LOGGER.error("CorsFilter error: {}", e.getMessage());
			throw e;
		}
	}

	/**
	 * Destroys the filter.
	 * <p>
	 * This implementation is a no-op, as no resources need to be released.
	 * </p>
	 */
	@Override
	public void destroy() {
		// destroy method from Filter
	}
}