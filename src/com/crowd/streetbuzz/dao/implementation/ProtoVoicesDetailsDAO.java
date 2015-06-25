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
import com.crowd.streetbuzz.model.ProtoVoicesDetails;

/**
 * @author Atrijit
 *
 */
public class ProtoVoicesDetailsDAO extends HibernateDaoSupport implements CommonDAOInterface{
	public void addOrUpdateRecord(Object obj) throws HibernateException{
		getHibernateTemplate().saveOrUpdate(obj);
	}

	public void deleteRecord(Object obj) throws HibernateException{
		getHibernateTemplate().delete(obj);
	}
	public List getAllRecords() throws HibernateException{
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session)
                throws HibernateException {
                
                List result = session.createQuery("from ProtoVoicesDetails").list();
                return result;
            }

        });
	}
	
	public List getAllRecordsbyVoices(final Long voicesid) throws HibernateException{
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session)
                throws HibernateException {
                
                List result = session.createQuery("from ProtoVoicesDetails v where v.voicesid=?").setLong(0, voicesid).list();
                return result;
            }

        });
	}
	
	public List getAllRecordsbyVoicesAfterId(final Long voicesid, final Long afterid) throws HibernateException{
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session)
                throws HibernateException {
                
            	Criteria criteria = session.createCriteria(ProtoVoicesDetails.class).add(Restrictions.eq("voicesid", voicesid)).add(Restrictions.gt("id",afterid));
				return criteria.list();
            }

        });
	}
	
	public Object getObjectById(final Long id) throws HibernateException{
		return (ProtoVoicesDetails) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                        throws HibernateException {
                        
                        return session.createQuery("from ProtoVoicesDetails v where v.id = ? ")
                        .setLong(0, id).uniqueResult();
                    }
                });
	}
}
