package about.nocare.casaer.satanwang.adapter;

import android.content.Context;


import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.List;

import about.nocare.casaer.satanwang.R;
import about.nocare.casaer.satanwang.bean.HomeCouponBean;

/**
 * 领取优惠劵列表适配器
 * Created by wanghaoyu on 2018/2/28.
 */
public class HomeGetWelfareListAdapter extends SuperAdapter<HomeCouponBean> {

    private static final String TYPE_DISCOUNT = "1"; //折扣卷
    private static final String TYPE_AMOUNT = "2"; //满减券

    public HomeGetWelfareListAdapter(Context context, List<HomeCouponBean> items) {
        super(context, items, R.layout.dialog_welfare_list_item);
    }

    @Override
    public void onBind(SuperViewHolder holder, int i, int i1, HomeCouponBean bean) {
        /*判断优惠劵类型 填充优惠劵标题*/
        String title = "";
        switch (bean.getCouponType()) {
            case TYPE_DISCOUNT: {
                title = bean.getDiscount() + "折";
                break;
            }
            case TYPE_AMOUNT: {
                title = "¥" + bean.getAmount();
                break;
            }
            default: {
                title = "¥0";
                break;
            }
        }
        holder.setText(R.id.amount, title);
        /*判断使用金额 如果为0 即为无门槛使用*/
        if (Integer.valueOf(bean.getPayLimit()) == 0) {
            holder.setText(R.id.use, "无门槛使用");
        } else {
            holder.setText(R.id.use, "满¥" + bean.getPayLimit() + "可使用");
        }
        /*适用范围*/
        holder.setText(R.id.scope, bean.getScope());
        holder.setText(R.id.dateRange, "有效期" + bean.getExpiredDateFrom() + "至" + bean.getExpiredDateTo());
    }
}
