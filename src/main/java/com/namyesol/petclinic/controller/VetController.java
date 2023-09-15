package com.namyesol.petclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.namyesol.petclinic.domain.Vets;
import com.namyesol.petclinic.service.ClinicService;

@Controller
public class VetController {

	private final ClinicService clinicService;
	
	@Autowired
	public VetController(ClinicService clinicService) {
		this.clinicService = clinicService;
	}
	
	@GetMapping("/vets")
	public String showVetList(Model model) {
		Vets vets = getVets();
		model.addAttribute("vets", vets);
		return "vets/vetList";
	}
	
	@GetMapping(value= "/vets.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Vets showJsonVetList() {
		return getVets();
	}
	
	@GetMapping(value = "/vets.xml", produces = MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	public Vets showXmlVetList() {
		return getVets();
	}
	
	private Vets getVets() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet objects
        // so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(clinicService.findVets());
		return vets;
	}
}
