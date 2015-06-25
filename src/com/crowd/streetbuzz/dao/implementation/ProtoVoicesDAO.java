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
import com.crowd.streetbuzz.model.ProtoVoices;

/**
 * @author Atrijit
 *
 */
public class ProtoVoicesDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from ProtoVoices").list();
				return result;
			}

		});
	}

	public List getAllRecordsbyCardId(final Long cardid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from ProtoVoices v where v.cardid=?").setLong(0, cardid)
						.list();
				return result;
			}

		});
	}

	public List getAllRecordsbyCardUniqueId(final String carduniqueid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from ProtoVoices v where v.carduniqueid=?").setString(0,
						carduniqueid).list();

				return result;
			}

		});
	}

	public List getAllRecordsbyCardUniqueIdAndSource(final String carduniqueid,
			final String source) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from ProtoVoices v where v.carduniqueid=? and v.source=?")
						.setString(0, carduniqueid).setString(1, source).list();

				return result;
			}

		});
	}

	public List getAllRecordsbyCardUniqueIdAndChannel(
			final String carduniqueid, final String channel)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from ProtoVoices v where v.carduniqueid=? and v.channel=?")
						.setString(0, carduniqueid).setString(1, channel)
						.list();

				return result;
			}

		});
	}

	public List getAllRecordsbyCardIdSource(final Long cardid,
			final String source) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from ProtoVoices v where v.cardid=? and v.source=?")
						.setLong(0, cardid).setString(1, source).list();
				return result;
			}

		});
	}

	public List getAllRecordsbyUserId(final Long userid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from ProtoVoices v where v.userid=?").setLong(0, userid)
						.list();
				return result;
			}

		});
	}

	public List getAllRecordsbyUserIdSource(final Long userid,
			final String source) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from ProtoVoices v where v.userid=? and v.source=?")
						.setLong(0, userid).setString(1, source).list();
				return result;
			}

		});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (ProtoVoices) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {

				return session.createQuery("from ProtoVoices v where v.id = ? ")
						.setLong(0, id).uniqueResult();
			}
		});
	}

	public Object getObjectByUniqueId(final String uniquevoiceid)
			throws HibernateException {
		return (ProtoVoices) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {

				return session.createQuery(
						"from ProtoVoices v where v.uniquevoiceid = ? ").setString(
						0, uniquevoiceid).uniqueResult();
			}
		});
	}
}
