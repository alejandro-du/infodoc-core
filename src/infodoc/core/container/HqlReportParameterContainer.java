package infodoc.core.container;

import infodoc.core.dto.HqlReportParameter;
import enterpriseapp.hibernate.DefaultHbnContainer;

@SuppressWarnings("unchecked")
public class HqlReportParameterContainer extends DefaultHbnContainer<HqlReportParameter> {
	
	private static final long serialVersionUID = 1L;

	public HqlReportParameterContainer() {
		super(HqlReportParameter.class);
	}

}
