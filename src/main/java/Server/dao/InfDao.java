package Server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import Server.pojo.Parame;
@Repository("infdao")
public interface InfDao {

	public HashMap select(Parame parame);

	public void insertparame(Parame parame);
}
