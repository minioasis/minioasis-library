package org.minioasis.library.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

@Configuration
public class MinioConfig {

	@Value("${minio.accessKey}")
	private String accessKey;

	@Value("${minio.secretKey}")
	private String secretKey;

	@Value("${minio.endpoint}")
	private String endPoint;

	@Value("${minio.connectTimeout:10000}")
	private Integer connectTimeout;

	@Value("${minio.writeTimeout:10000}")
	private Integer writeTimeout;

	@Value("${minio.readTimeout:10000}")
	private Integer readTimeout;

	private static final Log logger = LogFactory.getLog(MinioConfig.class);

	@Bean
	public MinioClient minioClient() {
		MinioClient minioClient = null;
		try {
			logger.info("Connecting to " + endPoint);
			minioClient = new MinioClient(endPoint, accessKey, secretKey);
			minioClient.setTimeout(connectTimeout.longValue(), writeTimeout.longValue(), readTimeout.longValue());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return minioClient;
	}
}
