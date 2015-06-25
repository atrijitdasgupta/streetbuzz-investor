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
import com.crowd.streetbuzz.model.Taxonomy;

/**
 * @author Atrijit
 * 
 */
public class TaxonomyDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from Taxonomy").list();
				return result;
			}

		});
	}
	
	public List getAllRecordsbyCategory(final Long categoryid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from Taxonomy t where t.categoryid=?").setLong(0, categoryid).list();
				return result;
			}

		});
	}
	
	public List getAllRecordsByRoot(final String rootword) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from Taxonomy t where t.rootword=?").setString(0, rootword).list();
				return result;
			}

		});
	}
	
	public List getAllRecordsByLink(final String linkedword) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from Taxonomy t where t.linkedword=?").setString(0, linkedword).list();
				return result;
			}

		});
	}
	
	public List getAllRecordsByRootCategory(final String rootword, final Long categoryid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from Taxonomy t where t.rootword=?").setString(0, rootword).list();
				return result;
			}

		});
	}
	
	public List getAllRecordsByLinkCategory(final String linkedword, final Long categoryid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from Taxonomy t where t.linkedword=?").setString(0, linkedword).list();
				return result;
			}

		});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (Taxonomy) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from Taxonomy t where t.id = ? ").setLong(0,
								id).uniqueResult();
					}
				});
	}

	public Object getObjectByRootLink(final String rootword,
			final String linkword) throws HibernateException {
		return (Taxonomy) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session
								.createQuery(
										"from Taxonomy t where t.rootword = ? and t.linkedword=?")
								.setString(0, rootword).setString(1, linkword)
								.uniqueResult();
					}
				});
	}
	
	public Object getObjectByRootLinkHash(final String rootword,
			final String linkword, final String hash) throws HibernateException {
		return (Taxonomy) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session
								.createQuery(
										"from Taxonomy t where t.rootword = ? and t.linkedword=? and t.hash=?")
								.setString(0, rootword).setString(1, linkword).setString(2, hash)
								.uniqueResult();
					}
				});
	}
}
