package com.lovcreate.core.base;

import java.util.List;

/**
 * 权限回调接口
 * Created by Albert.Ma on 2017/8/17 0017.
 */

public interface PermissionListener {

    //授权成功
    void onGranted();

    //授权部分
    void onGranted(List<String> grantedPermission);

    //拒绝授权
    void onDenied(List<String> deniedPermission);
}
