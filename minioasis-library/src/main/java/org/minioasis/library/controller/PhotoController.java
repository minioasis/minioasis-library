package org.minioasis.library.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.minioasis.library.domain.Photo;
import org.minioasis.library.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PhotoController {
	
	private static final Logger logger = LoggerFactory.getLogger(PhotoController.class);
	
	@Autowired
	private PhotoService photoService;

	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

	@GetMapping(path = "/member/photo/patron/{id}")
	public void memberPhoto(@PathVariable("id") String id, HttpServletResponse response) throws MalformedURLException {
		
		Photo photo = null;
		
		try {
			photo = this.photoService.findPatronThumbnailByIc(id);
		} catch(ConnectException cex) {
			logger.info("MINIO LOG : Member Thumbnail Not Found : " + id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (photo != null) {
			URL imgUrl = new URL(photo.getUrl());

			try {
				response.reset();
				// TODO : please include png , gif ...etc
				response.setContentType("image/jpeg");
				response.setBufferSize(DEFAULT_BUFFER_SIZE);
				response.setHeader("Content-Length", String.valueOf(photo.getSize()));
				response.setHeader("Content-Disposition", "inline;filename=\"" + photo.getName() + "\"");
				OutputStream out = response.getOutputStream();

				byte[] chunk = new byte[4096];
				int bytesRead;
				InputStream in = imgUrl.openStream();

				while ((bytesRead = in.read(chunk)) > 0) {
					out.write(chunk, 0, bytesRead);
				}

				out.flush();
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	@GetMapping(path = "/admin/photo/patron/{id}")
	public void patronPhoto(@PathVariable("id") String id, HttpServletResponse response) throws MalformedURLException {
		
		Photo photo = null;
		
		try {
			photo = this.photoService.findPatronByIc(id);
		} catch(ConnectException cex) {
			logger.info("MINIO LOG : Patron Photo Not Found : " + id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (photo != null) {
			URL imgUrl = new URL(photo.getUrl());

			try {
				response.reset();
				// TODO : please include png , gif ...etc
				response.setContentType("image/jpeg");
				response.setBufferSize(DEFAULT_BUFFER_SIZE);
				response.setHeader("Content-Length", String.valueOf(photo.getSize()));
				response.setHeader("Content-Disposition", "inline;filename=\"" + photo.getName() + "\"");
				OutputStream out = response.getOutputStream();

				byte[] chunk = new byte[4096];
				int bytesRead;
				InputStream in = imgUrl.openStream();

				while ((bytesRead = in.read(chunk)) > 0) {
					out.write(chunk, 0, bytesRead);
				}

				out.flush();
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	@GetMapping(path = "/admin/thumbnail.photo/patron/{id}")
	public void patronThumbnailPhoto(@PathVariable("id") String id, HttpServletResponse response) throws MalformedURLException {
		
		Photo photo = null;
		
		try {
			photo = this.photoService.findPatronThumbnailByIc(id);
		} catch(ConnectException cex) {
			logger.info("MINIO LOG : Patron Thumbnail Not Found : " + id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (photo != null) {
			URL imgUrl = new URL(photo.getUrl());

			try {
				response.reset();
				// TODO : please include png , gif ...etc
				response.setContentType("image/jpeg");
				response.setBufferSize(DEFAULT_BUFFER_SIZE);
				response.setHeader("Content-Length", String.valueOf(photo.getSize()));
				response.setHeader("Content-Disposition", "inline;filename=\"" + photo.getName() + "\"");
				OutputStream out = response.getOutputStream();

				byte[] chunk = new byte[4096];
				int bytesRead;
				InputStream in = imgUrl.openStream();

				while ((bytesRead = in.read(chunk)) > 0) {
					out.write(chunk, 0, bytesRead);
				}

				out.flush();
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@GetMapping(path = "/photo/biblio/{id}")
	public void biblioPhoto(@PathVariable("id") String id, HttpServletResponse response) throws MalformedURLException {

		Photo photo = null;
		
		try {
			photo = this.photoService.findBiblioByImageId(id);
		} catch(ConnectException cex) {
			logger.info("MINIO LOG : Biblio Image Not Found : " + id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (photo != null) {
			URL imgUrl = new URL(photo.getUrl());

			try {
				response.reset();
				// TODO : please include png , gif ...etc
				response.setContentType("image/jpeg");
				response.setBufferSize(DEFAULT_BUFFER_SIZE);
				response.setHeader("Content-Length", String.valueOf(photo.getSize()));
				response.setHeader("Content-Disposition", "inline;filename=\"" + photo.getName() + "\"");
				OutputStream out = response.getOutputStream();

				byte[] chunk = new byte[4096];
				int bytesRead;
				InputStream in = imgUrl.openStream();

				while ((bytesRead = in.read(chunk)) > 0) {
					out.write(chunk, 0, bytesRead);
				}

				out.flush();
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	@GetMapping(path = "/thumbnail.photo/biblio/{id}")
	public void biblioThumbnailPhoto(@PathVariable("id") String id, HttpServletResponse response) throws MalformedURLException {

		Photo photo = null;
		
		try {
			photo = this.photoService.findBiblioThumbnailByImageId(id);
		} catch(ConnectException cex) {
			logger.info("MINIO LOG : Biblio Thumbnail Not Found : " + id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (photo != null) {
			URL imgUrl = new URL(photo.getUrl());

			try {
				response.reset();
				// TODO : please include png , gif ...etc
				response.setContentType("image/jpeg");
				response.setBufferSize(DEFAULT_BUFFER_SIZE);
				response.setHeader("Content-Length", String.valueOf(photo.getSize()));
				response.setHeader("Content-Disposition", "inline;filename=\"" + photo.getName() + "\"");
				OutputStream out = response.getOutputStream();

				byte[] chunk = new byte[4096];
				int bytesRead;
				InputStream in = imgUrl.openStream();

				while ((bytesRead = in.read(chunk)) > 0) {
					out.write(chunk, 0, bytesRead);
				}

				out.flush();
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
}
