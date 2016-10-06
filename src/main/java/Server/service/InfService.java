package Server.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import Server.dao.InfDao;
import Server.pojo.Parame;
import Tool.Tools;
import redis.clients.jedis.Jedis;

@Service("Inf")
public class InfService implements Inf {

	@Resource
	private Tools tools;

	@Resource(name = "infdao")
	private InfDao dao;

	@Override
	public HashMap getParame(Parame parame) {
		// TODO Auto-generated method stub
		return dao.select(parame);
	}

	public JSONObject insertParameToRedis(Parame parame) {

		// dao.insertparame(parame);
		Jedis jedis = tools.getJedis();
		JSONObject json = (JSONObject) JSON.toJSON(parame);
		jedis.rpush("list", json.toString());
		jedis.expire("list", 1300);
		JSONObject json1 = new JSONObject();
		json1.put("flag", "SUCCESS");
		return json1;

	}

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
			dao.insertparame(newParame);
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
