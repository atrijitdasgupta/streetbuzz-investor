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
import com.crowd.streetbuzz.model.User;

/**
 * @author Atrijit
 *
 */
public class UserDAO extends HibernateDaoSupport implements CommonDAOInterface{
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
                
                List result = session.createQuery("from User").list();
                return result;
            }

        });
	}
	
	public Object getObjectById(final Long id) throws HibernateException{
		return (User) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                        throws HibernateException {
                        
                        return session.createQuery("from User u where u.id = ? ")
                        .setLong(0, id).uniqueResult();
                    }
                });
	}
	
	public Object getObjectByUserIdSocialnetwork(final Long userid, final String socialnetwork) throws HibernateException{
		return (User) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                        throws HibernateException {
                        
                        return session.createQuery("from User u where u.userid = ? and u.socialnetwork=?")
                        .setLong(0, userid).setString(1, socialnetwork).uniqueResult();
                    }
                });
	}
	
	public Object getObjectByUserId(final Long userid) throws HibernateException{
		return (User) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                        throws HibernateException {
                        
                        return session.createQuery("from User u where u.userid = ?")
                        .setLong(0, userid).uniqueResult();
                    }
                });
	}
	
	public Object getObjectByEmailId(final String emailid) throws HibernateException{
		return (User) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                        throws HibernateException {
                        
                        return session.createQuery("from User u where u.email = ?")
                        .setString(0, emailid).uniqueResult();
                    }
                });
	}
}
