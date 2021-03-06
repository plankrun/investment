package com.doku.investment.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.doku.investment.entities.Property;

/**
 * @author Laurence
 * @see Repository
 * <p>
 * Repository For Handle Property Repository
 * <p>
 * Anotation Repository is for managing database operations.
 * <p>
 * Extends CrudRepository is for have some methods for our data repository implemented, including findAll()
 */
@Repository
public interface PropertyRepository extends CrudRepository<Property, Long> {
	
	List<Property> findAll();
}