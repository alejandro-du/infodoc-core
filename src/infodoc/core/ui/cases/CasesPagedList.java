package infodoc.core.ui.cases;

import infodoc.core.InfodocConstants;
import infodoc.core.container.CaseContainer;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.Case;

import java.util.ArrayList;
import java.util.Collection;

import org.vaadin.pagingcomponent.PagingComponent;
import org.vaadin.pagingcomponent.PagingComponent.ChangePageEvent;
import org.vaadin.pagingcomponent.PagingComponent.PagingComponentListener;

public class CasesPagedList extends CasesList implements PagingComponentListener<Long> {

	private static final long serialVersionUID = 1L;
	
	ArrayList<Long> caseDtosIds = new ArrayList<Long>();
	
	private PagingComponent<Long> pagingComponent;
	
	public void addProcesInstances(Collection<Long> caseDtosIds) {
		this.caseDtosIds.addAll(caseDtosIds);
		
		if(pagingComponent != null) {
			layout.removeComponent(pagingComponent);
		}
		
		if(!caseDtosIds.isEmpty()) {
			pagingComponent = new PagingComponent<Long>(InfodocConstants.infodocCasesPerPage, caseDtosIds, this);
			layout.addComponent(pagingComponent);
		}
	}

	@Override
    public void removeAllComponents() {
		super.removeAllComponents();
		caseDtosIds.clear();
	}
	
	@Override
	public void displayPage(ChangePageEvent<Long> event) {
		CaseContainer caseDtoContainer = InfodocContainerFactory.getCaseContainer();
		super.removeAllComponents();
		
		for(int i = event.getPageRange().getIndexPageStart(); i < event.getPageRange().getIndexPageEnd(); i++) {
			Case caseDto = caseDtoContainer.getEntity(caseDtosIds.get(i));
			addAtEnd(caseDto, null);
		}
	}
	
}
