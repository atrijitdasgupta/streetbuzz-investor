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
import com.crowd.streetbuzz.model.CardCommentVoteModel;

/**
 * @author Atrijit
 * 
 */
public class CardCommentVoteModelDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from CardCommentVoteModel")
						.list();
				return result;
			}

		});
	}

	public List getAllRecordsByCommentId(final Long commentid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from CardCommentVoteModel c where c.commentid = ?")
						.setLong(0, commentid).list();
				return result;
			}

		});
	}

	public List getAllUpRecordsByCommentId(final Long commentid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from CardCommentVoteModel ccvm where ccvm.commentid = ? and ccvm.vote = ?")
						.setLong(0, commentid).setInteger(1, new Integer(1))
						.list();
				return result;
			}

		});
	}

	public List getAllDownRecordsByCommentId(final Long commentid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from CardCommentVoteModel ccvm where ccvm.commentid = ? and ccvm.vote = ?")
						.setLong(0, commentid).setInteger(1, new Integer(0))
						.list();
				return result;
			}

		});
	}

	public List getAllRecordsByCommentidUserid(final Long commentid,
			final Long userid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from CardCommentVoteModel cvm where cvm.commentid = ? and cvm.userid = ?")
						.setLong(0, commentid).setLong(1, userid).list();
				return result;
			}

		});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (CardCommentVoteModel) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from CardCommentVoteModel c where c.id = ? ")
								.setLong(0, id).uniqueResult();
					}
				});
	}
}
