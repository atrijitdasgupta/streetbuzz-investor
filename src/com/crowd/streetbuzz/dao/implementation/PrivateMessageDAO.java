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
import com.crowd.streetbuzz.model.PrivateMessage;

/**
 * @author Atrijit
 * 
 */
public class PrivateMessageDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from PrivateMessage").list();
				return result;
			}

		});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (PrivateMessage) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from PrivateMessage pm where pm.id = ? ")
								.setLong(0, id).uniqueResult();
					}
				});
	}

	public Object getObjectByUniqueId(final String uniqueid)
			throws HibernateException {
		return (PrivateMessage) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session
								.createQuery(
										"from PrivateMessage pm where pm.uniqueid = ? ")
								.setString(0, uniqueid).uniqueResult();
					}
				});
	}

	public List getAllRecordsbyUserId(final Long messagebyid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from PrivateMessage pm where pm.messagebyuserid=? ORDER BY pm.messageat DESC")
						.setLong(0, messagebyid).list();

				return result;
			}

		});
	}

	public List getAllOriginalRecords(final Long messagebyid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from PrivateMessage pm where pm.isreply =? and pm.messagebyuserid=? or pm.messagetouserid =? ORDER BY pm.messageat DESC")
						.setString(0, "N").setLong(1, messagebyid).setLong(2,
								messagebyid).list();

				return result;
			}

		});
	}

	public List getAllOriginalRecordsPerPage(final Long messagebyid,
			final int perpage) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from PrivateMessage pm where pm.isreply =? and pm.messagebyuserid=? or pm.messagetouserid =? ORDER BY pm.id DESC")
						.setString(0, "N").setLong(1, messagebyid).setLong(2,
								messagebyid).setMaxResults(perpage).list();

				return result;
			}

		});
	}
	
	public List getAllOriginalRecordsPerPageBeforeId(final Long messagebyid,
			final int perpage, final Long beforeId) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from PrivateMessage pm where pm.isreply =? and pm.messagebyuserid=? or pm.messagetouserid =? and pm.id < ? ORDER BY pm.id DESC")
						.setString(0, "N").setLong(1, messagebyid).setLong(2,
								messagebyid).setLong(3, beforeId).setMaxResults(perpage).list();

				return result;
			}

		});
	}
	
	public List getAllOriginalRecordsPerPageAfterId(final Long messagebyid,
			final int perpage, final Long afterId) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from PrivateMessage pm where pm.isreply =? and pm.messagebyuserid=? or pm.messagetouserid =? and pm.id > ? ORDER BY pm.id DESC")
						.setString(0, "N").setLong(1, messagebyid).setLong(2,
								messagebyid).setLong(3, afterId).setMaxResults(perpage).list();

				return result;
			}

		});
	}

	public List getAllOriginalRecordsbyUserId(final Long messagebyid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from PrivateMessage pm where pm.messagebyuserid=? and pm.isreply =? ORDER BY pm.messageat DESC")
						.setLong(0, messagebyid).setString(1, "N").list();

				return result;
			}

		});
	}

	public List getAllRecordsforUserId(final Long messagetouserid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from PrivateMessage pm where pm.messagetouserid=? ORDER BY pm.messageat DESC")
						.setLong(0, messagetouserid).list();

				return result;
			}

		});
	}

	public List getAllOriginalRecordsforUserId(final Long messagetouserid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from PrivateMessage pm where pm.messagetouserid=?  and pm.isreply =? ORDER BY pm.messageat DESC")
						.setLong(0, messagetouserid).setString(1, "N").list();

				return result;
			}

		});
	}

	public List getAllRecordsbyUserIdPerPage(final Long messagebyid,
			final int perpage) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from PrivateMessage pm where pm.messagebyuserid=? ORDER BY pm.messageat DESC")
						.setLong(0, messagebyid).setMaxResults(perpage).list();

				return result;
			}

		});
	}

	public List getAllRecordsbyUserIdPerPageBeforeId(final Long messagebyid,
			final int perpage, final Long beforeId) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from PrivateMessage pm where pm.messagebyuserid=? and pm.id < ? ORDER BY pm.messageat DESC")
						.setLong(0, messagebyid).setLong(1, beforeId)
						.setMaxResults(perpage).list();

				return result;
			}

		});
	}

	public List getAllRecordsbyUserIdPerPageAfterId(final Long messagebyid,
			final int perpage, final Long afterId) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from PrivateMessage pm where pm.messagebyuserid=? and pm.id > ? ORDER BY pm.messageat DESC")
						.setLong(0, messagebyid).setLong(1, afterId)
						.setMaxResults(perpage).list();

				return result;
			}

		});
	}

	public List getAllRecordsbyReplyToMessageId(final Long replytomessageid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				/*List result = session
						.createQuery(
								"from PrivateMessage pm where pm.replytomessageid=? ORDER BY pm.messageat DESC")
						.setLong(0, replytomessageid).list();*/
				List result = session
				.createQuery(
						"from PrivateMessage pm where pm.replytomessageid=? ORDER BY pm.id ASC")
				.setLong(0, replytomessageid).list();
				return result;
			}

		});
	}
}
