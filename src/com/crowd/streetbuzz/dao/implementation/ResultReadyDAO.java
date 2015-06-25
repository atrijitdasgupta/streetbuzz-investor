/**
 * 
 */
package com.crowd.streetbuzz.dao.implementation;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.interfaces.CommonDAOInterface;
import com.crowd.streetbuzz.model.ResultReady;

/**
 * @author Atrijit
 *
 */
public class ResultReadyDAO extends HibernateDaoSupport implements CommonDAOInterface, Constants{
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

				List result = session.createQuery("from ResultReady").list();
				return result;
			}

		});
	}
	
	public List getAllRecordsByCardIdUserId(final Long userid, final Long cardid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from ResultReady r where r.userid = ? and r.cardid = ?").setLong(0,userid).setLong(1, cardid).list();
				return result;
			}

		});
	}
	
	public Object getObjectById(final Long id) throws HibernateException {
		return (ResultReady) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from ResultReady r where r.id = ? ").setLong(0,
								id).uniqueResult();
					}
				});
	}
}
