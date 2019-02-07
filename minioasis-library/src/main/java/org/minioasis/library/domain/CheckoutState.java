/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  The ASF licenses this file to You
* under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.  For additional information regarding
* copyright in this work, please see the NOTICE file in the top level
* directory of this distribution.
*/

package org.minioasis.library.domain;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum CheckoutState {

	// active state
	CHECKOUT("Check Out"), 
	RENEW("Renew"),
	RETURN_WITH_FINE("Return with fine"),
	RETURN_WITH_DAMAGE("Return with damage"),
	RETURN_WITH_DAMAGE_AND_FINE("Return with damage and fine"), 
	REPORTLOST("Report lost"),
	REPORTLOST_WITH_FINE("Report lost with fine"),
	// completed state
	RETURN("Return"), 
	FINE_PAID("Fine paid"), 
	DAMAGE_PAID("Damage paid"), 
	DAMAGE_AND_FINEPAID("Damage and fine paid"), 
	REPORTLOST_PAID("Report lost paid"), 
	REPORTLOST_AND_FINEPAID("Report lost and fines paid");

	private final String description;
	
	private CheckoutState(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public static List<CheckoutState> getActives(){
		return new ArrayList<CheckoutState>(EnumSet.range(CHECKOUT, REPORTLOST_WITH_FINE));
	}
	
	public static List<CheckoutState> getCheckouts(){
		return new ArrayList<CheckoutState>(EnumSet.range(CHECKOUT, RENEW));
	}
	
	public static List<CheckoutState> getReturnWithFines(){
		return new ArrayList<CheckoutState>(
				EnumSet.of(CheckoutState.RETURN_WITH_FINE));
	}
	
	public static List<CheckoutState> getDamageAndFine(){
		return new ArrayList<CheckoutState>(
				EnumSet.of(CheckoutState.RETURN_WITH_DAMAGE_AND_FINE));
	}
	
	public static List<CheckoutState> getReportLostWithFine(){
		return new ArrayList<CheckoutState>(
				EnumSet.of(CheckoutState.REPORTLOST_WITH_FINE));
	}
	
	public static List<CheckoutState> getDamage(){
		return new ArrayList<CheckoutState>(
				EnumSet.of(CheckoutState.RETURN_WITH_DAMAGE));
	}

	public static List<CheckoutState> getReportLost(){
		return new ArrayList<CheckoutState>(
				EnumSet.of(CheckoutState.REPORTLOST));
	}
	
}