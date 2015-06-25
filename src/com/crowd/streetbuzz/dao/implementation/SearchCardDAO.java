/**
 * 
 */
package com.crowd.streetbuzz.dao.implementation;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.interfaces.CommonDAOInterface;
import com.crowd.streetbuzz.model.SearchCard;

/**
 * @author Atrijit
 *
 */
public class SearchCardDAO extends HibernateDaoSupport implements CommonDAOInterface, Constants{
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

				List result = session.createQuery("from SearchCard").list();
				return result;
			}

		});
	}
	
	public List getAllRecordsOfUser(final Long userid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from SearchCard s where s.userid=?").setLong(0, userid).list();
				return result;
			}

		});
	}
	
	public List getAllRecordsOfUserBySource(final Long userid, final String source) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from SearchCard s where s.userid=? and s.source=?").setLong(0, userid).setString(1, source).list();
				return result;
			}

		});
	}
	
	public List getAllRecordsAfterId(final Long afterid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria criteria = session.createCriteria(SearchCard.class).add(Restrictions.gt("afterid",afterid));
				return criteria.list();
			}

		});
	}
	
	public List getAllPendingNewRecords() throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from SearchCard s where s.action=? and s.actiontype=?").setString(0,ACTIONYES).setString(1, ACTIONTYPENEEW).list();
				return result;
			}

		});
	}
	
	public List getAllPendingEditedRecords() throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from SearchCard s where s.action=? and s.actiontype=?").setString(0,ACTIONYES).setString(1, ACTIONTYPEEDIT).list();
				return result;
			}

		});
	}
	
	public List getAllReadyRecords() throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from SearchCard s where s.action=?").setString(0,ACTIONNO).list();
				return result;
			}

		});
	}
	
	public List getAllPendingRefreshedRecords() throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from SearchCard s where s.action=? and s.actiontype=?").setString(0,ACTIONYES).setString(1, ACTIONTYPEREFRESH).list();
				return result;
			}

		});
	}
	
	public Object getObjectById(final Long id) throws HibernateException {
		return (SearchCard) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from SearchCard s where s.id = ? ").setLong(0,
								id).uniqueResult();
					}
				});
	}
	
	public Object getObjectByUniqueId(final String uniqueid) throws HibernateException {
		return (SearchCard) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from SearchCard s where s.uniqueid = ? ").setString(0,
										uniqueid).uniqueResult();
					}
				});
	}
}
