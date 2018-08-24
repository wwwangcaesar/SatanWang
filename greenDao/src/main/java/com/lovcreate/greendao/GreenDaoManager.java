package com.lovcreate.greendao;

import android.content.Context;

import com.lovcreate.greendao.gen.DaoMaster;
import com.lovcreate.greendao.gen.DaoSession;

/**
 * GreenDao管理工具
 * <p>
 * 创建实例：GreenDaoManager.getInstance(this).getmDaoSession().getMessageDao();
 * 插入数据：messageDao.insert()
 * 查询数据：messageDao.queryBuilder()
 * 设置分页：queryBuilder.offset(...).limit(Integer.parseInt(PAGE_SIZE));
 * <p>
 * Created by Albert.Ma on 2017/10/27 0027.
 */
public class GreenDaoManager {

    private DaoMaster mDaoMaster;

    private DaoSession mDaoSession;

    private static Context context;

    private GreenDaoManager(Context context) {
        init(context);
    }

    /**
     * 静态内部类，实例化对象使用
     */
    private static class SingleInstanceHolder {
        private static final GreenDaoManager INSTANCE = new GreenDaoManager(GreenDaoManager.context);
    }

    /**
     * 对外唯一实例的接口
     *
     * @return
     */
    public static GreenDaoManager getInstance(Context context) {
        GreenDaoManager.context = context;
        return SingleInstanceHolder.INSTANCE;
    }

    /**
     * 初始化数据
     */
    private void init(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "lovcreate-db-hydra");
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoMaster getmDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

}
