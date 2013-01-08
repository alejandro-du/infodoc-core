package infodoc.core.ui.processinstance;

import infodoc.core.InfodocConstants;
import infodoc.core.container.ProcessInstanceContainer;
import infodoc.core.container.InfodocContainerFactory;
import infodoc.core.dto.ProcessInstance;

import java.util.ArrayList;
import java.util.Collection;

import org.vaadin.pagingcomponent.PagingComponent;
import org.vaadin.pagingcomponent.PagingComponent.ChangePageEvent;
import org.vaadin.pagingcomponent.PagingComponent.PagingComponentListener;

public class ProcessInstancesPagedList extends ProcessInstancesList implements PagingComponentListener<Long> {

	private static final long serialVersionUID = 1L;
	
	ArrayList<Long> processInstancesIds = new ArrayList<Long>();
	
	private PagingComponent<Long> pagingComponent;
	
	public void addProcesInstances(Collection<Long> processInstancesIds) {
		this.processInstancesIds.addAll(processInstancesIds);
		
		if(pagingComponent != null) {
			layout.removeComponent(pagingComponent);
		}
		
		if(!processInstancesIds.isEmpty()) {
			pagingComponent = new PagingComponent<Long>(InfodocConstants.infodocProcessInstancesPerPage, processInstancesIds, this);
			layout.addComponent(pagingComponent);
		}
	}

	@Override
    public void removeAllComponents() {
		super.removeAllComponents();
		processInstancesIds.clear();
	}
	
	@Override
	public void displayPage(ChangePageEvent<Long> event) {
		ProcessInstanceContainer processInstanceContainer = InfodocContainerFactory.getProcessInstanceContainer();
		super.removeAllComponents();
		
		for(int i = event.getPageRange().getIndexPageStart(); i < event.getPageRange().getIndexPageEnd(); i++) {
			ProcessInstance processInstance = processInstanceContainer.getEntity(processInstancesIds.get(i));
			addAtEnd(processInstance, null);
		}
	}
	
}
