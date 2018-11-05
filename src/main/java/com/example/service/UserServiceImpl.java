package com.example.service;

import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import com.example.model.User;
import com.example.persistence.HibernateUtil;

@Service("userService")
public class UserServiceImpl implements UserService {

	public List<User> findAllUsers() {
		return getAllUsers();
	}

	public User findById(long id) {
		for (User user : getAllUsers()) {
			if (user.getId() == id) {
				return user;
			}
		}
		return null;
	}

	public User findByName(String name) {
		for (User user : getAllUsers()) {
			if (user.getUsername().equalsIgnoreCase(name)) {
				return user;
			}
		}
		return null;
	}

	public void saveOrUpdateUser(User user) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.getTransaction();
			transaction.begin();
			session.saveOrUpdate(user);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

	public void deleteUserById(long id) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.getTransaction();
			transaction.begin();
			User user = findById(id);
			if (user != null) {
				session.delete(user);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}
	}

	public boolean isUserExist(User user) {
		return findByName(user.getUsername()) != null;
	}

	public void deleteAllUsers() {

		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.getTransaction();
			transaction.begin();
			List<User> userList = getAllUsers();
			if (!userList.isEmpty()) {
				for(User user : userList) {
					session.delete(user);
				}
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		}

	}

	private List<User> getAllUsers() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			CriteriaQuery<User> cq = session.getCriteriaBuilder().createQuery(User.class);
			cq.from(User.class);
			List<User> userList = session.createQuery(cq).getResultList();
			transaction.commit();
			return userList;
		} catch (HibernateException e) {
		}
		return null;
	}
}
