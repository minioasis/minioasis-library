package org.minioasis.library.service;

import org.jooq.Record3;
import org.jooq.Result;

public interface ReportService {

	Result<Record3<Integer, String, Integer>> CountPatronsByTypes();
}
