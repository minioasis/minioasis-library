package org.minioasis.library.service;

import java.net.URL;

import org.springframework.web.client.RestTemplate;

public interface RemoteAccessService {

	RestTemplate getRestTemplate();
	URL getUrl();

}
