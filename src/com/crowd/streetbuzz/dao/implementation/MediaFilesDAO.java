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
import com.crowd.streetbuzz.model.MediaFiles;

/**
 * @author Atrijit
 *
 */
public class MediaFilesDAO extends HibernateDaoSupport implements CommonDAOInterface{
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
                
                List result = session.createQuery("from MediaFiles").list();
                return result;
            }

        });
	}
	
	public Object getObjectById(final Long id) throws HibernateException{
		return (MediaFiles) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                        throws HibernateException {
                        
                        return session.createQuery("from MediaFiles m where m.id = ? ")
                        .setLong(0, id).uniqueResult();
                    }
                });
	}
	
	public Object getObjectByUniqueId(final String uniqueid) throws HibernateException{
		return (MediaFiles) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                        throws HibernateException {
                        
                        return session.createQuery("from MediaFiles m where m.uniqueid = ? ")
                        .setString(0, uniqueid).uniqueResult();
                    }
                });
	}
}
