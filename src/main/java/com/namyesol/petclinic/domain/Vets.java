package com.namyesol.petclinic.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Simple domain object representing a list of veterinarians. Mostly here to be used for the 'vets' {@link
 * org.springframework.web.servlet.view.xml.MarshallingView}.
 *
 */
@XmlRootElement
public class Vets {
	
	private List<Vet> vetList;
	
	@XmlElement(name = "vet")
	public List<Vet> getVetList() {
		if (vetList == null) {
			vetList = new ArrayList<>();
		}
		return vetList;
	}
}
