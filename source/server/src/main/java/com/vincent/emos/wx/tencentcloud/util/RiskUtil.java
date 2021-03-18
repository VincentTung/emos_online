package com.vincent.emos.wx.tencentcloud.util;

import cn.hutool.core.date.DateUtil;
import com.vincent.emos.wx.controller.form.CheckinForm;
import com.vincent.emos.wx.exception.EmosException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class RiskUtil {
    public static final String STRING_HIGH_RISK = "高风险";
    public static final String STRING_MIDDLE_RISK = "中风险";
    public static final String STRING_LOW_RISK = "低风险";

    // 风险等级
    public static final int RISK_LEVEL_HIGH = 3;
    public static final int RISK_LEVEL_MIDDLE = 2;
    public static final int RISK_LEVEL_LOW = 1;
    public static int getRisk(String cityCode,CheckinForm form){
        int risk = RISK_LEVEL_LOW;
        try {
            String url = "http://m." + cityCode + ".bendibao.com/news/yqdengji/?qu=" + form.getDistrict();
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByClass("list-detail");
            for (Element element : elements) {
                String result = element.text().split(" ")[1];
                if (STRING_HIGH_RISK.equals(result)) {
                    risk = RISK_LEVEL_HIGH;
                    break;
                } else if (STRING_MIDDLE_RISK.equals(result)) {
                    risk = risk < RISK_LEVEL_MIDDLE ? RISK_LEVEL_MIDDLE : risk;
                    break;
                }
            }
        } catch (Exception e) {
            throw new EmosException("获取风险等级失败");
        }

        return risk;
    }
}
