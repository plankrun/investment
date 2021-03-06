package com.doku.investment.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.doku.investment.entities.User;

/**
 * @author Laurence
 * @see Repository
 * <p>
 * Repository For Handle User Repository
 * <p>
 * Anotation Repository is for managing database operations.
 * <p>
 * Extends CrudRepository is for have some methods for our data repository implemented, including findAll()
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
	@Query(value = "SELECT u.id FROM user u where u.username = :username and u.password = :password", nativeQuery = true)
    Integer getIdFindByUsernameAndPassword(String username, String password);
	
    User findByUsernameAndPassword(String username, String password);
    
}