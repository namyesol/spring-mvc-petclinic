package com.namyesol.petclinic.repository.mybatis;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import com.namyesol.petclinic.domain.Owner;
import com.namyesol.petclinic.domain.Pet;
import com.namyesol.petclinic.repository.OwnerRepository;
import com.namyesol.petclinic.repository.mybatis.mapper.OwnerMapper;
import com.namyesol.petclinic.repository.mybatis.mapper.PetMapper;

@Repository
public class MyBatisOwnerRepository implements OwnerRepository {

	private final OwnerMapper ownerMapper;
	private final PetMapper petMapper;
	
	@Autowired
	public MyBatisOwnerRepository(OwnerMapper ownerMapper, PetMapper petMapper) {
		this.ownerMapper = ownerMapper;
		this.petMapper = petMapper;
	}
	
    /**
     * Loads {@link Owner Owners} from the data store by last name, returning all owners whose last name <i>starts</i> with
     * the given name; also loads the {@link Pet Pets} and {@link Visit Visits} for the corresponding owners, if not
     * already loaded.
     */
	@Override
	public Collection<Owner> findByLastName(String lastName) {
		List<Owner> owners = ownerMapper.findOwnersByLastName(lastName);
		
		loadOwnersPetsAndVisits(owners);
		return owners;
	}

	/**
     * Loads the {@link Owner} with the supplied <code>id</code>; also loads the {@link Pet Pets} and {@link Visit Visits}
     * for the corresponding owner, if not already loaded.
     */
	@Override
	public Owner findById(int id) {
		Owner owner = ownerMapper.findOwnerById(id);
		if (owner == null) {
			throw new ObjectRetrievalFailureException(Owner.class, id);
		}
		loadPetsAndVisits(owner);
		return owner;
	}

	private void loadPetsAndVisits(final Owner owner) {
		List<Pet> pets = petMapper.findPetsByOwnerId(owner.getId());
		
		for (Pet pet : pets) {
			owner.addPet(pet);
		}
	}
	
    private void loadOwnersPetsAndVisits(List<Owner> owners) {
		for (Owner owner : owners) {
			loadPetsAndVisits(owner);
		}
	}
	

	@Override
	public void save(Owner owner) {
		if (owner.isNew()) {
			ownerMapper.insertOwner(owner);
		} else {
			ownerMapper.updateOwner(owner);
		}
	}

}
