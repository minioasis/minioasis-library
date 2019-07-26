package org.minioasis.library.controller;

import java.time.LocalDate;

import org.minioasis.library.domain.Attachment;
import org.minioasis.library.domain.AttachmentCheckout;
import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.CheckoutResult;
import org.minioasis.library.domain.CirculationCode;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.search.CirculationDTO;
import org.minioasis.library.exception.LibraryException;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/circ")
public class CheckinController {

	@Autowired
	private LibraryService service;

	@RequestMapping(value = { "/return" }, method = RequestMethod.GET)
	public String checkin(Model model) {

		CirculationDTO dto = new CirculationDTO();
		model.addAttribute("dto", dto);

		return "circ.return.form";
	}

	@RequestMapping(value = { "/return" }, method = RequestMethod.POST)
	public String checkin(@ModelAttribute("dto") CirculationDTO dto, BindingResult result, Model model) {

		if (result.hasErrors())
			return "circ.return.form";

		// given Date
		LocalDate now = LocalDate.now();
		boolean damage = dto.isDamage();
		LocalDate given = dto.getGiven();
		if (given == null)
			given = now;

		String barcode = dto.getBarcode();

		CheckoutResult checkoutResult = null;

		// Return Item
		Item item = this.service.getItemForCheckin(barcode);
		
		Reservation r = null;

		if (item != null) {

			try {

				checkoutResult = this.service.returnItem(item, given, damage);

			} catch (LibraryException ex) {

				model.addAttribute("RETURN_ERRORS", ex.getAllErrors());
				
				return "circ.return.form";
			}

			r = checkoutResult.getReservation();

			Checkout c = checkoutResult.getCheckout();
			Long cid = c.getId();

			if (r != null) {
				Long rid = r.getId();
				return "redirect:/admin/circ/return/item/" + cid + "/" + rid;
			}

			return "redirect:/admin/circ/return/item/" + cid;

		}

		// Or Return Attachment
		Attachment attachment = this.service.getAttachmentForCheckin(barcode);

		if (attachment != null) {

			try {

				checkoutResult = this.service.returnAttachment(attachment, given, damage);

			} catch (LibraryException ex) {

				model.addAttribute("RETURN_ERRORS", ex.getAllErrors());
				return "circ.return.form";
			}

			r = checkoutResult.getReservation();

			this.service.save(attachment);

			AttachmentCheckout c = checkoutResult.getAttachmentCheckout();
			Long acid = c.getId();

			if (r != null) {

				Long rid = r.getId();
				return "redirect:/admin/circ/return/attachment/" + acid + "/" + rid;
			}

			return "redirect:/admin/circ/return/attachment/" + acid;

		}

		model.addAttribute("RETURN_ERRORS", CirculationCode.ITEM_NOT_FOUND);
		model.addAttribute("dto", new CirculationDTO());

		return "circ.return.form";

	}

	// Item
	@RequestMapping(value = { "/return/item/{cid}" }, method = RequestMethod.GET)
	public String checkin(@PathVariable("cid") long cid, Model model) {

		Checkout icheckout = this.service.getCheckout(cid);
		model.addAttribute("icheckout", icheckout);

		CirculationDTO dto = new CirculationDTO();
		model.addAttribute("dto", dto);

		return "circ.return.form";
	}

	@RequestMapping(value = { "/return/item/{cid}/{rid}" }, method = RequestMethod.GET)
	public String checkin(@PathVariable("cid") long cid, @PathVariable("rid") long rid, Model model) {

		Checkout icheckout = this.service.getCheckout(cid);
		model.addAttribute("icheckout", icheckout);

		Reservation r = this.service.getReservation(rid);
		model.addAttribute("reservation", r);

		CirculationDTO dto = new CirculationDTO();
		model.addAttribute("dto", dto);

		return "circ.return.form";
	}

	// Attachment
	@RequestMapping(value = { "/return/attachment/{acid}" }, method = RequestMethod.GET)
	public String checkinAttachment(@PathVariable("acid") long acid, Model model) {

		AttachmentCheckout acheckout = this.service.getAttachmentCheckout(acid);
		model.addAttribute("acheckout", acheckout);

		CirculationDTO dto = new CirculationDTO();
		
		model.addAttribute("dto", dto);

		return "circ.return.form";
	}

	@RequestMapping(value = { "/return/attachment/{acid}/{rid}" }, method = RequestMethod.GET)
	public String checkinAttachment(@PathVariable("acid") long acid, @PathVariable("rid") long rid, Model model) {

		AttachmentCheckout acheckout = this.service.getAttachmentCheckout(acid);
		model.addAttribute("acheckout", acheckout);

		Reservation r = this.service.getReservation(rid);
		model.addAttribute("reservation", r);

		CirculationDTO dto = new CirculationDTO();
		
		model.addAttribute("dto", dto);

		return "circ.return.form";
	}

}
