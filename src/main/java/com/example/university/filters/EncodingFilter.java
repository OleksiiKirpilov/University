package com.example.university.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * Encoding filter. Checks for requested client encoding.
 * If none requested - sets to UTF-8.
 */
public class EncodingFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger(EncodingFilter.class);

    private String encoding;

    @Override
    public void destroy() {
        LOG.debug("Filter destruction");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        LOG.debug("Filter starts");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        LOG.trace("Requested URI: {}", httpRequest.getRequestURI());
        String requestEncoding = request.getCharacterEncoding();
        if (requestEncoding == null) {
            LOG.trace("Requested encoding = null, encoding set to {}", encoding);
            request.setCharacterEncoding(encoding);
        }
        LOG.debug("Filter finished");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        LOG.debug("Filter initialization starts");
        encoding = fConfig.getInitParameter("encoding");
        LOG.trace("Encoding from web.xml = {}", encoding);
        LOG.debug("Filter initialization finished");
    }

}