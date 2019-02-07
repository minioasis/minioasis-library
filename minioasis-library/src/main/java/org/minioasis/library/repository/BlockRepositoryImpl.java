package org.minioasis.library.repository;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.CacheMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.minioasis.library.domain.Block;
import org.minioasis.library.domain.BlockState;

public class BlockRepositoryImpl implements BlockRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	public void refreshLibraryBlockStates() {

		Session session = em.unwrap(Session.class);

		ScrollableResults libraryBlockCursor = session
				.createQuery("from Block")
				.setCacheMode(CacheMode.IGNORE)
				.scroll(ScrollMode.FORWARD_ONLY);

		int count = 0;
		Date now = new Date();

		while (libraryBlockCursor.next()) {

			Block block = (Block) libraryBlockCursor.get(0);

			Date startDate = block.getStartDate();
			Date expiryDate = block.getExpiryDate();

			if (startDate.after(now) || expiryDate.before(now)) {
				block.setState(BlockState.NONACTIVE);
			} else {
				block.setState(BlockState.ACTIVE);
			}

			if (++count % 20 == 0) {
				// flush a batch of updates and release memory:
				session.flush();
				session.clear();
			}

		}
		
	}

}
