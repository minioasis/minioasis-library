package org.minioasis.library.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.minioasis.library.domain.Attachment;
import org.minioasis.library.domain.CheckoutResult;
import org.minioasis.library.domain.CirculationCode;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.search.CirculationDTO;
import org.minioasis.library.exception.LibraryException;
import org.minioasis.library.service.LibraryService;
import org.minioasis.validation.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/circ")
public class CirculationController {

	@Autowired
	private LibraryService service;

	private void preparingCirculation(Patron patron, CirculationDTO dto, String cardKey) {

		dto.setPatron(patron);
		dto.setCardKey(cardKey);
		
	}
	
	@RequestMapping(value = { "/patronid.form" }, method = RequestMethod.GET)
	public String cardKey(Model model) {
		return "circ.patronid.form";
	}

	@RequestMapping(value = { "/checkout" }, method = RequestMethod.GET)
	public String checkout(@RequestParam(value = "pid", required = true) String cardKey, 
							@ModelAttribute("dto") CirculationDTO dto, Model model) {
		
		Patron patron = this.service.getCirculationPatronByCardKey(cardKey, LocalDate.now());

		if (patron == null) {
			model.addAttribute("exist", "no");
			return "circ.patronid.form";
		}
		
		preparingCirculation(patron, dto, cardKey);

		return "circ.checkout.form";

	}

	@RequestMapping(value = { "/checkin" }, method = RequestMethod.GET)
	public String checkin(@RequestParam(value = "pid", required = true) String cardKey, 
							@ModelAttribute("dto") CirculationDTO dto) {

		Patron patron = this.service.getCirculationPatronByCardKey(cardKey, LocalDate.now());
		preparingCirculation(patron, dto, cardKey);
		
		return "circ.checkin.form";
		
	}

	@RequestMapping(value = { "/renew" }, method = RequestMethod.GET)
	public String renew(@RequestParam(value = "pid", required = true) String cardKey, 
							@ModelAttribute("dto") CirculationDTO dto) {

		Patron patron = this.service.getCirculationPatronByCardKey(cardKey, LocalDate.now());
		preparingCirculation(patron, dto, cardKey);
		
		return "circ.renew.form";
		
	}

	@RequestMapping(value = { "/reportlost" }, method = RequestMethod.GET)
	public String reportLost(@RequestParam(value = "pid", required = true) String cardKey, 
								@ModelAttribute("dto") CirculationDTO dto) {

		Patron patron = this.service.getCirculationPatronByCardKey(cardKey, LocalDate.now());
		preparingCirculation(patron, dto, cardKey);
		
		return "circ.reportlost.form";
		
	}

	@RequestMapping(value = { "/payfine" }, method = RequestMethod.GET)
	public String payFine(@RequestParam(value = "pid", required = true) String cardKey, 
							@ModelAttribute("dto") CirculationDTO dto) {

		Patron patron = this.service.getCirculationPatronByCardKey(cardKey, LocalDate.now());
		preparingCirculation(patron, dto, cardKey);
		
		return "circ.payfine.form";
		
	}

	// Checkout
	@RequestMapping(value = { "/checkout" }, method = RequestMethod.POST)
	public String checkout(@ModelAttribute("dto") CirculationDTO dto, BindingResult result, Model model) {

		if (result.hasErrors())
			return "circ.checkout.form";

		// given Date
		LocalDate given = dto.getGiven();
		if (given == null)
			given = LocalDate.now();

		// patron & item
		String cardKey = dto.getCardKey();
		String barcode = dto.getBarcode();

		Patron patron = this.service.getCirculationPatronByCardKey(cardKey, given);
		
		// for web gui
		dto.setPatron(patron);
		dto.clearHistory();
		
		Item item = this.service.getItemForCheckout(barcode);
		if (item != null) {

			// checkout item
			try {

				this.service.checkout(patron, item, given);

			} catch (LibraryException ex) {

				model.addAttribute("CHECKOUT_ERRORS", ex.getAllErrors());
				return "circ.checkout.form";
			}

		} else {

			Attachment attachment = this.service.getAttachmentForCheckout(barcode);
			if (attachment != null) {

				// checkout attachment
				try {

					this.service.checkoutAttachment(patron, attachment, given);

				} catch (LibraryException ex) {

					model.addAttribute("CHECKOUT_ERRORS", ex.getAllErrors());	
					return "circ.checkout.form";
				}

			} else {

				model.addAttribute("CHECKOUT_ERRORS",
				new Notification(CirculationCode.ITEM_NOT_FOUND).getAllMessages());			
				return "circ.checkout.form";

			}

		}

		return "circ.checkout.form";

	}

