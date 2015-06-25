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
import com.crowd.streetbuzz.model.UserDuplicateCardMap;

/**
 * @author Atrijit
 *
 */
public class UserDuplicateCardMapDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from UserDuplicateCardMap").list();
				return result;
			}

		});
	}
	
	public List getAllRecordsforUserByInterest(final Long userid, final Long interestid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery("from UserDuplicateCardMap u where u.userid = ? and u.interestid =?").setLong(0,userid).setLong(1,interestid).list();
				return result;
			}

		});
	}
	
	public Object getObjectById(final Long id) throws HibernateException {
		return (UserDuplicateCardMap) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from UserDuplicateCardMap u where u.id = ? ")
								.setLong(0, id).uniqueResult();
					}
				});
	}
	
	public Object getObjectByUserIdCardIdInterestId(final Long userid, final Long cardid, final Long interestid) throws HibernateException {
		return (UserDuplicateCardMap) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from UserDuplicateCardMap u where u.userid = ? and u.cardid = ? and u.interestid =?")
								.setLong(0, userid).setLong(1,cardid).setLong(2, interestid).uniqueResult();
					}
				});
	}
}
