package infodoc.core.ui.cases;

import infodoc.core.InfodocConstants;
import infodoc.core.container.PropertyValueContainer;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.PropertyValue;
import infodoc.core.dto.Case;
import infodoc.core.dto.Form;
import infodoc.core.dto.ActivityInstance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfWriter;

import enterpriseapp.Utils;

public class CasePrintService {

	public static byte[] generateFile(Case caseDto) throws IOException, DocumentException {
		Form form = InfodocContainerFactory.getFormContainer().getEntity(caseDto.getForm().getId());
		
		if(caseDto.getId() != null) {
			caseDto = InfodocContainerFactory.getCaseContainer().getEntity(caseDto.getId());
		}
		
		Integer width = caseDto.getForm().getWidth();
		Integer height = caseDto.getForm().getHeight();
		Integer hMargin= caseDto.getForm().getHorizontalMargin();
		Integer vMargin = caseDto.getForm().getVerticalMargin();
		
		width = width == null ? 50 : width;
		height = height == null ? 13 : height;
		hMargin = hMargin == null ? 1 : hMargin;
		vMargin = vMargin == null ? 1 : vMargin;
		
		Document document = new Document(new Rectangle(mmToPoints(width), mmToPoints(height)), mmToPoints(hMargin), mmToPoints(hMargin), mmToPoints(vMargin), mmToPoints(vMargin));
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		PdfWriter pdfWriter = PdfWriter.getInstance(document, outStream);
		pdfWriter.setOpenAction(new PdfAction(PdfAction.PRINTDIALOG));
		
		document.addTitle(caseDto.toString());
		document.addCreator(InfodocConstants.infodocCompanyName);
		document.addCreationDate();
		document.open();
		
		HTMLWorker htmlWorker = new HTMLWorker(document);
		htmlWorker.setPageSize(new Rectangle(mmToPoints(width), mmToPoints(height)));
		htmlWorker.parse(new StringReader(replaceValues(form.getPrintTemplate(), caseDto)));
		
		document.close();
		
		return outStream.toByteArray();
	}
	
	protected static String replaceValues(String template, Case caseDto) {
		String output = template;
		String firstActivityInstanceDate = "?";
		ActivityInstance firstActivityInstance = InfodocContainerFactory.getCaseContainer().getFisrtActivityInstance(caseDto);
		PropertyValueContainer propertyValueContainer = InfodocContainerFactory.getPropertyValueContainer();
		
		if(firstActivityInstance != null) {
			firstActivityInstanceDate = Utils.dateTimeToString(firstActivityInstance.getExecutionTime());
		}
		
		for(PropertyValue propertyValue : caseDto.getPropertyValues()) {
			String value = propertyValueContainer.getStringValue(propertyValue);
			
			if(value == null) {
				value = "";
			}
			
			output = output.replace(":" + propertyValue.getProperty().getName(), value);
		}
		
		output = output
			.replace("${company}", InfodocConstants.infodocCompanyName)
			.replace("${number}", caseDto.toString())
			.replace("${firstActivityInstanceDate}", firstActivityInstanceDate);
		
		return output;
	}
	
	protected static float mmToPoints(int f) {
		return (f / 25.4f) * 72; // 1in = 25.4mm = 72pt
	}
}
