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
import com.crowd.streetbuzz.model.CategoryMaster;

/**
 * @author Atrijit
 *
 */
public class CategoryMasterDAO extends HibernateDaoSupport implements CommonDAOInterface{
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

				List result = session.createQuery("from CategoryMaster").list();
				return result;
			}

		});
	}
	
	public List getAllRecordsByParentId(final Long parentid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from CategoryMaster cm where cm.parentid = ?").setLong(0,parentid).list();
				return result;
			}

		});
	}
	
	public List getAllPeerMasterRecordsByParentId(final Long interestid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from CategoryMaster cm where cm.parentid = ? or cm.id= ? ").setLong(0,interestid).setLong(1, interestid).list();
				return result;
			}

		});
	}
	
	public Object getObjectById(final Long id) throws HibernateException {
		return (CategoryMaster) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from CategoryMaster c where c.id = ? ").setLong(0,
								id).uniqueResult();
					}
				});
	}
	
	public Object getObjectByCategory(final String categoryname) throws HibernateException {
		return (CategoryMaster) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from CategoryMaster c where c.categoryname = ? ").setString(0,
										categoryname).uniqueResult();
					}
				});
	}
}
