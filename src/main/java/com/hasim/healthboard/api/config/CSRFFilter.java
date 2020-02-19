
package com.hasim.healthboard.api.config;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hasim.healthboard.api.exception.ErrorCodes;
import com.hasim.healthboard.api.util.DashboardCommonUtil;


public class CSRFFilter extends OncePerRequestFilter {
    private static final Logger logger = LogManager.getLogger(CSRFFilter.class);

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        boolean isCSFRException = false;
        Enumeration<String> headers = request.getHeaderNames();
        if (null != headers) {
            while (headers.hasMoreElements()) {
                String headerName = headers.nextElement();
                try {
                    isCSFRException = DashboardCommonUtil
                        .validateRequestParameter(request.getHeader(headerName));
                } catch (Exception e) {
                    logger.error(e);
                    isCSFRException = true;
                }
                if (isCSFRException) {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    response.getWriter().write(DashboardCommonUtil
                        .convertObjectToJson(ErrorCodes.MALFORMED_REQUEST.getErrorResponse()));
                    break;
                }
            }
        }
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        
       // response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        
        response.addHeader("Access-Control-Allow-Methods", "PUT, GET, POST, PUT, OPTIONS, DELETE, PATCH");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        
        if (!isCSFRException) {
            filterChain.doFilter(request, response);
        }

    }

}