package org.gooru.dap.deps.competency;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author gooru on 04-May-2018
 */
public abstract class CommonDao {

	@SqlQuery("SELECT taxonomy FROM collection WHERE id = :collectionId::uuid")
	protected abstract String fetchCollectionCompetency(@Bind("collectionId") String collectionId);
}
