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
import com.crowd.streetbuzz.model.GCM;

/**
 * @author Atrijit
 *
 */
public class GCMDAO extends HibernateDaoSupport implements CommonDAOInterface{
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
                
                List result = session.createQuery("from GCM").list();
                return result;
            }

        });
	}
	
	public Object getObjectById(final Long id) throws HibernateException{
		return (GCM) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                        throws HibernateException {
                        
                        return session.createQuery("from GCM g where g.id = ? ")
                        .setLong(0, id).uniqueResult();
                    }
                });
	}
	
	public Object getObjectByUserId(final Long userid) throws HibernateException{
		return (GCM) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                        throws HibernateException {
                        
                        return session.createQuery("from GCM g where g.userid = ?")
                        .setLong(0, userid).uniqueResult();
                    }
                });
	}
	
	public Object getObjectByUserIdSocialnetwork(final Long userid, final String socialnetwork) throws HibernateException{
		return (GCM) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                        throws HibernateException {
                        
                        return session.createQuery("from GCM g where g.userid = ? and g.socialnetwork=?")
                        .setLong(0, userid).setString(1,socialnetwork).uniqueResult();
                    }
                });
	}
}
