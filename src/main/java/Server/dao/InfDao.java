package Server.dao;

import Server.pojo.Parame;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
@Repository("infdao")
public interface InfDao {

	public HashMap select(Parame parame);

}
