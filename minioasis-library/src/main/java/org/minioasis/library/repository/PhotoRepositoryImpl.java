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
	
	@Value("${minio.biblio.bucket.name}")
	private String biblioBucket;
	
	@Value("${minio.biblio.thumbnail.bucket.name}")
	private String biblioThumbnailBucket;
	
	@Value("${minio.journal.bucket.name}")
	private String journalBucket;
	
	@Value("${minio.journal.thumbnail.bucket.name}")
	private String journalThumbnailBucket;	

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
	
	public Photo findBiblioByIsbn(String id) throws IOException, NoSuchAlgorithmException, InvalidKeyException, XmlPullParserException {
		
		Photo photo = null;
		
		try {

			boolean found = minioClient.bucketExists(biblioBucket);

			if (found) {

				ObjectStat objectStat = minioClient.statObject(biblioBucket, id + ".jpg");

				if (objectStat == null) {
					objectStat = minioClient.statObject(biblioBucket, id + ".jpeg");
				}

				if (objectStat != null) {
					
					String url = minioClient.presignedGetObject(biblioBucket, objectStat.name(), 60 * 60 * 24);
					
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
	
	public Photo findBiblioThumbnailByIsbn(String id) throws IOException, NoSuchAlgorithmException, InvalidKeyException, XmlPullParserException {
		
		Photo photo = null;
		
		try {

			boolean found = minioClient.bucketExists(biblioThumbnailBucket);

			if (found) {

				ObjectStat objectStat = minioClient.statObject(biblioThumbnailBucket, id + ".jpg");

				if (objectStat == null) {
					objectStat = minioClient.statObject(biblioThumbnailBucket, id + ".jpeg");
				}

				if (objectStat != null) {
					
					String url = minioClient.presignedGetObject(biblioThumbnailBucket, objectStat.name(), 60 * 60 * 24);
					
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
	
	public Photo findJournalByIssnCoden(String id)
			throws IOException, NoSuchAlgorithmException, InvalidKeyException, XmlPullParserException {
		
		Photo photo = null;
		
		try {

			boolean found = minioClient.bucketExists(journalBucket);

			if (found) {

				ObjectStat objectStat = minioClient.statObject(journalBucket, id + ".jpg");

				if (objectStat == null) {
					objectStat = minioClient.statObject(journalBucket, id + ".jpeg");
				}

				if (objectStat != null) {
					
					String url = minioClient.presignedGetObject(journalBucket, objectStat.name(), 60 * 60 * 24);
					
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
	
	public Photo findJournalThumbnailByIssnCoden(String id)
			throws IOException, NoSuchAlgorithmException, InvalidKeyException, XmlPullParserException {

		Photo photo = null;
		
		try {

			boolean found = minioClient.bucketExists(journalThumbnailBucket);

			if (found) {

				ObjectStat objectStat = minioClient.statObject(journalThumbnailBucket, id + ".jpg");

				if (objectStat == null) {
					objectStat = minioClient.statObject(journalThumbnailBucket, id + ".jpeg");
				}

				if (objectStat != null) {
					
					String url = minioClient.presignedGetObject(journalThumbnailBucket, objectStat.name(), 60 * 60 * 24);
					
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
