package com.crowd.streetbuzz.dao.implementation;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.crowd.streetbuzz.dao.interfaces.CommonDAOInterface;
import com.crowd.streetbuzz.model.VoiceVoteModel;

public class VoiceVoteModelDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from VoiceVoteModel").list();
				return result;
			}

		});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (VoiceVoteModel) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from VoiceVoteModel v where v.id = ? ")
								.setLong(0, id).uniqueResult();
					}
				});
	}

	public List getAllRecordsByVoiceid(final Long voiceid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from VoiceVoteModel v where v.voiceid = ?").setLong(0,
						voiceid).list();
				return result;
			}

		});
	}

	public List getAllUpRecordsByVoiceid(final Long voiceid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from VoiceVoteModel v where v.voiceid = ? and v.vote = ?")
						.setLong(0, voiceid).setInteger(1, new Integer(1))
						.list();
				return result;
			}

		});
	}

	public List getAllDownRecordsByVoiceid(final Long voiceid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from VoiceVoteModel v where v.voiceid = ? and v.vote = ?")
						.setLong(0, voiceid).setInteger(1, new Integer(0))
						.list();
				return result;
			}

		});
	}

	public List getAllRecordsByVoiceidUserid(final Long voiceid,
			final Long userid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from VoiceVoteModel v where v.voiceid = ? and v.userid = ?")
						.setLong(0, voiceid).setLong(1, userid).list();
				return result;
			}

		});
	}
}
