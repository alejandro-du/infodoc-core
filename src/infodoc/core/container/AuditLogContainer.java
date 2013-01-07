package infodoc.core.container;

import infodoc.core.dto.AuditLog;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import enterpriseapp.hibernate.DefaultHbnContainer;

@SuppressWarnings("unchecked")
public class AuditLogContainer extends DefaultHbnContainer<AuditLog> {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(AuditLogContainer.class);

	public AuditLogContainer() {
		super(AuditLog.class);
		sort(new Object[] {"date"}, new boolean[] {false});
	}
	
	@Override
	public Serializable saveOrUpdateEntity(AuditLog auditLog) {
		try {
			sessionManager.getSession().saveOrUpdate(auditLog);
			
		} catch(Exception e) {
			logger.error("Error updating or saving entity", e);
		}
		return (Serializable) getIdForPojo(auditLog);
	}
	
}
