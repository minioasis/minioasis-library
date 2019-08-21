package org.minioasis.library.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity audit = (AuditRevisionEntity) revisionEntity;
        audit.setUsername(getPrincipal());
    }
    
	private String getPrincipal(){
		
		String userName = "BY_SYSTEM";	

		if(SecurityContextHolder.getContext().getAuthentication() != null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (principal instanceof UserDetails) {
				userName = ((UserDetails)principal).getUsername();
			} else {
				userName = principal.toString();
			}
		}
			
		return userName;
	}
}
