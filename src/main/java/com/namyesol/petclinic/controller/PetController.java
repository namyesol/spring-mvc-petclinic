package com.namyesol.petclinic.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.namyesol.petclinic.domain.Owner;
import com.namyesol.petclinic.domain.Pet;
import com.namyesol.petclinic.domain.PetType;
import com.namyesol.petclinic.service.ClinicService;

@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";
	private final ClinicService clinicService;
	
	@Autowired
	public PetController(ClinicService clinicService) {
		this.clinicService = clinicService;
	}
	
	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.clinicService.findPetTypes();
	}
	
	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable("ownerId") int ownerId) {
		return this.clinicService.findOwnerById(ownerId);
	}
	
	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.addValidators(new PetValidator());
	}
	
	@GetMapping("/pets/new")
	public String initCreationForm(@ModelAttribute Owner owner, Model model) {
		Pet pet = new Pet();
		owner.addPet(pet);
		model.addAttribute("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping("/pets/new")
	public String initCreationForm(@ModelAttribute Owner owner, @Valid @ModelAttribute Pet pet, BindingResult bindingResult, Model model) {
		
		if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null) {
			bindingResult.rejectValue("name", "duplicate", "already exists");
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		} else {
			owner.addPet(pet);
			this.clinicService.savePet(pet);
			return "redirect:/owners/{ownerId}";
		}
	}
	
	@GetMapping("/pets/{petId}/edit")
	public String initUpdateForm(@PathVariable("petId") int petId, Model model) {
		Pet pet = clinicService.findPetById(petId);
		model.addAttribute("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping("/pets/{petId}/edit")
	public String processUpdateForm(@Valid @ModelAttribute Pet pet, BindingResult bindingResult, Owner owner, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		} else {
			owner.addPet(pet);
			this.clinicService.savePet(pet);
			return "redirect:/owners/{ownerId}";
		}
	}
}
