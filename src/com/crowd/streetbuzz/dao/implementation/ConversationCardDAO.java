/**
 * 
 */
package com.crowd.streetbuzz.dao.implementation;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.interfaces.CommonDAOInterface;
import com.crowd.streetbuzz.model.ConversationCard;

/**
 * @author Atrijit
 * 
 */
public class ConversationCardDAO extends HibernateDaoSupport implements
		CommonDAOInterface, Constants {
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
				List result = session.createQuery("from ConversationCard")
						.list();
				return result;
			}

		});
	}

	public List getAllRecordsAfterId(final Long afterid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria criteria = session.createCriteria(
						ConversationCard.class).add(
						Restrictions.gt("afterid", afterid));
				return criteria.list();
			}

		});
	}

	public List getAllRecordsOfUser(final Long userid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from ConversationCard c where c.userid=?").setLong(0,
						userid).list();
				return result;
			}

		});
	}

	public List getAllRecordsOfUser(final String userid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from ConversationCard c where c.userid=?").setString(
						0, userid).list();
				return result;
			}

		});
	}

	public List getAllRecordsOfUserBySource(final Long userid,
			final String source) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.userid=? and c.source=? ORDER by c.creationdate DESC")
						.setLong(0, userid).setString(1, source).list();
				return result;
			}

		});
	}

	public List getAllPendingNewRecords() throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.actiontype=? ORDER by c.id ASC")
						.setString(0, ACTIONYES).setString(1, ACTIONTYPENEEW)
						.list();
				return result;
			}

		});
	}

	public List getAllPendingNewInterestRecords() throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action = ? and c.interestid = ?")
						.setString(0, ACTIONINTEREST).setLong(1, 0).list();
				return result;
			}

		});
	}

	public List getAllPendingEditedRecords() throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.actiontype=? ORDER by c.creationdate DESC")
						.setString(0, ACTIONYES).setString(1, ACTIONTYPEEDIT)
						.list();
				return result;
			}

		});
	}

	public List getAllReadyRecords() throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action=? ORDER by c.creationdate DESC")
						.setString(0, ACTIONNO).list();
				return result;
			}

		});
	}
	
	public List searchCards(final Long interestid, final String searchterm) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where lower(c.topic) like '%"+searchterm+"%' and c.action = ? and c.interestid = ? ORDER by c.id DESC")
						.setString(0, ACTIONNO).setLong(1,interestid).list();
				/*List result = session
				.createQuery(
						"from ConversationCard c where lower(c.topic) like '%?%' and c.action = ? and c.interestid = ? ORDER by c.id DESC")
				.setString(0,searchterm).setString(1, ACTIONNO).setLong(2,interestid).list();*/
				return result;
			}

		});
	}

	public List getAllReadyRecordsInterest(final Long interestid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.interestid =? ORDER by c.id DESC")
						.setString(0, ACTIONNO).setLong(1, interestid).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsInterestDedup(final Long interestid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.interestid =? and c.action=? ORDER by c.id DESC")
						.setLong(0, interestid).setString(1,ACTIONNO).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsPerPage(final int perpage)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action=? ORDER by c.id DESC")
						.setString(0, ACTIONNO).setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsPerPageInterest(final int perpage,
			final Long interestid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.interestid=? ORDER BY c.id DESC")
						.setString(0, ACTIONNO).setLong(1, interestid)
						.setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsPerPageBeforeId(final int perpage,
			final Long beforeId) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.id < ? ORDER by c.creationdate DESC")
						.setString(0, ACTIONNO).setLong(1, beforeId)
						.setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsPerPageBeforeIdInterest(final int perpage, final Long interestid,
			final Long beforeId)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {
	
				List result = session
				.createQuery(
						"from ConversationCard c where c.id > ? and c.action=? and c.interestid=? ORDER BY c.id ASC")
				.setLong(0,beforeId).setString(1, ACTIONNO).setLong(2, interestid).setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsPerPageAfterId(final int perpage,
			final Long afterId) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.id > ? ORDER by c.creationdate DESC")
						.setString(0, ACTIONNO).setLong(1, afterId)
						.setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsPerPageAfterIdInterest(final int perpage,final Long interestid,
			final Long afterId )
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {
			
				List result = session
				.createQuery(
						"from ConversationCard c where c.id < ? and c.action=? and c.interestid=? ORDER by c.id DESC")
				.setLong(0, afterId).setString(1, ACTIONNO).setLong(2, interestid).setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsOwnUserPerPage(final String userid,
			final int perpage) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.userid= ? ORDER by c.creationdate DESC")
						.setString(0, ACTIONNO).setString(1, userid)
						.setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsOwnUserPerPageInterest(final String userid,
			final int perpage, final Long interestid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.userid=? and c.interestid = ? ORDER by c.id DESC")
						.setString(0, ACTIONNO).setString(1, userid).setLong(2,
								interestid).setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsOtherUsersPerPageInterest(
			final String userid, final int perpage, final Long interestid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.userid!=? and c.interestid = ? ORDER by c.id DESC")
						.setString(0, ACTIONNO).setString(1, userid).setLong(2,
								interestid).setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsOwnUserPerPageBeforeId(final String userid,
			final int perpage, final Long beforeId) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.userid=? and c.id < ? ORDER by c.creationdate DESC")
						.setString(0, ACTIONNO).setString(1, userid).setLong(2,
								beforeId).setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsOwnUserPerPageBeforeIdInterest(
			final String userid, final int perpage, final Long beforeId,
			final Long interestid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				/*List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.userid=? and c.interestid=? and c.id < ? ORDER by c.id DESC")
						.setString(0, ACTIONNO).setString(1, userid).setLong(2,
								interestid).setLong(3, beforeId).setMaxResults(
								perpage).list();*/
				List result = session
				.createQuery(
						"from ConversationCard c where c.id > ? and c.action=? and c.userid=? and c.interestid=?  ORDER by c.id ASC")
				.setLong(0,beforeId).setString(1, ACTIONNO).setString(2, userid).setLong(3,
						interestid).setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsOtherUsersPerPageBeforeIdInterest(
			final String userid, final int perpage, final Long beforeId,
			final Long interestid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				/*List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.userid!=? and c.interestid=? and c.id < ? ORDER by c.id DESC")
						.setString(0, ACTIONNO).setString(1, userid).setLong(2,
								interestid).setLong(3, beforeId).setMaxResults(
								perpage).list();*/
				List result = session
				.createQuery(
						"from ConversationCard c where c.id > ? and c.action=? and c.userid!=? and c.interestid=?  ORDER by c.id ASC")
				.setLong(0,beforeId).setString(1, ACTIONNO).setString(2, userid).setLong(3,
						interestid).setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsOwnUserPerPageAfterId(final String userid,
			final int perpage, final Long afterId) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.userid=? and c.id > ? ORDER by c.creationdate DESC")
						.setString(0, ACTIONNO).setString(1, userid).setLong(2,
								afterId).setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsOwnUserPerPageAfterIdInterest(
			final String userid, final int perpage, final Long afterId,
			final Long interestid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				/*List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.userid=? and c.interestid=? and c.id > ? ORDER by c.id DESC")
						.setString(0, ACTIONNO).setString(1, userid).setLong(2,
								interestid).setLong(3, afterId).setMaxResults(
								perpage).list();*/
				List result = session
				.createQuery(
						"from ConversationCard c where c.id < ? and c.action=? and c.userid=? and c.interestid=? ORDER by c.id DESC")
				.setLong(0,afterId).setString(1, ACTIONNO).setString(2, userid).setLong(3,
						interestid).setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllReadyRecordsOtherUsersPerPageAfterIdInterest(
			final String userid, final int perpage, final Long afterId,
			final Long interestid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				/*List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.userid!=? and c.interestid=? and c.id > ? ORDER by c.id DESC")
						.setString(0, ACTIONNO).setString(1, userid).setLong(2,
								interestid).setLong(3, afterId).setMaxResults(
								perpage).list();*/
				List result = session
				.createQuery(
						"from ConversationCard c where c.id < ? and c.action=? and c.userid!=? and c.interestid=? ORDER by c.id DESC")
				.setLong(0,afterId).setString(1, ACTIONNO).setString(2, userid).setLong(3,
						interestid).setMaxResults(perpage).list();
				return result;
			}

		});
	}

	public List getAllPendingRefreshedRecords() throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from ConversationCard c where c.action=? and c.actiontype=? ORDER by c.creationdate DESC")
						.setString(0, ACTIONYES)
						.setString(1, ACTIONTYPEREFRESH).list();
				return result;
			}

		});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (ConversationCard) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from ConversationCard c where c.id = ? ")
								.setLong(0, id).uniqueResult();
					}
				});
	}

	public Object getObjectByUniqueId(final String uniqueid)
			throws HibernateException {
		return (ConversationCard) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session
								.createQuery(
										"from ConversationCard c where c.uniqueid = ? ")
								.setString(0, uniqueid).uniqueResult();
					}
				});
	}
}
