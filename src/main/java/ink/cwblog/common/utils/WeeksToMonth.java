package ink.cwblog.common.utils;

import ink.cwblog.common.vo.WeekVo;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @auther chenw
 * @date 2021/10/22 14:45
 *
 * 一个月内的周
 * 例如 十月1号是周三
 * 那么第一周则是 周三-周日
 *
 */
public class WeeksToMonth {

    public static List<WeekVo> getWeekList(Date startDate, Date endDate){
        List<WeekVo> weekList = new ArrayList<>();
        //转换成joda-time的对象
        DateTime firstDay = new DateTime(startDate).dayOfWeek().withMinimumValue();
        DateTime lastDay = new DateTime(endDate).dayOfWeek().withMaximumValue();
        //计算两日期间的区间天数
        Period p = new Period(firstDay, lastDay, PeriodType.days());
        int days = p.getDays();
        if (days > 0){
            int weekLength = 7;
            for(int i=0;i<days;i=i+weekLength){
                String monDay = firstDay.plusDays(i).toString("yyyy/MM/dd");
                String sunDay = firstDay.plusDays(i+6).toString("yyyy/MM/dd");
                WeekVo week = new WeekVo();
                week.setStart(new Date(monDay));
                week.setEnd(new Date(sunDay));
                weekList.add(week);
            }
        }
        weekList.get(0).setStart(startDate);
        weekList.get(weekList.size()-1).setEnd(endDate);
        return weekList;
    }
}
