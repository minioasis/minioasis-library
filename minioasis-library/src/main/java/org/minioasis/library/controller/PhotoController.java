package org.minioasis.library.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.Photo;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PhotoController {

	@Autowired
	private LibraryService service;
	
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
	
	@RequestMapping("/patron/{id}/photo")
	public String show(@PathVariable("id") long id, HttpServletResponse response) {

		Patron patron = this.service.getPatron(id);
		
		Photo photo = patron.getPhoto();

		if (photo != null) {

			byte[] img = photo.getImg();

			try {
				System.out.println("+++++++++++++++++++++++++++++++++++++++++" + img.length);
				response.reset();
				// TODO : please include png , gif ...etc
				response.setContentType("image/jpeg");
				response.setBufferSize(DEFAULT_BUFFER_SIZE);
				response.setHeader("Content-Length", String.valueOf(img.length));
				response.setHeader("Content-Disposition", "inline;filename=\""
						+ photo.getName() + "\"");
				OutputStream out = response.getOutputStream();

				out.write(img);
				out.flush();
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (DataAccessException e) {
				e.printStackTrace();
			}

		}

		return null;
	}

}