	@RequestMapping(value = { "/checkin" }, method = RequestMethod.POST)
	public String checkin(@ModelAttribute("dto") CirculationDTO dto, BindingResult result, Model model) {

		if (result.hasErrors())
			return "circ.checkin.form";

		// given Date
		boolean damage = dto.isDamage();
		LocalDate given = dto.getGiven();
		if (given == null)
			given = LocalDate.now();

		// patron & item
		String cardKey = dto.getCardKey();
		String barcode = dto.getBarcode();

		Patron patron = this.service.getCirculationPatronByCardKey(cardKey, given);

		// for web gui
		dto.setPatron(patron);
		dto.clearHistory();
		
		Reservation r = null;

		Item item = this.service.getItemForCheckout(barcode);
		if (item != null) {
			// checkin item
			boolean aCheckout = false;
			CheckoutResult checkoutResult = null;

			try {
				
				checkoutResult = this.service.checkin(patron, item, given, damage, aCheckout);

			} catch (LibraryException ex) {

				model.addAttribute("CHECKIN_ERRORS", ex.getAllErrors());
				return "circ.checkin.form";

			}

			r = checkoutResult.getReservation();

		} else {

			Attachment attachment = this.service.getAttachmentForCheckin(barcode);
			if (attachment != null) {

				// checkin attachment
				try {

					this.service.checkinAttachment(patron, attachment, given, damage);

				} catch (LibraryException ex) {
					
					model.addAttribute("CHECKIN_ERRORS", ex.getAllErrors());
					return "circ.checkin.form";

				}

			} else {

				model.addAttribute("CHECKIN_ERRORS", new Notification(CirculationCode.ITEM_NOT_FOUND).getAllMessages());
				return "circ.checkin.form";

			}

		}

		// for web gui
		dto.setReservation(r);

		return "circ.checkin.form";

	}

	@RequestMapping(value = { "/renew" }, method = RequestMethod.POST)
	public String renew(@ModelAttribute("dto") CirculationDTO dto, BindingResult result, Model model) {

		if (result.hasErrors())
			return "circ.renew.form";

		// given Date
		LocalDate given = dto.getGiven();
		if (given == null)
			given = LocalDate.now();

		// patron & item
		String cardKey = dto.getCardKey();
		String barcode = dto.getBarcode();

		Patron patron = this.service.getCirculationPatronByCardKey(cardKey, given);

		// for web gui
		dto.setPatron(patron);
		dto.clearHistory();
		
		Item item = this.service.getItemForCheckout(barcode);
		if (item == null) {
			model.addAttribute("RENEW_ERRORS", new Notification(CirculationCode.ITEM_NOT_FOUND).getAllMessages());
			return "circ.renew.form";
		}
		
		Long bid = item.getBiblio().getId();		
		List<Reservation> reservations = this.service.findReservationsByBiblioIdAndActiveStates(bid);
		
		if(reservations.size() > 0) {
			model.addAttribute("RENEW_ERRORS", new Notification(CirculationCode.HAS_RESERVATIONS).getAllMessages());
			return "circ.renew.form";
		}
			

		try {

			this.service.renew(patron, item, given);

		} catch (LibraryException ex) {

			model.addAttribute("RENEW_ERRORS", ex.getAllErrors());
			return "circ.renew.form";

		}

		return "circ.renew.form";

	}

	@RequestMapping(value = { "/reportlost" }, method = RequestMethod.POST)
	public String reportLost(@ModelAttribute("dto") CirculationDTO dto, BindingResult result, Model model) {

		if (result.hasErrors())
			return "circ.reportlost.form";

		// given Date
		LocalDate given = dto.getGiven();
		if (given == null)
			given = LocalDate.now();

		// patron & item
		String cardKey = dto.getCardKey();
		String barcode = dto.getBarcode();

		Patron patron = this.service.getCirculationPatronByCardKey(cardKey, given);
		
		// for web gui
		dto.setPatron(patron);
		dto.clearHistory();

		Item item = this.service.getItemForCheckout(barcode);
		if (item != null) {

			try {

				this.service.reportlost(patron, item, given);

			} catch (LibraryException ex) {

				model.addAttribute("REPORTLOST_ERRORS", ex.getAllErrors());
				return "circ.reportlost.form";
			}

		} else {

			Attachment attachment = this.service.getAttachment(barcode);
			if (attachment != null) {

				try {

					this.service.reportlost(patron, attachment, given);

				} catch (LibraryException ex) {

					model.addAttribute("REPORTLOST_ERRORS", ex.getAllErrors());
					return "circ.reportlost.form";
				}

			}else {

				model.addAttribute("REPORTLOST_ERRORS", new Notification(CirculationCode.ITEM_NOT_FOUND).getAllMessages());
				return "circ.reportlost.form";

			}

		}

		return "circ.reportlost.form";

	}

	@RequestMapping(value = { "/payfine" }, method = RequestMethod.POST)
	public String payFine(@Valid @ModelAttribute("dto") CirculationDTO dto,  BindingResult result, Model model) {

		if (result.hasErrors()) {
			Patron patron = this.service.getCirculationPatronByCardKey(dto.getCardKey(), LocalDate.now());
			preparingCirculation(patron, dto, dto.getCardKey());
			return "circ.payfine.form";
		}

		// given Date
		LocalDate given = dto.getGiven();
		if (given == null)
			given = LocalDate.now();
		BigDecimal payAmount = dto.getPayAmount();
		Long[] ids = dto.getIds();

		// patron & item
		String cardKey = dto.getCardKey();

		Patron patron = this.service.getCirculationPatronByCardKey(cardKey, given);

		// for web gui
		dto.setPatron(patron);
		dto.clearHistory();
		
		try {

			this.service.payFine(patron, ids, payAmount, given);

		} catch (LibraryException ex) {

			model.addAttribute("PAYFINE_ERRORS", ex.getAllErrors());
			dto.setBarcode(null);
			return "circ.payfine.form";

		}

		return "circ.payfine.form";

	}

}
