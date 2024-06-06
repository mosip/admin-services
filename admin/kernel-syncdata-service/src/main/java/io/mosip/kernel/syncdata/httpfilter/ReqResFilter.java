package io.mosip.kernel.syncdata.httpfilter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

public class ReqResFilter implements Filter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// init method overriding
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		ContentCachingRequestWrapper requestWrapper = null;
		ContentCachingResponseWrapper responseWrapper = null;

		if (httpServletRequest.getRequestURI().endsWith(".stream")) {
			chain.doFilter(request, response);
			return;
		}
		requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
		responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);
		chain.doFilter(requestWrapper, responseWrapper);
		responseWrapper.copyBodyToResponse();

	}

	@Override
	public void destroy() {
		// destroy method overriding
	}

}
