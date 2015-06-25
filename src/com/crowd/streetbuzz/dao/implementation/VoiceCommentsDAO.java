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
import com.crowd.streetbuzz.model.VoiceComments;

/**
 * @author Atrijit
 * 
 */
public class VoiceCommentsDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from VoiceComments").list();
				return result;
			}

		});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (VoiceComments) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from VoiceComments v where v.id = ? ")
								.setLong(0, id).uniqueResult();
					}
				});
	}

	public List getAllRecordsbyVoiceid(final Long voicesid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from VoiceComments v where v.voicesid=? order by v.id DESC")
						.setLong(0, voicesid).list();
				return result;
			}

		});
	}

	public Object getObjectByCommentUniqueId(final String commentuniqueid)
			throws HibernateException {
		return (VoiceComments) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session
								.createQuery(
										"from VoiceComments v where v.commentuniqueid = ? ")
								.setString(0, commentuniqueid).uniqueResult();
					}
				});
	}

}
