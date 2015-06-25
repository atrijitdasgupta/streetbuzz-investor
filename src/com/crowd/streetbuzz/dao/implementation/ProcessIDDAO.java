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
import com.crowd.streetbuzz.model.ProcessID;

/**
 * @author Atrijit
 * 
 */
public class ProcessIDDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from ProcessID").list();
				return result;
			}

		});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (ProcessID) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from ProcessID a where a.id = ? ").setLong(0,
								id).uniqueResult();
					}
				});
	}

	public Object getObjectByProcessId(final String processid)
			throws HibernateException {
		return (ProcessID) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from ProcessID a where a.processid = ? ")
								.setString(0, processid).uniqueResult();
					}
				});
	}
}
