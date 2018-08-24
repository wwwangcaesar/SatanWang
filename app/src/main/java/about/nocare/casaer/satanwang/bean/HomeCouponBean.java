package about.nocare.casaer.satanwang.bean;

/**
*     优惠券bean
* @author Satan Wang
* created at 2018/7/10 11:40
*/

public class HomeCouponBean {
    private String couponType;
    private String discount;
    private String amount;
    private String payLimit;
    private String scope;
    private String expiredDateFrom;
    private String expiredDateTo;

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayLimit() {
        return payLimit;
    }

    public void setPayLimit(String payLimit) {
        this.payLimit = payLimit;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getExpiredDateFrom() {
        return expiredDateFrom;
    }

    public void setExpiredDateFrom(String expiredDateFrom) {
        this.expiredDateFrom = expiredDateFrom;
    }

    public String getExpiredDateTo() {
        return expiredDateTo;
    }

    public void setExpiredDateTo(String expiredDateTo) {
        this.expiredDateTo = expiredDateTo;
    }
}
