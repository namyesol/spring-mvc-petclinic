package com.namyesol.petclinic.controller;

import java.util.Collection;

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

import com.namyesol.petclinic.domain.Owner;
import com.namyesol.petclinic.service.ClinicService;

@Controller
public class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
	private final ClinicService clinicService;
	
	@Autowired
	public OwnerController(ClinicService clinicService) {
		this.clinicService = clinicService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping("/owners/new")
	public String initCreationForm(Model model) {
		Owner owner = new Owner();
		model.addAttribute("owner", owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping("/owners/new")
	public String processCreationForm(@Valid @ModelAttribute Owner owner, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		} else {
			clinicService.saveOwner(owner);
			return "redirect:/owners/" + owner.getId();
		}
	}
	
	@GetMapping("/owners/find")
	public String initFindForm(Model model) {
		model.addAttribute("owner", new Owner());
		return "owners/findOwners";
	}
	
	@GetMapping("/owners")
	public String processFindForm(@ModelAttribute Owner owner, BindingResult bindingResult, Model model) {
		
		// allow parameterless GET request for /owners to return all records
		if (owner.getLastName() == null) {
			owner.setLastName(""); // empty string signifies broadest possible search
		}
		
		// find owners by last name
		Collection<Owner> results = clinicService.findOwnerByLastName(owner.getLastName());
		if (results.isEmpty()) {
			bindingResult.rejectValue("lastName", "notFound", "not found");
			return "owners/findOwners";
		} else if (results.size() == 1) {
			owner = results.iterator().next();
			return "redirect:/owners/" + owner.getId();
		} else {
			// multiple owners found
			model.addAttribute("selections", results);
			return "owners/ownersList";
		}
	}
	
	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
		Owner owner = clinicService.findOwnerById(ownerId);
		model.addAttribute(owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid @ModelAttribute Owner owner, BindingResult bindingResult, @PathVariable("ownerId") int ownerId) {
		if (bindingResult.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		} else {
			owner.setId(ownerId);
			clinicService.saveOwner(owner);
			return "redirect:/owners/{ownerId}";
		}
	}
	
	@GetMapping("/owners/{ownerId}")
	public String showOwner(@PathVariable("ownerId") int ownerId, Model model) {
		
		Owner owner = clinicService.findOwnerById(ownerId);
		model.addAttribute("owner", owner);
		return "owners/ownerDetails";
	}
}
