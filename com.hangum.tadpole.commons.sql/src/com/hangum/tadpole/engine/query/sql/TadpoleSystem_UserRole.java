package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.initialize.TadpoleEngineUserDB;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.TadpoleUserDbRoleDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Tadpole basic table (User_group_role)
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_UserRole {
	
	/**
	 * 탈퇴.
	 * @param userSeq
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void withdrawal(int userSeq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.update("userWithdrawal", userSeq); //$NON-NLS-1$
		sqlClient.update("dbWithdrawal", userSeq); //$NON-NLS-1$
	}

	/**
	 * 해당 디비에 사용자 롤이 추가 되어 있는지 검사합니다. 
	 * 
	 * @param userDB
	 * @param user
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static boolean isDBAddRole(UserDBDAO userDB, UserDAO user) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		
		Map<String, Integer> mapParameter = new HashMap<String, Integer>();
		mapParameter.put("db_seq", userDB.getSeq());
		mapParameter.put("user_seq", user.getSeq());
		
		List roleList = sqlClient.queryForList("isDBAddRole", mapParameter);
		return roleList.size()==0;
	}
	
	/**
	 * insert table
	 *  
	 * @param userSeq
	 * @param dbSeq
	 * @param roleType
	 * @param description
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static TadpoleUserDbRoleDAO insertTadpoleUserDBRole(int userSeq, int dbSeq, String roleType, String description) throws TadpoleSQLManagerException, SQLException {
		TadpoleUserDbRoleDAO userDBRoleDao = new TadpoleUserDbRoleDAO();
		userDBRoleDao.setUser_seq(userSeq);
		userDBRoleDao.setDb_seq(dbSeq);
		userDBRoleDao.setRole_id(roleType);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 100);
		userDBRoleDao.setTerms_of_use_endtime(new Timestamp(cal.getTimeInMillis()));
		userDBRoleDao.setDescription(description);
		
		// Insert tadpole_user_db_role table. 
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		TadpoleUserDbRoleDAO roleDao = (TadpoleUserDbRoleDAO)sqlClient.insert("userDBRoleInsert", userDBRoleDao);
		
		return roleDao;
	}
	
	
	/**
	 * insert tadpole_user_db_role table
	 * 
	 * @param userDBRoleDao
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static TadpoleUserDbRoleDAO insertTadpoleUserDBRole(TadpoleUserDbRoleDAO userDBRoleDao) throws TadpoleSQLManagerException, SQLException {
		// Insert tadpole_user_db_role table. 
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		userDBRoleDao = (TadpoleUserDbRoleDAO)sqlClient.insert("userDBRoleInsert", userDBRoleDao);
		
		// save db access control
		TadpoleSystem_AccessControl.saveDBAccessControl(userDBRoleDao);
		
		return userDBRoleDao;
	}
	
	/**
	 * update user role
	 * 
	 * @param userDBRoleDao
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateTadpoleUserDBRole(TadpoleUserDbRoleDAO userDBRoleDao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.update("userDBRoleUpdate", userDBRoleDao);
	}

	/**
	 * user list
	 * @param userDB
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List getUserRoleList(UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("getUserRoleList", userDB);
	}
}
