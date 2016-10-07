package Server.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import Server.dao.InfDao;
import Server.pojo.Parame;
import tool.Tools;
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




}
