package com.ecity.cswatersupply.model;

/***
 * 工单汇总详情信息数据模型
 * @author qw
 *
 */
public class SummaryDetailModel extends AModel implements Comparable<SummaryDetailModel> {
    private static final long serialVersionUID = 1L;
    //日期
    private String date;
    private String dateStr;
    //工单总数
    private String total;
    //已完结
    private String finishedAmount;
    //处理中
    private String dealingAmount;
    //超期
    private String overSceduleAmount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFinishedAmount() {
        return finishedAmount;
    }

    public void setFinishedAmount(String finishedAmount) {
        this.finishedAmount = finishedAmount;
    }

    public String getDealingAmount() {
        return dealingAmount;
    }

    public void setDealingAmount(String dealingAmount) {
        this.dealingAmount = dealingAmount;
    }

    public String getOverSceduleAmount() {
        return overSceduleAmount;
    }

    public void setOverSceduleAmount(String overSceduleAmount) {
        this.overSceduleAmount = overSceduleAmount;
    }


    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    @Override
    public int compareTo(SummaryDetailModel another) {
        int result = date.compareTo(another.date);

        if (result < 0)
            return -1;
        if (result > 0)
            return 1;

        return 0;
    }

}
