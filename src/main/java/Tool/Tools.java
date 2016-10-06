package Tool;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;


@Component
public class Tools {
	/*此类为需要初始化的各项服务，此注释代表初始化此bean时，执行此方法，估计同样也可以放在此函数的构造方法中，
	 * 但是考虑到服务器关闭时各项服务需要销毁，所以使用@PostConstruct和@PreDestroy注解
	 * 
	 */
	private Jedis jedis;
	private static Logger log=Logger.getLogger(Tools.class);
	
	@PostConstruct   
	private void init(){
		log.info("初始化Redis");
		jedis=new Jedis("localhost", 6379);
	}
	@PreDestroy
	private void destory(){
		System.out.println();
		jedis.close();
	}
	public Jedis getJedis() {
		return jedis;
	}
	
}
