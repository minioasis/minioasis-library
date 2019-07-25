package org.minioasis.library.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RemoteAccessServiceImpl implements RemoteAccessService {

	private RestTemplate restTemplate;
    private String host;
    private String port;
    private String contextPath;

    @Autowired
    public RemoteAccessServiceImpl(RestTemplateBuilder restTemplateBuilder, 
    		@Value("${server.host.name:http://localhost}") String host, 
    		@Value("${server.port:8080}") String port, 
    		@Value("${server.servlet.context-path:}") String contextPath) {
    	
    	this.restTemplate = restTemplateBuilder.build();
        this.host = host;
        this.port = port;
        this.contextPath = contextPath;
    }

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public URL getUrl() {

		StringBuffer sb = new StringBuffer(host);
		
		if(port != null && !port.equals("") ) {
			sb.append(":").append(port);
		}
		if(contextPath != null && !contextPath.equals("")) {
			sb.append(contextPath);
		}
			
		URL url = null;
		try {
			url = new URL(sb.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return url;
	}
	
}
