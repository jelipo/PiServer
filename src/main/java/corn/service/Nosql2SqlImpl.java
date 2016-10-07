package corn.service;



import com.alibaba.fastjson.JSON;
import corn.dao.Nosql2SqlDao;
import corn.pojo.Parame;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import tool.Tools;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 10441 on 2016/10/7.
 */
@Service("corn/service/Nosql2SqlImpl")
public class Nosql2SqlImpl implements Nosql2Sql {

    @Resource(name = "tool/Tools")
    private Tools tools;

    @Resource(name = "corn/dao/Nosql2SqlDao")
    private Nosql2SqlDao dao;
    private static Logger log=Logger.getLogger(Nosql2SqlImpl.class);
    @Override
    public void insertParameToSql() {
        Jedis jedis = tools.getJedis();
        List<String> list = jedis.lrange("list", 0, -1);
        jedis.del("list");
        if (list.size() > 0) {
            List<Integer> temperatureList = new ArrayList<>();
            List<Integer> humidityList = new ArrayList<>();
            for (int i = 0, listsize = list.size(); i < listsize; i++) {
                Parame parame1 = JSON.parseObject(list.get(i), Parame.class);
                temperatureList.add(parame1.getTemperature());
                humidityList.add(parame1.getHumidity());
            }
            Collections.sort(temperatureList);
            Collections.sort(humidityList);
            Integer temperatureAverage = setAverage(temperatureList);
            Integer humidityAverage = setAverage(humidityList);
            Parame newParame = new Parame();
            newParame.setTemperature(temperatureAverage);
            newParame.setHumidity(humidityAverage);
            LocalDateTime time = LocalDateTime.now();
            newParame.setYear(time.getYear());
            newParame.setMonth(time.getMonthValue());
            newParame.setDay(time.getDayOfMonth());
            newParame.setHour(time.getHour());
            newParame.setMinute(time.getMinute());
            newParame.setSecond(time.getSecond());
            log.info("list数据插入数据库");
            dao.insertparame(newParame);
        }else {
            log.info("list无数据，不进行操作");
        }
    }
    private Integer setAverage(List<Integer> list) {
		/*
		 * 因为数据难免有不稳定性，所以为了尽量减小误差，把list中的最高和最低都去掉（list的size低于5时生效），
		 * 当list的size大于5时，根据百分比来计算需要去除的数量，
		 */
        if (list.size() > 2) {
            if (list.size() >= 5) {
                int delSize = list.size() * 3 / 10;
                for (int i = 0; i < delSize; i++) {
                    list.remove(i);
                    list.remove(list.size() - 1);
                }
            } else {
                list.remove(0);
                list.remove(list.size() - 1);
            }
        }
        Integer value = 0;
        for (int i = 0; i < list.size(); i++) {
            value += list.get(i);
        }
        return value / (list.size());
    }

}
