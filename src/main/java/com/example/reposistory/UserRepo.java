package com.example.reposistory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.example.bean.TokenDetails;
import com.example.bean.User;

@Repository
@Transactional
public class UserRepo {

	@PersistenceContext
	private EntityManager entityManager;

	public void saveUser(User user) {
		entityManager.persist(user);
	}

	public User getUser(Long val) {
		return entityManager.find(User.class, val);

	}

	public User findByUsername(String username) {
		Query query = this.entityManager.createQuery("FROM User UR where UR.email =:email");
		query.setParameter("email", username);
		return (User) query.getSingleResult();
	}

	public void saveToken(TokenDetails details) {
		this.entityManager.persist(details);

	}
}
