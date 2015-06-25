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
import com.crowd.streetbuzz.model.Voices;

/**
 * @author Atrijit
 * 
 */
public class VoicesDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from Voices").list();
				return result;
			}

		});
	}

	public List doSearch(final String searchterm, final String channel,
			final Long cardid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from Voices v where lower(v.topic) like '%"
								+ searchterm
								+ "%' and v.cardid=? and v.channel=? ")
						.setLong(0, cardid).setString(1, channel).list();
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
						"from Voices v where v.cardid=?").setLong(0, cardid)
						.list();
				return result;
			}

		});
	}

	public List getAllNNRecordsbyCardId(final Long cardid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from Voices v where v.cardid=? and v.sentimentrating is not null and v.voicesdate is not null").setLong(0, cardid)
						.list();
				return result;
			}

		});
	}

	public List getAllRecordsbyCardIdChannel(final Long cardid,
			final String channel) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from Voices v where v.cardid=? and v.channel=?")
						.setLong(0, cardid).setString(1, channel).list();
				return result;
			}

		});
	}

	public List getAllRecordsbyCardIdPerpage(final Long cardid,
			final int perpage) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from Voices v where v.cardid=?").setLong(0, cardid)
						.setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllRecordsbyCardIdPerpageBeforeId(final Long cardid,
			final int perpage, final Long beforeId) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from Voices v where v.cardid=? and v.id < ?").setLong(
						0, cardid).setLong(1, beforeId).setMaxResults(perpage)
						.list();
				return result;
			}

		});
	}

	public List getAllRecordsbyCardIdPerpageAfterId(final Long cardid,
			final int perpage, final Long afterId) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from Voices v where v.cardid=? and v.id > ?").setLong(
						0, cardid).setLong(1, afterId).setMaxResults(perpage)
						.list();
				return result;
			}

		});
	}

	//
	public List getAllRecordsbyCardIdPerpageChannel(final Long cardid,
			final int perpage, final String channel) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from Voices v where v.cardid=? and v.channel=? ORDER BY v.id DESC")
						.setLong(0, cardid).setString(1, channel)
						.setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllRecordsbyCardIdPerpageBeforeIdChannel(final Long cardid,
			final int perpage, final Long beforeId, final String channel)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from Voices v where v.id > ? and v.cardid=?  and v.channel=? ORDER BY v.id ASC")
						.setLong(0, beforeId).setLong(1, cardid).setString(2,
								channel).setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllRecordsbyCardIdPerpageAfterIdChannel(final Long cardid,
			final int perpage, final Long afterId, final String channel)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from Voices v where v.id < ? and v.cardid=? and v.channel=? ORDER BY v.id DESC")
						.setLong(0, afterId).setLong(1, cardid).setString(2,
								channel).setMaxResults(perpage).list();
				return result;
			}

		});
	}

	//

	public List getAllRecordsbyCardUniqueId(final String carduniqueid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from Voices v where v.carduniqueid=?").setString(0,
						carduniqueid).list();

				return result;
			}

		});
	}

	/*
	 * public List getAllRecordsbyCardId(final Long cardid) throws
	 * HibernateException { return (List) getHibernateTemplate().execute(new
	 * HibernateCallback() {
	 * 
	 * public Object doInHibernate(Session session) throws HibernateException {
	 * 
	 * List result = session.createQuery( "from Voices v where
	 * v.carduniqueid=?").setString(0, carduniqueid).list();
	 * 
	 * return result; }
	 * 
	 * }); }
	 */

	public List getAllRecordsbyCardUniqueIdAndSource(final String carduniqueid,
			final String channel) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from Voices v where v.carduniqueid=? and v.channel=?")
						.setString(0, carduniqueid).setString(1, channel)
						.list();

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
						"from Voices v where v.carduniqueid=? and v.channel=?")
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
						"from Voices v where v.cardid=? and v.source=?")
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
						"from Voices v where v.userid=?").setLong(0, userid)
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
						"from Voices v where v.userid=? and v.source=?")
						.setLong(0, userid).setString(1, source).list();
				return result;
			}

		});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (Voices) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {

				return session.createQuery("from Voices v where v.id = ? ")
						.setLong(0, id).uniqueResult();
			}
		});
	}

	public Object getObjectByUniqueId(final String uniquevoiceid)
			throws HibernateException {
		return (Voices) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {

				return session.createQuery(
						"from Voices v where v.uniquevoiceid = ? ").setString(
						0, uniquevoiceid).uniqueResult();
			}
		});
	}
}
