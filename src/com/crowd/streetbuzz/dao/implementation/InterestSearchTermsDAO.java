/**
 * 
 */
package com.crowd.streetbuzz.dao.implementation;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.crowd.streetbuzz.dao.interfaces.CommonDAOInterface;
import com.crowd.streetbuzz.model.InterestSearchTerms;

/**
 * @author Atrijit
 *
 */
public class InterestSearchTermsDAO extends HibernateDaoSupport implements
CommonDAOInterface{
	public void addOrUpdateRecord(Object obj) throws HibernateException {
		getHibernateTemplate().saveOrUpdate(obj);
	}

	public void deleteRecord(Object obj) throws HibernateException {
		getHibernateTemplate().delete(obj);
	}
	public List getAllRecords() throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from InterestSearchTerms").list();
				return result;
			}

		});
	}
	public List getAllRecordsPending() throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from InterestSearchTerms i where i.status = ?").setString(0,"PENDING").list();
				return result;
			}

		});
	}
	
	public List getAllRecordsCompleted() throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from InterestSearchTerms i where i.status = ?").setString(0,"DONE").list();
				return result;
			}

		});
	}
	public Object getObjectById(final Long id) throws HibernateException {
		return (InterestSearchTerms) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from InterestSearchTerms i where i.id = ? ").setLong(0,
								id).uniqueResult();
					}
				});
	}
}
