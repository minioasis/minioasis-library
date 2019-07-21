package org.minioasis.library.domain.validator;

import org.minioasis.library.domain.ItemStatus;
import org.minioasis.library.domain.Location;
import org.minioasis.library.domain.search.ItemAttributes;
import org.springframework.validation.Errors;

public class ItemAttributesValidator {

	public void validate(Object command, Errors errors) {
		
		ItemAttributes a = (ItemAttributes)command;
		String which = a.getWhich();
		ItemStatus status = a.getItemStatus();
		Location location = a.getLocation();
		
		if(which.equals("itemstatus")){
			
			String name = status.getName();
			Boolean borrowable = status.getBorrowable();
			Boolean reservable = status.getReservable();
			
			// name
			if (name == null || name.equals("")) {		
				errors.rejectValue("itemStatus.name","*", null ,"required");	
			}else{
				if(name.length() > 64)
					errors.rejectValue("itemStatus.name", "" , null ,"< 65");	
			}
			
			// borrowable
			if(borrowable == null){
				errors.rejectValue("itemStatus.borrowable", "*" , null , "required");
			}
			
			// reservable
			if(reservable == null){
				errors.rejectValue("itemStatus.reservable", "*" , null , "required");
			}
			
		}
		
		if(which.equals("location")){
			
			String name = location.getName();
			// location
			if (name == null || name.equals("")) {
				errors.rejectValue("location.name", "*" , null , "required");
			}else{
				if(name.length() > 64)
					errors.rejectValue("location.name", "" , null , "< 65");
			}
		}
		
	}
}
