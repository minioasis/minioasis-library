package org.minioasis.library.repository;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.minioasis.library.domain.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.xmlpull.v1.XmlPullParserException;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.MinioException;

@Repository
public class PhotoRepositoryImpl implements PhotoRepository {

	@Autowired
	public MinioClient minioClient;
	
	@Value("${minio.patron.bucket.name}")
	private String patronBucket;
	
	@Value("${minio.patron.thumbnail.bucket.name}")
	private String patronThumbnailBucket;

	public Photo findPatronByIc(String id)
			throws IOException, NoSuchAlgorithmException, InvalidKeyException, XmlPullParserException {
		
		Photo photo = null;
		
		try {

			boolean found = minioClient.bucketExists(patronBucket);

			if (found) {

				ObjectStat objectStat = minioClient.statObject(patronBucket, id + ".jpg");

				if (objectStat == null) {
					objectStat = minioClient.statObject(patronBucket, id + ".jpeg");
				}

				if (objectStat != null) {
					
					String url = minioClient.presignedGetObject(patronBucket, objectStat.name(), 60 * 60 * 24);
					
					photo = convert(objectStat, url);

				} else {
					return photo;
				}

			}

		} catch (MinioException e) {
			return null;
		}
		
		return photo;

	}
	
	public Photo findPatronThumbnailByIc(String id)
			throws IOException, NoSuchAlgorithmException, InvalidKeyException, XmlPullParserException {

		Photo photo = null;
		
		try {

			boolean found = minioClient.bucketExists(patronThumbnailBucket);

			if (found) {

				ObjectStat objectStat = minioClient.statObject(patronThumbnailBucket, id + ".jpg");

				if (objectStat == null) {
					objectStat = minioClient.statObject(patronThumbnailBucket, id + ".jpeg");
				}

				if (objectStat != null) {
					
					String url = minioClient.presignedGetObject(patronThumbnailBucket, objectStat.name(), 60 * 60 * 24);
					
					photo = convert(objectStat, url);

				} else {
					return photo;
				}

			}

		} catch (MinioException e) {
			return null;
		}
		
		return photo;

	}
	
	private Photo convert(ObjectStat object, String url) {
		Photo photo = new Photo();
		
		String name = object.name();
		long size = object.length();
		
		photo.setName(name);
		photo.setSize(size);
		photo.setUrl(url);
		
		return photo;
	}

}
