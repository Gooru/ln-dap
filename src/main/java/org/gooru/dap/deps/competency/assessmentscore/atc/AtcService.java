package org.gooru.dap.deps.competency.assessmentscore.atc;

import java.util.UUID;

import org.skife.jdbi.v2.DBI;


/**
 * @author mukul@gooru
 */
public class AtcService {
	
	private final AtcDao atcDao;
	
	public AtcService(DBI coreDbi) {
		this.atcDao = coreDbi.onDemand(AtcDao.class);
	}

	Integer fetchGradefromClassMembers(String userId, String classId) {
		return atcDao.fetcheGradefromClassMembers(UUID.fromString(userId), UUID.fromString(classId));
	}
	
	Integer fetchGradefromClass(String classId, String courseId) {
		return atcDao.fetcheGradefromClass(UUID.fromString(classId), UUID.fromString(courseId));
	}
}
