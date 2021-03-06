package infodoc.core.container;

import infodoc.core.dto.HqlReport;

import java.util.Collection;
import java.util.List;

import enterpriseapp.hibernate.DefaultHbnContainer;

@SuppressWarnings("unchecked")
public class HqlReportContainer extends DefaultHbnContainer<HqlReport> {
	
	private static final long serialVersionUID = 1L;

	public HqlReportContainer() {
		super(HqlReport.class);
	}
	
	public List<HqlReport> listByUserGroupIdAndFormId(Long userGroupId, Long formId) {
		return query(
			"select r" +
			" from HqlReport r join" +
			" r.userGroups g" +
			" where g.id = ?" +
			" and r.form.id = ?" +
			" and r.disabled is false",
			new Object[] {userGroupId, formId});
	}
	
	public Collection<?> getQueryResult(String hqlQuery, String[] properties, Class<?>[] classes, String[] paramNames, Object[] params) {
		return parseSpecialQueryResult(
			specialQuery(hqlQuery, paramNames, params),
			properties,
			classes
		);
	}

}
