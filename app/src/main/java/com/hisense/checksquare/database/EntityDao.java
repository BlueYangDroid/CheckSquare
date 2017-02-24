package com.hisense.checksquare.database;

import android.content.Context;

import com.hisense.checksquare.entity.CheckEntity;
import com.hisense.checksquare.entity.CheckEntityDao;
import com.hisense.checksquare.entity.DaoMaster;
import com.hisense.checksquare.entity.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by yanglijun.ex on 2017/2/24.
 */

public class EntityDao {
    /**
     * 插入一条记录
     *
     * @param entity
     */
    public void insert(Context context, CheckEntity entity) {
        DaoMaster daoMaster = new DaoMaster(DBManager.getInstance(context).getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CheckEntityDao checkEntityDao = daoSession.getCheckEntityDao();
        checkEntityDao.insert(entity);
    }

    /**
     * 插入集合
     *
     * @param checkEntities
     */
    public void insertList(Context context, List<CheckEntity> checkEntities) {
        if (checkEntities == null || checkEntities.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(DBManager.getInstance(context).getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CheckEntityDao checkEntityDao = daoSession.getCheckEntityDao();
        checkEntityDao.insertOrReplaceInTx(checkEntities);
    }

    /**
     * 删除一条记录
     *
     * @param checkEntity
     */
    public void delete(Context context, CheckEntity checkEntity) {
        DaoMaster daoMaster = new DaoMaster(DBManager.getInstance(context).getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CheckEntityDao checkEntityDao = daoSession.getCheckEntityDao();
        checkEntityDao.delete(checkEntity);
    }

    /**
     * 更新一条记录
     *
     * @param checkEntity
     */
    public void update(Context context, CheckEntity checkEntity) {
        DaoMaster daoMaster = new DaoMaster(DBManager.getInstance(context).getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CheckEntityDao checkEntityDao = daoSession.getCheckEntityDao();
        checkEntityDao.update(checkEntity);
    }

    /**
     * 查询列表
     */
    public List<CheckEntity> queryList(Context context) {
        DaoMaster daoMaster = new DaoMaster(DBManager.getInstance(context).getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CheckEntityDao checkEntityDao = daoSession.getCheckEntityDao();
        QueryBuilder<CheckEntity> qb = checkEntityDao.queryBuilder();
        List<CheckEntity> list = qb.list();
        return list;
    }

    /**
     * 查询列表
     */
    public List<CheckEntity> queryList(Context context, int type) {
        DaoMaster daoMaster = new DaoMaster(DBManager.getInstance(context).getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CheckEntityDao checkEntityDao = daoSession.getCheckEntityDao();
        QueryBuilder<CheckEntity> qb = checkEntityDao.queryBuilder();
        qb.where(CheckEntityDao.Properties.Type.eq(type)).orderAsc(CheckEntityDao.Properties.CheckId);
        List<CheckEntity> list = qb.list();
        return list;
    }
}
