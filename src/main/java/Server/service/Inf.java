package Server.service;

import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import Server.pojo.Parame;

public interface Inf {
	
	public HashMap getParame(Parame parame);
	
	public JSONObject insertParameToRedis(Parame parame);
	

}
