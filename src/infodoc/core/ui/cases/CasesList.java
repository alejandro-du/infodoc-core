package infodoc.core.ui.cases;

import infodoc.core.dto.Case;

import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class CasesList extends CustomComponent {

	private static final long serialVersionUID = 1L;
	
	protected VerticalLayout layout;
	protected VerticalLayout instancesLayout;
	
	public CasesList() {
		instancesLayout = new VerticalLayout();
		
		layout = new VerticalLayout();
		layout.addComponent(instancesLayout);
		
		setCompositionRoot(layout);
	}
	
	public void addAtBeginning(Case instance) {
		addAtBeginning(instance, null);
	}
	
	public void addAtBeginning(Case instance, Component rightComponent) {
		addInstance(instance, rightComponent, true);
	}
	
	public void addAtEnd(Case instance, Component rightComponent) {
		addInstance(instance, rightComponent, false);
	}
	
	@Override
    public void removeAllComponents() {
		instancesLayout.removeAllComponents();
	}

	protected void addInstance(Case instance, Component rightComponent, boolean addAtBeginning) {
		AbstractLayout instanceLayout = getCaseLayout(instance, rightComponent);
		
		if(addAtBeginning) {
			instancesLayout.addComponentAsFirst(instanceLayout);
		} else {
			instancesLayout.addComponent(instanceLayout);
		}
	}

	protected AbstractLayout getCaseLayout(Case instance, Component rightComponent) {
		CaseBox boxComponent = new CaseBox(instance);
		AbstractLayout instanceLayout;
		
		if(rightComponent == null) {
			HorizontalLayout horizontalLayout = new HorizontalLayout();
			horizontalLayout.addComponent(boxComponent);
			horizontalLayout.setMargin(false, false, true, false);
			instanceLayout = horizontalLayout;
		} else {
			VerticalLayout firstComponent = new VerticalLayout();
			firstComponent.setMargin(false, true, true, false);
			firstComponent.addComponent(boxComponent);
			
			VerticalLayout secondComponent = new VerticalLayout();
			//secondComponent.setMargin(false, false, true, true);
			secondComponent.setMargin(false, false, true, false);
			secondComponent.addComponent(rightComponent);
			
			/* TODO: Enable if ticket http://dev.vaadin.com/ticket/6024 gets solved
			HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
			splitPanel.setFirstComponent(firstComponent);
			splitPanel.setSecondComponent(secondComponent);
			
			instanceLayout = splitPanel;
			//*/
			
			//*
			HorizontalLayout horizontalLayout = new HorizontalLayout();
			horizontalLayout.addComponent(firstComponent);
			horizontalLayout.addComponent(secondComponent);
			instanceLayout = horizontalLayout;
			//*/
		}
		
		instanceLayout.setWidth("100%");
		return instanceLayout;
	}
	
}
