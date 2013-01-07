package infodoc.core.ui.processinstance;

import infodoc.core.InfodocConstants;
import infodoc.core.container.PropertyValueContainer;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.PropertyValue;
import infodoc.core.dto.ProcessInstance;
import infodoc.core.dto.Process;
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

public class ProcessInstancePrintService {

	public static byte[] generateFile(ProcessInstance processInstance) throws IOException, DocumentException {
		Process process = InfodocContainerFactory.getProcessContainer().getEntity(processInstance.getProcess().getId());
		
		if(processInstance.getId() != null) {
			processInstance = InfodocContainerFactory.getProcessInstanceContainer().getEntity(processInstance.getId());
		}
		
		Integer width = processInstance.getProcess().getWidth();
		Integer height = processInstance.getProcess().getHeight();
		Integer hMargin= processInstance.getProcess().getHorizontalMargin();
		Integer vMargin = processInstance.getProcess().getVerticalMargin();
		
		width = width == null ? 50 : width;
		height = height == null ? 13 : height;
		hMargin = hMargin == null ? 1 : hMargin;
		vMargin = vMargin == null ? 1 : vMargin;
		
		Document document = new Document(new Rectangle(mmToPoints(width), mmToPoints(height)), mmToPoints(hMargin), mmToPoints(hMargin), mmToPoints(vMargin), mmToPoints(vMargin));
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		PdfWriter pdfWriter = PdfWriter.getInstance(document, outStream);
		pdfWriter.setOpenAction(new PdfAction(PdfAction.PRINTDIALOG));
		
		document.addTitle(processInstance.toString());
		document.addCreator(InfodocConstants.infodocCompanyName);
		document.addCreationDate();
		document.open();
		
		HTMLWorker htmlWorker = new HTMLWorker(document);
		htmlWorker.setPageSize(new Rectangle(mmToPoints(width), mmToPoints(height)));
		htmlWorker.parse(new StringReader(replaceValues(process.getPrintTemplate(), processInstance)));
		
		document.close();
		
		return outStream.toByteArray();
	}
	
	protected static String replaceValues(String template, ProcessInstance processInstance) {
		String output = template;
		String firstActivityInstanceDate = "?";
		ActivityInstance firstActivityInstance = InfodocContainerFactory.getProcessInstanceContainer().getFisrtActivityInstance(processInstance);
		PropertyValueContainer propertyValueContainer = InfodocContainerFactory.getPropertyValueContainer();
		
		if(firstActivityInstance != null) {
			firstActivityInstanceDate = Utils.dateTimeToString(firstActivityInstance.getExecutionTime());
		}
		
		for(PropertyValue propertyValue : processInstance.getPropertyValues()) {
			String value = propertyValueContainer.getStringValue(propertyValue);
			
			if(value == null) {
				value = "";
			}
			
			output = output.replace(":" + propertyValue.getProperty().getName(), value);
		}
		
		output = output
			.replaceAll(":company", InfodocConstants.infodocCompanyName)
			.replaceAll(":number", processInstance.toString())
			.replaceAll(":firstActivityInstanceDate", firstActivityInstanceDate);
		
		return output;
	}
	
	protected static float mmToPoints(int f) {
		return (f / 25.4f) * 72; // 1in = 25.4mm = 72pt
	}
}
