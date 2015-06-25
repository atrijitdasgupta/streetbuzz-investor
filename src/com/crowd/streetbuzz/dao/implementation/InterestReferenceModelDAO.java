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
import com.crowd.streetbuzz.model.InterestReferenceModel;

/**
 * @author Atrijit
 *
 */
public class InterestReferenceModelDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from InterestReferenceModel").list();
				return result;
			}

		});
	}
	
	public List getAllRecordsByIntParentIntIds(final Long interestid, final Long parentinterestid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from InterestReferenceModel i where i.interestid = ? and i.parentinterestid =?").setLong(0, interestid).setLong(1, parentinterestid).list();
				return result;
			}

		});
	}
	
	public List getAllRecordsByInterestIds(final Long interestid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from InterestReferenceModel i where i.interestid = ?").setLong(0, interestid).list();
				return result;
			}

		});
	}
	
	public Object getObjectById(final Long id) throws HibernateException {
		return (InterestReferenceModel) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from InterestReferenceModel i where i.id = ? ").setLong(0,
								id).uniqueResult();
					}
				});
	}

}
