package infodoc.core.ui.comun;

import infodoc.core.InfodocConstants;
import infodoc.core.dto.JavaReport;
import infodoc.core.dto.Process;
import infodoc.core.dto.User;

import java.awt.Color;
import java.util.List;

import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import enterpriseapp.Utils;
import enterpriseapp.ui.reports.AbstractReport;

public abstract class InfodocReport extends AbstractReport {

	private static final long serialVersionUID = 1L;
	
	protected Process process;
	protected final JavaReport javaReport;
	
	public abstract String getSubtitle();
	
	public InfodocReport(Process process, JavaReport javaReport) {
		this.process = process;
		this.javaReport = javaReport;
	}
	
	public String getTitle() {
		return process.getName() + " - " + javaReport.getName();
	}
	
	@Override
	public String getFileName() {
		return (getTitle().toLowerCase().replace(" - ", "-") + "-" + Utils.getCurrentTimeAndDate()).replace(" ", "-");
	}

	@Override
	public void configureColumn(String property, AbstractColumn column, DynamicReportBuilder reportBuilder) {
		super.configureColumn(property, column, reportBuilder);
		Style style = column.getStyle();
		style.setFont(Font.ARIAL_MEDIUM);
		style.setPaddingLeft(2);
		style.setPaddingRight(2);
		style.setPaddingTop(0);
		style.setPaddingBottom(0);
	};
	
	@Override
	public DynamicReportBuilder getReportBuilder() {
		
		DynamicReportBuilder reportBuilder = super.getReportBuilder();
		
		Style titleStyle = new StyleBuilder(true).setPadding(0).setFont(Font.ARIAL_BIG_BOLD).setHorizontalAlign(HorizontalAlign.CENTER).build();
		reportBuilder.setTitleStyle(titleStyle);
		reportBuilder.setTitleHeight(18);
		reportBuilder.setTitle(getTitle());
		
		if(getSubtitle() != null) {
			Style subtitleStyle = new StyleBuilder(true).setPadding(0).setPaddingBottom(10).setFont(Font.ARIAL_MEDIUM_BOLD).setHorizontalAlign(HorizontalAlign.CENTER).build();
			
			reportBuilder.setSubtitle(getSubtitle());
			reportBuilder.setSubtitleStyle(subtitleStyle);
		}
		
		reportBuilder.setUseFullPageWidth(true);
		
		Style headerStyle = new StyleBuilder(true).setFont(Font.ARIAL_MEDIUM).build();
		User user = (User) getApplication().getUser();
		
		reportBuilder.addAutoText(InfodocConstants.infodocCompanyName, AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 200, headerStyle);
		reportBuilder.addAutoText(user.getUserGroup().toString(), AutoText.POSITION_HEADER, AutoText.ALIGMENT_LEFT, 200, headerStyle);
		reportBuilder.addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_HEADER, AutoText.ALIGNMENT_RIGHT, 200, 10, headerStyle);
		reportBuilder.addAutoText(Utils.getCurrentTimeAndDate(), AutoText.POSITION_HEADER, AutoText.ALIGNMENT_RIGHT, 200, headerStyle);
		
		reportBuilder.setGrandTotalLegend("");
		
		Style footerStyle = new StyleBuilder(true).setFont(Font.ARIAL_MEDIUM).setTextColor(Color.GRAY).build();
		reportBuilder.addAutoText(InfodocConstants.uiInfodoc + " " + InfodocConstants.infodocVersion, AutoText.POSITION_FOOTER, AutoText.ALIGMENT_LEFT, 200, footerStyle);
		
		return reportBuilder;
	}
	
	protected String[] ListToStringArray(List<String> list) {
		String[] array = new String[list.size()];
		
		for(int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		
		return array;
	}
	
	protected Class<?>[] ListToClassArray(List<Class<?>> list) {
		Class<?>[] array = new Class[list.size()];
		
		for(int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		
		return array;
	}
	
}
