package infodoc.core.container;

import infodoc.core.dto.JavaReport;

@SuppressWarnings("unchecked")
public class JavaReportContainer extends UserGroupFilteredContainer<JavaReport> {
	
	private static final long serialVersionUID = 1L;

	public JavaReportContainer() {
		super(JavaReport.class, "userGroup.id");
	}

	@Override
	public void beforeSaveOrUpdate(JavaReport javaReport) {
		if(javaReport.getUserGroup() == null) {
			javaReport.setUserGroup(getUserGroup().getParentUserGroup());
		}
	}
	
}
