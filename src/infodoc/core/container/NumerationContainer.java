package infodoc.core.container;

import infodoc.core.dto.Numeration;
import infodoc.core.dto.Form;

import java.util.Calendar;

@SuppressWarnings("unchecked")
public class NumerationContainer extends UserGroupFilteredContainer<Numeration> {

	private static final long serialVersionUID = 1L;

	public NumerationContainer() {
		super(Numeration.class, "userGroup.id");
	}
	
	@Override
	public void beforeSaveOrUpdate(Numeration numeration) {
		if(numeration.getUserGroup() == null) {
			numeration.setUserGroup(getUserGroup().getParentUserGroup());
		}
	}
	
	public synchronized Long getNextValue(Long formId) {
		Long next = null;
		
		Form form = InfodocContainerFactory.getFormContainer().getEntity(formId);
		
		if(form != null && form.getNumeration() != null) {
			Numeration numeration = getEntity(form.getNumeration().getId());
			
			if(numeration != null) {
				restartIfNeeded(numeration);
				next = numeration.getNextValue();
				numeration.setNextValue(next + 1);
				saveOrUpdateEntity(numeration);
			}
		}
		
		return next;
	}
	
	public void restartIfNeeded(Numeration numeration) {
		if(numeration.getDailyRestart() || numeration.getMonthlyRestart() || numeration.getAnnualRestart()) {
			Calendar now = Calendar.getInstance();
			
			if(numeration.getNextRestartDate() == null) {
				numeration.setNextRestartDate(getNextRestartDate(numeration).getTime());
				
			} else if(numeration.getNextRestartDate().compareTo(now.getTime()) <= 0) {
				numeration.setNextRestartDate(getNextRestartDate(numeration).getTime());
				numeration.setNextValue(numeration.getInitialValue());
			}
		}
	}
	
	
	private Calendar getNextRestartDate(Numeration numeration) {
		Calendar nextRestartDate = Calendar.getInstance();
		nextRestartDate.set(Calendar.HOUR_OF_DAY, 0);
		nextRestartDate.set(Calendar.MINUTE, 0);
		nextRestartDate.set(Calendar.SECOND, 0);
		nextRestartDate.set(Calendar.MILLISECOND, 0);
		
		if(numeration.getDailyRestart()) {
			nextRestartDate.add(Calendar.DAY_OF_MONTH, 1);
			
		} else if(numeration.getMonthlyRestart()) {
			nextRestartDate.add(Calendar.MONTH, 1);
			
		} else if(numeration.getAnnualRestart()) {
			nextRestartDate.add(Calendar.YEAR, 1);
			
		} else {
			return null;
		}
		
		return nextRestartDate;
	}

}
