package com.lovcreate.core.util;

/**
 * 正则校验工具类
 * Created by Albert.Ma on 2017/10/19 0019.
 */
public class CheckUtil {

    /**
     * 手机号码规范判断
     */
    public static Boolean phoneNumberCheck(String str) {
        String regEx = "^((13[0-9])|(15[^4,\\D])|(14[579])|(17[0-9])|(18[0,0-9]))\\d{8}$";
        return str.matches(regEx);
    }

    /**
     * 密码规范判断6-16位
     */
    public static Boolean passwordCheck(String str) {
        String regEx = "^[a-zA-Z0-9]\\w{5,15}$";
        return str.matches(regEx);
    }

    /**
     * 复杂密码规范
     * 检验密码是含有小写字母、大写字母、数字、特殊符号的两种及以上
     * 6-16位
     */
    public static Boolean mazyPasswordCheck(String str) {
        String regEx = "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\S{6,16}$";
        return str.matches(regEx);
    }

    /**
     * 姓名规范验证
     */
    public static Boolean nameCheck(String str) {
        String regEx = "^[A-Z|a-z|\\u4e00-\\u9fa5]*$";
        return str.matches(regEx);
    }

    /**
     * 邮箱规范验证
     */
    public static Boolean emailCheck(String str) {
        String regEx = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        return str.matches(regEx);
    }

    /**
     * 文字输入校验
     *
     * @param str 中文、英文、数字、特殊字符(
     *            1.	百分号
     *            2.	破折号
     *            3.	省略号
     *            4.	双引号
     *            5.	间隔号
     *            6.	感叹号
     *            7.	书名号
     *            8.	逗号
     *            9.	顿号
     *            10.	括号
     *            11.	冒号
     *            12.	问号
     *            13.   句号
     *            )
     * @return 包含以上条件则返回true
     */
    public static Boolean textCheck(String str) {
        String regEx = "[a-zA-Z0-9\u4E00-\u9FA5_,\\.;\\:\"'?!\\-%—…“”|！《》，、()（）:：？。]+";
        return str.matches(regEx);
    }

}
