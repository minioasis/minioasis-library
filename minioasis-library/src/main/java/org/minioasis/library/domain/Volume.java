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

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.Length;


@Embeddable
public class Volume {

	@Column(name = "publishing_date")
	private LocalDate publishingDate;
	
	@Length(max = 32)
	@Column(name = "volume_no" , length = 32)
	private String volumeNo;
	
	public Volume() {
	}

	public Volume(LocalDate publishingDate, String volumeNo) {
		this.publishingDate = publishingDate;
		this.volumeNo = volumeNo;
	}

	public LocalDate getPublishingDate() {
		return this.publishingDate;
	}

	public void setPublishingDate(LocalDate publishingDate) {
		this.publishingDate = publishingDate;
	}

	public String getVolumeNo() {
		return this.volumeNo;
	}

	public void setVolumeNo(String volumeNo) {
		this.volumeNo = volumeNo;
	}

}
