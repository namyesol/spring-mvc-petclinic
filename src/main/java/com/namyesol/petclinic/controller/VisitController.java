package com.namyesol.petclinic.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.namyesol.petclinic.domain.Pet;
import com.namyesol.petclinic.domain.Visit;
import com.namyesol.petclinic.service.ClinicService;

@Controller
public class VisitController {

	private final ClinicService clinicService;
	
	@Autowired
	public VisitController(ClinicService clinicService) {
		this.clinicService = clinicService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	/**
	 * Called before each and every @GetMapping or @PostMapping annotated method.
	 * 2 goals:
	 * - Make sure we always have fresh data
	 *  - Since we do not use the session scope, make sure that Pet object always has an id
	 *  (Even though id is not part of the form fields)
	 *  
	 * @param petId
	 * @return pet
	 */
	@ModelAttribute("visit")
	public Visit loadPetWithVisit(@PathVariable("petId") int petId) {
		Pet pet = this.clinicService.findPetById(petId);
		Visit visit = new Visit();
		pet.addVisit(visit);
		return visit;
	}
	
	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
	@GetMapping("/owners/*/pets/{petId}/visits/new")
	public String initNewVisitForm(@PathVariable("petId") int petId, Model model) {
		return "pets/createOrUpdateVisitForm";
	}
	
	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
	@PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	public String processNewVisitForm(@Valid Visit visit, BindingResult bindingResult) {
		if (bindingResult.hasErrors() ) {
			return "pets/createOrUpdateVisitForm";
		} else {
			this.clinicService.saveVisit(visit);
			return "redirect:/owners/{ownerId}";
		}
	}
	
	@GetMapping("/owners/*/pets/{petId}/visits")
	public String showVisits(@PathVariable int petId, Model model) {
		List<Visit> visits = this.clinicService.findPetById(petId).getVisits();
		model.addAttribute("visits", visits);
		return "visitList";
	}
}
