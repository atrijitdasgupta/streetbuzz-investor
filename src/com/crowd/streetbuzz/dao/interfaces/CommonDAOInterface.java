/**
 * 
 */
package com.crowd.streetbuzz.dao.interfaces;

import java.util.List;

import org.hibernate.HibernateException;

/**
 * @author Atrijit
 *
 */
public interface CommonDAOInterface {
	public void addOrUpdateRecord(Object obj) throws HibernateException;

	public void deleteRecord(Object obj) throws HibernateException;

	public List getAllRecords() throws HibernateException;

	public Object getObjectById(Long id) throws HibernateException;
}
