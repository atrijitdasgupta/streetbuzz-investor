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
import com.crowd.streetbuzz.model.BookMark;

/**
 * @author Atrijit
 *
 */
public class BookMarkDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from BookMark").list();
				return result;
			}

		});
	}
	
	public List getAllRecordsForUserByInterest(final Long userid, final Long interestid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from BookMark bm where bm.userid = ? and bm.interestid = ?").setLong(0,userid).setLong(1,interestid).list();
				return result;
			}

		});
	}
	
	public List getAllRecordsForUser(final Long userid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from BookMark bm where bm.userid = ?").setLong(0,userid).list();
				return result;
			}

		});
	}
	
	public Object getObjectById(final Long id) throws HibernateException {
		return (BookMark) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from BookMark bm where bm.id = ? ").setLong(0,
								id).uniqueResult();
					}
				});
	}
	
	public Object getObjectByUserIdAndCardId(final Long userid, final Long cardid) throws HibernateException {
		return (BookMark) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from BookMark bm where bm.userid = ? and bm.entityid =?").setLong(0,
										userid).setLong(1, cardid).uniqueResult();
					}
				});
	}
}
