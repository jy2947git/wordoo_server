package com.focaplo.wordee.server.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.focaplo.wordee.PuzzleRemoteService;

public class LogFileFilter implements Filter {

    private static final Logger log = Logger.getLogger(LogFileFilter.class.getName());
    private FilterConfig filterConfig = null;

    

	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
		this.filterConfig=null;
		
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		Map myParameterMap = new HashMap();
		if(request.getMethod().equalsIgnoreCase("post")){
			Map parameterMap = request.getParameterMap();
			if(!parameterMap.isEmpty() && !parameterMap.containsKey("command")){
				//
				String queryString = (String)parameterMap.keySet().iterator().next();
				
				StringTokenizer st = new StringTokenizer(queryString, "&");
				while(st.hasMoreTokens()){
					String s = st.nextToken();
					String p = s.substring(0,s.indexOf("="));
					String v = s.substring(s.indexOf("=")+1);
					myParameterMap.put(p, v);
				}
			}
		}
		
		// check token
		String token = req.getParameter("token")!=null?req.getParameter("token"):(String)myParameterMap.get("token");
		
		if(token==null || !token.equalsIgnoreCase(PuzzleRemoteService.requestToken)){
			//bad
			log.warning("bad request comes from " + req.getRemoteAddr());
			return;
		}
		
		chain.doFilter(req, res);

	}

	public void init(FilterConfig arg0) throws ServletException {
		this.filterConfig = arg0;
		
	}
}