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
import com.crowd.streetbuzz.model.CardExclusion;

/**
 * @author Atrijit
 * 
 */
public class CardExclusionDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from CardExclusion").list();
				return result;
			}

		});
	}

	public List getAllRecordsByUserId(final Long userid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from CardExclusion c where c.userid = ?").setLong(0,
						userid).list();
				return result;
			}

		});
	}

	public List getAllRecordsByUserIdCardId(final Long userid, final Long cardid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from CardExclusion c where c.userid = ? and c.cardid = ?")
						.setLong(0, userid).setLong(1, cardid).list();
				return result;
			}

		});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (CardExclusion) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from CardExclusion c where c.id = ? ")
								.setLong(0, id).uniqueResult();
					}
				});
	}
}
