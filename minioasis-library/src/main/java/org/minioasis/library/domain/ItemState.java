package org.minioasis.library.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Access;
import javax.persistence.AccessType;

@Embeddable
@Access(AccessType.FIELD)
public class ItemState implements java.io.Serializable {

	private static final long serialVersionUID = -8067586322243984665L;
	
	@Column(name = "state" , nullable = false , columnDefinition = "VARCHAR(25)")
	private String state;
	private boolean borrowable;
	private boolean reservable;

    public static final ItemState IN_LIBRARY = new ItemState("IN_LIBRARY",true,false);
    public static final ItemState RESERVED_IN_LIBRARY = new ItemState("RESERVED_IN_LIBRARY",true,true);
    public static final ItemState CHECKOUT = new ItemState("CHECKOUT",false,true);
    public static final ItemState REPORTLOST = new ItemState("REPORTLOST",false,false);
    public static final ItemState REPORTLOST_AND_PAID = new ItemState("REPORTLOST_AND_PAID",false,false);
    

	private static final Map<String, ItemState> INSTANCES = new HashMap<String, ItemState>();

	static {
		INSTANCES.put(IN_LIBRARY.toString(), IN_LIBRARY);
		INSTANCES.put(RESERVED_IN_LIBRARY.toString(), RESERVED_IN_LIBRARY);
		INSTANCES.put(CHECKOUT.toString(), CHECKOUT);
		INSTANCES.put(REPORTLOST.toString(), REPORTLOST);
		INSTANCES.put(REPORTLOST_AND_PAID.toString(), REPORTLOST_AND_PAID);
	}
	
	/** default constructor */
	public ItemState() {
	}

	/**
	 * Prevent instantiation and subclassing with a private constructor.
	 */

	private ItemState(String state,boolean borrowable,boolean reservable) {
		this.state = state;
		this.borrowable = borrowable;
		this.reservable = reservable;
	}

	// ********************** Common Methods ********************** //

	public String toString() {
		return this.state;
	}
	
	Object readResolve() {
		return getInstance(state);
	}

	public static ItemState getInstance(String name) {
		return (ItemState) INSTANCES.get(name);
	}

	public boolean isBorrowable() {
		return borrowable;
	}
	
	public void setBorrowable(boolean borrowable) {
		this.borrowable = borrowable;
	}
	
	public boolean isReservable() {
		return reservable;
	}

	public void setReservable(boolean reservable) {
		this.reservable = reservable;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public static Collection<ItemState> getItemStates(){
		return INSTANCES.values();
	}

	// **************************************************************************
	
	public boolean equals(Object other) {
		
		if (this == other) return true;
		if (this.state == null)	return false;
		if (!(other instanceof ItemState)) return false;
		final ItemState that = (ItemState) other;

		return this.state.equals(that.getState());
		
	}
	
	public int hashCode() {
		return this.state == null ? System.identityHashCode(this) : this.state.hashCode();
	}

}
