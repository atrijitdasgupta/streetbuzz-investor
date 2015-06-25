/**
 * 
 */
package com.crowd.streetbuzz.dao.implementation;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.interfaces.CommonDAOInterface;
import com.crowd.streetbuzz.model.CardVoteModel;

/**
 * @author Atrijit
 * 
 */
public class CardVoteModelDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from CardVoteModel").list();
				return result;
			}

		});
	}

	public List getAllRecordsByCardid(final Long cardid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from CardVoteModel cvm where cvm.cardid = ?").setLong(
						0, cardid).list();
				return result;
			}

		});
	}

	public List getAllUpRecordsByCardid(final Long cardid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from CardVoteModel cvm where cvm.cardid = ? and cvm.vote = ?")
						.setLong(0, cardid).setInteger(1, new Integer(1))
						.list();
				return result;
			}

		});
	}

	public List getAllUpTypeRecordsByCardid(final Long cardid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from CardVoteModel cvm where cvm.cardid = ? and cvm.type = ?")
						.setLong(0, cardid).setString(1, UPVOTE).list();
				return result;
			}

		});
	}

	public List getAllDownRecordsByCardid(final Long cardid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from CardVoteModel cvm where cvm.cardid = ? and cvm.vote = ?")
						.setLong(0, cardid).setInteger(1, new Integer(0))
						.list();
				return result;
			}

		});
	}

	public List getAllDownTypeRecordsByCardid(final Long cardid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from CardVoteModel cvm where cvm.cardid = ? and cvm.type = ?")
						.setLong(0, cardid).setString(1, DOWNVOTE).list();
				return result;
			}

		});
	}

	public List getAllRecordsByCardidUserid(final Long cardid, final Long userid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from CardVoteModel cvm where cvm.cardid = ? and cvm.userid = ?")
						.setLong(0, cardid).setLong(1, userid).list();
				return result;
			}

		});
	}

	public Object getObjectByUserIdCardIdType(final Long userid,
			final Long cardid, final String type) throws HibernateException {
		return (CardVoteModel) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session
								.createQuery(
										"from CardVoteModel c where c.cardid = ? and c.userid = ? and c.type = ?")
								.setLong(0, cardid).setLong(1, userid)
								.setString(2, type).uniqueResult();
					}
				});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (CardVoteModel) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from CardVoteModel c where c.id = ? ")
								.setLong(0, id).uniqueResult();
					}
				});
	}
}
