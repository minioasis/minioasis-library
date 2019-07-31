package org.minioasis.library.service;

import java.util.Set;

import org.minioasis.library.domain.search.Shelfmark;
import org.springframework.stereotype.Service;

@Service
public class ShelfmarkFormatImpl implements ShelfmarkFormat {

	private static String NEW_LINE = "\n";
	
	public Set<Shelfmark> format(Set<Shelfmark> shelfmarks) {

		for (Shelfmark s : shelfmarks) {

			String shelfmark = s.getShelfmark();

			if (shelfmark != null && !shelfmark.equals("")) {

				String parameters[] = shelfmark.split(" ");

				if (parameters.length == 1) {

					s.setShelfmark(parameters[0]);

				} else if (parameters.length == 2) {

					s.setShelfmark(parameters[0] + NEW_LINE + parameters[1]);

				} else if (parameters.length > 2) {

					int first = parameters[0].length();
					int second = parameters[1].length();
					int index = first + second + 1;

					if (shelfmark.length() == index || shelfmark.length() == (index + 1)) {
						s.setShelfmark(parameters[0] + NEW_LINE + parameters[1]);
					} else {
						s.setShelfmark(parameters[0] + NEW_LINE + parameters[1] + NEW_LINE + shelfmark.substring(index + 1));
					}
				}
			}
		}
		
		return shelfmarks;
	}

}
