package com.zzz.ecity.android.applibrary.database;

import java.util.List;

import com.ecity.android.db.model.DBRecord;
import com.zzz.ecity.android.applibrary.model.GPSPositionBean;
import com.zzz.ecity.android.applibrary.utils.DateUtilExt;

public class GPSPositionDao extends ABaseDao<GPSPositionBean> {
	public static final String TB_NAME = "GPSPosition";
	private static GPSPositionDao instance;

	public static GPSPositionDao getInstance() {
		if (null == instance) {
			try {
				Thread.sleep(300);
				synchronized (GPSPositionDao.class) {
					if (null == instance) {
						instance = new GPSPositionDao();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	private GPSPositionDao() {
	}

	@Override
	protected String getTableName() {
		return TB_NAME;
	}

	@Override
	protected boolean checkRecordType(DBRecord record) {
		return (record == null) ? false
				: (record.getBean() instanceof GPSPositionBean);
	}

	@Override
	protected Class<GPSPositionBean> getBeanClass() {
		return GPSPositionBean.class;
	}

	@Override
	protected String getCheckExistenceWhereBeforeInsert(GPSPositionBean bean) {
		return null;
	}

	/***
	 * * Description:保存轨迹<br/>
	 * 
	 * @param beans
	 * @version V1.0
	 */
	public synchronized void savePositionBeans(List<GPSPositionBean> beans) {
		if (null == beans || beans.size() < 0) {
			return;
		}
		int size = beans.size();
		for (int i = 0; i < size; i++) {
			savePositionBean(beans.get(i));
		}
	}

	/***
	 * * Description:保存轨迹<br/>
	 * 
	 * @param bean
	 * @version V1.0
	 */
	public synchronized void savePositionBean(GPSPositionBean bean) {
		try {
			insert(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * * Description:更新轨迹<br/>
	 * 
	 * @param beanList
	 * @version V1.0
	 */
	public synchronized void updateGPSPositions(List<GPSPositionBean> beanList) {
		if (beanList == null || beanList.size() < 1) {
			return;
		}
		int size = beanList.size();
		for (int i = 0; i < size; i++) {
			updateGPSPosition(beanList.get(i));
		}
	}

	/**
	 * * Description:更新本地数据库中的GPS坐标<br/>
	 * 
	 * @param bean
	 * @version V1.0
	 */
	public synchronized void updateGPSPosition(GPSPositionBean bean) {
		try {
			String where = " _rid = '" + bean.getId() + "' ";
			update(bean, where);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * * Description:<br/>
	 * 
	 * @param userid
	 *            用户id
	 * @param days
	 *            今日之前几天
	 * @param state
	 *            查询数据类型 0 未上�? 1 已上�? 2 全部
	 * @param sort
	 *            排序方式 0 asc 升序 1 desc 降序 2 不排�?
	 * @param limit
	 *            限制条数 小于1不限�?
	 * @param offset
	 *            从第几条�?始取数据
	 * @return
	 * @version V1.0
	 */
	public List<GPSPositionBean> getPositionBeans(String userid, int days,
			int state, int sort, int limit, int offset) {
		List<GPSPositionBean> results = null;
		if (days < 1) {
			days = 1;
		}

		try {
			String where = buildORTimeWhereBeforeDays("time", days)
					+ " AND userid = '" + userid;
			if (state == 0 || state == 1) {
				where += "' AND status = '" + state + "'";
			} else {
				where += "'";
			}

			if (sort == 0 || sort == 1) {
				if (sort == 0) {
					where += " ORDER BY time ASC";
				} else {
					where += " ORDER BY time DESC";
				}

			}

			if (limit > 0) {
				if (offset < 0) {
					offset = 0;
				}
				where += " LIMIT " + limit + " OFFSET " + offset;
			}
			results = queryList(where);
		} catch (Exception e) {
			e.printStackTrace();
			results = null;
		}

		return results;
	}
	/***
	 * 获取未上报坐标的数目
	 * @param userid 用户编号
	 * @param days 查询的天数 比如6 就代表今天往前6天之内的数据
	 * @param state 上报
	 * @return
	 */
	public int getNoreportedPositionsCount(int userid, int days, int state) {
		int count = 0;
		if (days < 1) {
			days = 1;
		}
		try {
			String where = buildORTimeWhereBeforeDays("time", days)
					+ " AND userid = '" + userid;
			if (state == 0 || state == 1) {
				where += "' AND status = '" + state + "'";
			}
			count = getRecordCount(where);
		} catch (Exception e) {
			e.printStackTrace();
			count = 0;
		}

		return count;
	}
	/***
	 * 获得某个用户当天的轨迹
	 * @param userid
	 * @return
	 */
	public List<GPSPositionBean> getToadyGPSPositionByUserID(int userid) {
		List<GPSPositionBean> result = null;

		try {
			String where = " time like '%" + DateUtilExt.getServiceDate()
					+ "%' AND userid = '" + userid + "'  ORDER BY time ASC";
			result = queryList(where);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/***
	 * 删除七天以前的数据
	 * 
	 * @version V1.0
	 */
	public void deleteGPSPositions7DaysBefore() {
		List<String> days = DateUtilExt.getDaysBeforeDays(6);
		String where = "";
		for (String day : days) {
			where += " time NOT LIKE '%" + day + "%' AND ";
		}
		where += " _rid > '-1'";
		try {
			delete(where);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String buildORTimeWhereBeforeDays(String filed, int day) {
		String timeWhere = "";
		List<String> days = DateUtilExt.getDaysBeforeDays(day);
		if (null == days || days.size() < 1) {
			timeWhere = filed + " like '%" + DateUtilExt.getServiceDate()
					+ "%'";
		} else {
			for (int i = 0; i < days.size() - 1; i++) {
				timeWhere += filed + " like '%" + days.get(i) + "%'";
				timeWhere += " OR ";
			}
			timeWhere += filed + " like '%" + days.get(days.size() - 1) + "%' ";
			timeWhere = "(" + timeWhere + ") ";
		}
		return timeWhere;
	}
}
