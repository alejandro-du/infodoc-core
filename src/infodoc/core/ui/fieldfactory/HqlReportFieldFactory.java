package infodoc.core.ui.fieldfactory;

import java.util.Set;

import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.HqlReport;
import infodoc.core.dto.HqlReportParameter;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;

import enterpriseapp.ui.crud.DefaultCrudFieldFactory;
import enterpriseapp.ui.crud.EmbeddedCrudComponent;
import enterpriseapp.ui.crud.EntitySetContainer;
import enterpriseapp.ui.crud.EntityTable;

public class HqlReportFieldFactory extends DefaultCrudFieldFactory {

	private static final long serialVersionUID = 1L;

	@Override
	public Field createCustomField(Object bean, final Item item, String pid, Component uiContext, Class<?> propertyType) {
		Field field = null;
		
		if("hqlQuery".equals(pid)) {
			field = new TextArea();
			
		} else if("hqlParameters".equals(pid)) {
			HqlReport report = (HqlReport) bean;
			Set<HqlReportParameter> hqlParameters = null;
			
			if(report != null && report.getId() != null) {
				report = InfodocContainerFactory.getHqlReportContainer().getEntity(report.getId());
				hqlParameters = report.getHqlParameters();
			}
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			EntityTable<HqlReportParameter> entityTableField = new EntityTable(HqlReportParameter.class, hqlParameters, new EmbeddedCrudComponent(HqlReportParameter.class, this, new EntitySetContainer(HqlReportParameter.class)));
			entityTableField.getCrudComponent().getTable().setTableFieldFactory(new InfodocFieldFactory());
			
			field = entityTableField;
		}
		
		return field;
	}
	
}
