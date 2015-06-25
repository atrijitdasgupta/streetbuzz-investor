/**
 * 
 */
package com.crowd.streetbuzz.dao.implementation;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.crowd.streetbuzz.dao.interfaces.CommonDAOInterface;
import com.crowd.streetbuzz.model.VoicesDetails;

/**
 * @author Atrijit
 * 
 */
public class VoicesDetailsDAO extends HibernateDaoSupport implements
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

				List result = session.createQuery("from VoicesDetails").list();
				return result;
			}

		});
	}

	public List getAllRecordsbyVoices(final Long voicesid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from VoicesDetails v where v.voicesid=?").setLong(0,
						voicesid).list();
				return result;
			}

		});
	}

	public List getAllNNRecordsbyVoices(final Long voicesid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session
						.createQuery(
								"from VoicesDetails v where v.voicesid=? and v.voicedate is not null and v.sentimentrating is not null")
						.setLong(0, voicesid).setMaxResults(10).list();
				return result;
			}

		});
	}

	public List getAllRecordsbyCardid(final Long cardid)
			throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				List result = session.createQuery(
						"from VoicesDetails v where v.cardid=?").setLong(0,
						cardid).list();
				return result;
			}

		});
	}
	
	public List getAllNNRecordsbyCardid(final Long cardid)
	throws HibernateException {
return (List) getHibernateTemplate().execute(new HibernateCallback() {

	public Object doInHibernate(Session session)
			throws HibernateException {

		List result = session.createQuery(
				"from VoicesDetails v where v.cardid=? and v.voicedate is not null and v.sentimentrating is not null").setLong(0,
				cardid).list();
		return result;
	}

});
}

	public List getAllRecordsbyVoicesAfterId(final Long voicesid,
			final Long afterid) throws HibernateException {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException {

				Criteria criteria = session.createCriteria(VoicesDetails.class)
						.add(Restrictions.eq("voicesid", voicesid)).add(
								Restrictions.gt("id", afterid));
				return criteria.list();
			}

		});
	}

	public Object getObjectById(final Long id) throws HibernateException {
		return (VoicesDetails) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {

						return session.createQuery(
								"from VoicesDetails v where v.id = ? ")
								.setLong(0, id).uniqueResult();
					}
				});
	}
}
