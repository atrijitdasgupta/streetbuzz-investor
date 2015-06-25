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
import com.crowd.streetbuzz.model.CardComments;

/**
 * @author Atrijit
 * 
 */
public class CardCommentsDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from CardComments").list();
				return result;
			}

		});
	}

	public List getAllRecordsbyCardIdType(final Long cardid, final String type)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from CardComments c where c.cardid=? and c.type=?")
						.setLong(0, cardid).setString(1, type).list();
				return result;
			}

		});
	}

	public List getAllRecordsbyCarduniqueid(final String carduniqueid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from CardComments c where c.carduniqueid=?")
						.setString(0, carduniqueid).list();
				return result;
			}

		});
	}

	public List getAllRecordsbyCardid(final Long cardid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from CardComments c where c.cardid=? order by c.id DESC")
						.setLong(0, cardid).list();
				return result;
			}

		});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (CardComments) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from CardComments c where c.id = ? ").setLong(
								0, id).uniqueResult();
					}
				});
	}

	public Object getObjectByCommentUniqueId(final String commentuniqueid)
			throws HibernateException {
		return (CardComments) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session
								.createQuery(
										"from CardComments c where c.commentuniqueid = ? ")
								.setString(0, commentuniqueid).uniqueResult();
					}
				});
	}
}
