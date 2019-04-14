package org.minioasis.library.domain.search;

import java.io.Serializable;

import org.minioasis.library.domain.ItemDuration;
import org.minioasis.library.domain.ItemStatus;
import org.minioasis.library.domain.Location;

public class ItemAttributes implements Serializable {

	private static final long serialVersionUID = 3177665731300224021L;

	private String which = "";
	private ItemDuration itemDuration = new ItemDuration();
	private ItemStatus itemStatus = new ItemStatus();
	private Location location = new Location();
	
	public String getWhich() {
		return which;
	}
	public void setWhich(String which) {
		this.which = which;
	}
	public ItemDuration getItemDuration() {
		return itemDuration;
	}
	public void setItemDuration(ItemDuration itemDuration) {
		this.itemDuration = itemDuration;
	}
	public ItemStatus getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(ItemStatus itemStatus) {
		this.itemStatus = itemStatus;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
}
