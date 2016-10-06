package Listener.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import Server.dao.InfDao;
import Server.pojo.Parame;
import Server.service.Inf;
import Tool.Tools;
import redis.clients.jedis.Jedis;

@Component
public class NoSql2Sql {

	/*
	 * 定时器任务，属于spring管辖，@Scheduled注解可以在启动程序时每隔一段时间执行注解下的方法
	 */

	@Resource
	private Tools tools;
	@Resource(name = "Inf")
	private Inf inf;

	@Scheduled(cron = "0 */10 * * * ?") 
	public void dosomething() {
		inf.insertParameToSql(); //从redis中取出并计算，存储到数据库中
	}

	
}
