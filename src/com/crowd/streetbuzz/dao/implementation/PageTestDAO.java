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
import com.crowd.streetbuzz.model.PageTest;

/**
 * @author Atrijit
 * 
 */
public class PageTestDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from PageTest").list();
				return result;
			}

		});
	}

	public List getAllRecordsPerPage(final int perpage, final Long category)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from PageTest p where p.category = ? ORDER BY p.id DESC").setLong(0,category).setMaxResults(
						perpage).list();
				return result;
			}

		});
	}

	public List getAllRecordsPerPageBeforeId(final int perpage,
			final Long beforeid, final Long category) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from PageTest p WHERE p.id > ? and p.category = ? ORDER BY p.id ASC")
						.setLong(0, beforeid).setLong(1,category).setMaxResults(perpage).list();

				return result;
			}

		});
	}

	public List getAllRecordsPerPageAfterId(final int perpage,
			final Long afterid, final Long category) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from PageTest p WHERE p.id < ? and p.category = ? ORDER BY p.id DESC")
						.setLong(0, afterid).setLong(1,category).setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (PageTest) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from PageTest p where p.id = ? ").setLong(0,
								id).uniqueResult();
					}
				});
	}
}
