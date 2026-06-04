package io.mosip.kernel.syncdata.httpfilter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
/**
 * A servlet filter that wraps HTTP requests and responses to enable content caching for non-streaming endpoints.
 * <p>
 * This filter wraps {@link HttpServletRequest} and {@link HttpServletResponse} with
 * {@link ContentCachingRequestWrapper} and {@link ContentCachingResponseWrapper} respectively,
 * to allow caching of request and response content for non-streaming endpoints (i.e., URIs not ending with ".stream").
 * Streaming endpoints are processed without wrapping to minimize overhead. The filter is optimized for performance
 * by creating wrappers only when necessary and using efficient string checks.
 * </p>
 *
 * @author Abhishek Kumar
 * @since 1.0.0
 */
public class ReqResFilter implements Filter {
	/**
	 * Logger instance for logging errors and debugging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReqResFilter.class);
	/**
	 * Constant for the streaming endpoint suffix.
	 */
	private static final String STREAM_SUFFIX = ".stream";
	/**
	 * Initializes the filter. This implementation is a no-op as no initialization is required.
	 *
	 * @param filterConfig the {@link FilterConfig} object containing filter configuration
	 * @throws ServletException if an error occurs during initialization
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// init method overriding
	}
	/**
	 * Processes incoming HTTP requests by applying content caching for non-streaming endpoints.
	 * <p>
	 * If the request URI ends with ".stream", the request is passed through the filter chain without
	 * wrapping to optimize performance for streaming endpoints. For other requests, the
	 * {@link HttpServletRequest} and {@link HttpServletResponse} are wrapped with
	 * {@link ContentCachingRequestWrapper} and {@link ContentCachingResponseWrapper} respectively,
	 * to enable content caching. The response body is copied back to the original response after
	 * processing. Errors during processing are logged and re-thrown to ensure proper error handling.
	 * </p>
	 *
	 * @param request     the incoming {@link ServletRequest}, expected to be an {@link HttpServletRequest}
	 * @param response    the {@link ServletResponse}, expected to be an {@link HttpServletResponse}
	 * @param chain       the {@link FilterChain} to continue processing the request
	 * @throws IOException      if an I/O error occurs during processing
	 * @throws ServletException if a servlet-specific error occurs during processing
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {
			if (httpRequest.getRequestURI().endsWith(STREAM_SUFFIX)) {
				chain.doFilter(request, response);
				return;
			}
			ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);
			ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);
			chain.doFilter(requestWrapper, responseWrapper);
			responseWrapper.copyBodyToResponse();
		} catch (Exception e) {
			LOGGER.error("ReqResFilter error: {}", e.getMessage());
			throw e;
		}
	}
	/**
	 * Destroys the filter. This implementation is a no-op as no resources need to be released.
	 */
	@Override
	public void destroy() {
		// destroy method overriding
	}
}