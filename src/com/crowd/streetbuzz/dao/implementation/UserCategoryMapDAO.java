/**
 * 
 */
package com.crowd.streetbuzz.dao.implementation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.crowd.streetbuzz.dao.interfaces.CommonDAOInterface;
import com.crowd.streetbuzz.model.UserCategoryMap;

/**
 * @author Atrijit
 *
 */
public class UserCategoryMapDAO extends HibernateDaoSupport implements CommonDAOInterface{
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
                
                List result = session.createQuery("from UserCategoryMap").list();
                return result;
            }

        });
	}
	
	public List getAllCategoriesforUser(final Long userid) throws HibernateException{
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session)
                throws HibernateException {
                
                List result = session.createQuery("from UserCategoryMap u where u.userid=?").setLong(0,userid).list();
                return result;
            }

        });
	}
	

	
	public List getAllUsersforCategory(final Long categoryid) throws HibernateException{
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session)
                throws HibernateException {
                List userList = new ArrayList();
                List result = session.createQuery("from UserCategoryMap u where u.categoryid=?").setLong(0,categoryid).list();
                for (int i=0;i<result.size();i++){
                	UserCategoryMap ucm = (UserCategoryMap)result.get(i);
                	Long userid = ucm.getUserid();
                	if(!userList.contains(userid)){
                		userList.add(userid);
                	}
                }
                return userList;
            }

        });
	}
	
	public Object getObjectByCatIdUserId(final Long categoryid, final Long userid) throws HibernateException{
		return (UserCategoryMap) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                        throws HibernateException {
                        
                        return session.createQuery("from UserCategoryMap u where u.categoryid = ? and u.userid = ?")
                        .setLong(0, categoryid).setLong(1,userid).uniqueResult();
                    }
                });
	}
	
	public Object getObjectByCatIdSubCatIdUserId(final Long categoryid, final Long userid, final Long subcategoryid) throws HibernateException{
		return (UserCategoryMap) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                        throws HibernateException {
                        
                        /*return session.createQuery("from UserCategoryMap u where u.categoryid = ? and u.subcategoryid = ? and u.userid = ?")
                        .setLong(0, categoryid).setLong(1, subcategoryid).setLong(2,userid).uniqueResult();*/
                    	List result = session.createQuery("from UserCategoryMap u where u.categoryid = ? and u.subcategoryid = ? and u.userid = ?")
                        .setLong(0, categoryid).setLong(1, subcategoryid).setLong(2,userid).list();
                    	if(result!=null && result.size()>0){
                    		UserCategoryMap ucm = (UserCategoryMap)result.get(0);
                    		return ucm;
                    	}else{
                    		return null;
                    	}
                    }
                });
	}
	
	
	
	public Object getObjectById(final Long id) throws HibernateException{
		return (UserCategoryMap) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                        throws HibernateException {
                        
                        return session.createQuery("from UserCategoryMap u where u.id = ? ")
                        .setLong(0, id).uniqueResult();
                    }
                });
	}
}
