package org.minioasis.library.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.minioasis.library.domain.Photo;
import org.minioasis.library.service.RemoteAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhotoClientController {
	
	@Autowired
	private RemoteAccessService service;

	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

	@GetMapping(path = "/patron/photo/{id}")
	public void patronPhoto(@PathVariable("id") String id, HttpServletResponse response) throws IOException {

		URL url = this.service.getUrl();
		Photo photo = this.service.getRestTemplate().getForObject(url.toString() + "/photo/patron/" + id, Photo.class);

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
	
	@GetMapping(path = "/biblio/photo/{id}")
	public void biblioPhoto(@PathVariable("id") String id, HttpServletResponse response) throws IOException {

		URL url = this.service.getUrl();
		Photo photo = this.service.getRestTemplate().getForObject(url.toString() + "/photo/biblio/" + id, Photo.class);

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
