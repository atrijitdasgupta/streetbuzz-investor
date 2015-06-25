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
import com.crowd.streetbuzz.model.CardInterestDetectedMap;

/**
 * @author Atrijit
 *
 */
public class CardInterestDetectedMapDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from CardInterestDetectedMap").list();
				return result;
			}

		});
	}
	
	public List getAllRecordsByCardIdUnProcessed(final Long cardid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from CardInterestDetectedMap cm where cm.cardid=? and cm.processed = ?").setLong(0, cardid).setLong(1,0).list();
				return result;
			}

		});
	}
	
	public List getAllRecordsByCardId(final Long cardid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from CardInterestDetectedMap cm where cm.cardid=?").setLong(0, cardid).list();
				return result;
			}

		});
	}
	
	public Object getObjectById(final Long id) throws HibernateException {
		return (CardInterestDetectedMap) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from CardInterestDetectedMap cm where cm.id = ? ").setLong(0,
								id).uniqueResult();
					}
				});
	}
}
