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
import com.crowd.streetbuzz.model.Distribution;

/**
 * @author Atrijit
 * 
 */
public class DistributionDAO extends HibernateDaoSupport implements
		CommonDAOInterface {
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

				List result = session.createQuery("from Distribution").list();
				return result;
			}

		});
	}

	public List getAllRecordsForUser(final Long destinationuserid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from Distribution d where d.destinationuserid =?")
						.setLong(0, destinationuserid).list();
				return result;
			}

		});
	}

	public List getAllNewRecordsForUser(final Long destinationuserid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from Distribution d where d.destinationuserid = ? and d.newflag = ?")
						.setLong(0, destinationuserid).setString(1, "Y").list();
				return result;
			}

		});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (Distribution) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from Distribution d where d.id = ? ").setLong(
								0, id).uniqueResult();
					}
				});
	}
}
