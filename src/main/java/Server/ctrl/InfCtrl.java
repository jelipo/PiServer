package Server.ctrl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import Server.pojo.Parame;
import Server.service.Inf;
@Controller
public class InfCtrl {
	
	@ResponseBody
	@RequestMapping("send")  //接收参数,参数temperature,humidity
	public JSONObject sendPemperature(@ModelAttribute Parame parame){ 
		return inf.insertParameToRedis(parame);
	}
	
	
	
	@Resource(name="Inf")
	public Inf inf;

}
