package org.minioasis.library.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Filter;
import org.hibernate.validator.constraints.Length;
import org.minioasis.library.domain.util.ReservationComparator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "biblio")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Biblio implements Serializable {

	private static final long serialVersionUID = -4071349509234076072L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
    @NotNull
    @Column(name = "biblio_type", nullable = false , updatable = true , columnDefinition = "VARCHAR(20)")
	@Enumerated(EnumType.STRING)
	private BiblioType biblioType;
    
	@NotNull
	@Column(name = "title", nullable = false)
	private String title;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "image_id")
	private Image image;

	@Length(max = 16)
	private String isbn;
	
	@Valid
	private Journal journal = new Journal();
	
	@Length(max = 30)
	private String edition;

	@Column(columnDefinition = "INT(10) UNSIGNED")
	private Integer pages;
	
	@Column(columnDefinition = "CHAR(1)")
	@Enumerated(EnumType.STRING)
	private Binding binding;

	@NotNull
	@Column(nullable = false , columnDefinition = "CHAR(2)")
	@Enumerated(EnumType.STRING)
	private Language language;
	
	@Length(max = 20)
	@Column(name = "class_mark")
	private String classMark;

	@Column(name = "publishing_year")
	private Integer publishingYear;
	
	@Length(max = 40)
	@Column(name = "publication_place")
	private String publicationPlace;
	
   	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated")
	private Date updated = new Date();

	@NotNull
	@Column(nullable = false , columnDefinition = "CHAR(1)")
	@Enumerated(EnumType.STRING)
	private YesNo active;

	@Length(max = 256)
	private String author;
	
	@Length(max = 256)
	@Column(name = "topic")
	private String subject;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name="publisher_id", foreignKey = @ForeignKey(name = "fk_biblio_publisher"))
	private Publisher publisher;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name="series_id", foreignKey = @ForeignKey(name = "fk_biblio_series"))
	private Series series;
 
    @ManyToMany
    @JoinTable(name = "biblio_tag", 
	joinColumns = @JoinColumn(name = "biblio_id", referencedColumnName = "id"), 
	inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))   
    private Set<Tag> tags = new HashSet<Tag>();
    
	@Length(max = 256)
	private String description;
	
	@Column(columnDefinition = "TEXT")
	private String note;

	@OneToMany(mappedBy="biblio" , fetch = FetchType.LAZY)
	private List<Item> items = new ArrayList<Item>();
    
    @OneToMany(mappedBy="biblio")
    @OrderBy("reservationDate ASC")
    @Filter(name = "reservationStateFilter")
	private List<Reservation> reservations = new ArrayList<Reservation>(); 
    
	public Biblio() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BiblioType getBiblioType() {
		return biblioType;
	}

	public void setBiblioType(BiblioType biblioType) {
		this.biblioType = biblioType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Journal getJournal() {
		return journal;
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public Binding getBinding() {
		return binding;
	}

	public void setBinding(Binding binding) {
		this.binding = binding;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public String getClassMark() {
		return classMark;
	}

	public void setClassMark(String classMark) {
		this.classMark = classMark;
	}

	public Integer getPublishingYear() {
		return publishingYear;
	}

	public void setPublishingYear(Integer publishingYear) {
		this.publishingYear = publishingYear;
	}

	public String getPublicationPlace() {
		return publicationPlace;
	}

	public void setPublicationPlace(String publicationPlace) {
		this.publicationPlace = publicationPlace;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public YesNo getActive() {
		return active;
	}

	public void setActive(YesNo active) {
		this.active = active;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Series getSeries() {
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	// Item
	public void addItem(Item item){
		if(item == null)
			throw new IllegalArgumentException("Can't add a null item.");
		
		this.items.add(item);
	}
	
	public void removeItem(Item item) {
		if(item == null)
			throw new IllegalArgumentException("Can't remove a null item.");
		
		this.items.remove(item);
	}

	public void removeItems(Set<Item> items) {
		if (items == null)
			throw new IllegalArgumentException("Can't remove a null items.");

		this.items.removeAll(items);
	}
	
	// Reservation
	public void addReservation(Reservation reservation) {
		if (reservation == null)
			throw new IllegalArgumentException("Can't add a null reservation.");
		
		this.reservations.add(reservation);
	}
	
	public void removeReservation (Reservation reservation) {
		if (reservation == null)
			throw new IllegalArgumentException("Can't remove a null reservation.");

		this.reservations.remove(reservation);
	}
	
	public void removeReservations(Set<Reservation> reservations) {
		if (reservations == null)
			throw new IllegalArgumentException("Can't remove a null reservations.");

		this.reservations.removeAll(reservations);

	}
	
	// ************************************  Domain Logic  *************************************
	
	public boolean hasReservation() {
		return (reservations.size() != 0);
	}
	
	public boolean isReserved(){
		
		boolean result = false;
		int count = 0;

		for(Reservation r : reservations){
			if(r.getState().equals(ReservationState.RESERVE))
				count = count + 1;
		}
		
		if(count != 0)
			result = true;
		
		return result;
	}

	private int noOfReservationsInAvailableState() {
		int count = 0;
		for(Reservation r : reservations){
			if(r.getState().equals(ReservationState.AVAILABLE) ||
			   r.getState().equals(ReservationState.NOTIFIED)){
				count = count + 1;
			}
		}
		return count;
		
	}
	
	public boolean amlThePickupCandidate(Patron patron) {
		
		int reservedInLibraryItemNo = noOfReservedInLibraryItems();
		
		if(noOfReservationsInAvailableState() > reservedInLibraryItemNo){
			return true;
		}else{
			Collections.sort(reservations,new ReservationComparator());

			List<Reservation> subRes = reservations.subList(0,reservedInLibraryItemNo);
			
			for(Reservation r : subRes){
				if(r.getPatron().getEntangled().equals(patron.getEntangled())){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Reservation findFirstReservationInReservedState() {
		
		List<Reservation> reservationsInReservedState = new ArrayList<Reservation>();
		
		for(Reservation r : reservations){
			if(r.getState().equals(ReservationState.RESERVE))
				reservationsInReservedState.add(r);
		}
		
		if(reservationsInReservedState.size() == 0)
			return null;
		
		Collections.sort(reservationsInReservedState,new ReservationComparator());
		
		return reservationsInReservedState.get(0);
	}
	
	private int noOfReservedInLibraryItems(){
		
		int count = 0;
		
		for(Item i : items) {
			if(i.getState().equals(ItemState.RESERVED_IN_LIBRARY)){
				count = count + 1;
			}
		}
		
		return count;
	}

    @Override
	public boolean equals(Object other) {
		
		if(this == other) 
			return true;
		if(other == null)	
			return false;
		if(!(other instanceof Biblio))	
			return false;
		final Biblio that = (Biblio) other;
		return id != null && id.equals(that.getId());
		
	}

    @Override
    public int hashCode() {
        return 33;
    }	

}
