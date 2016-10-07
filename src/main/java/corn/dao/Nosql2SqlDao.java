package corn.dao;



import corn.pojo.Parame;
import org.springframework.stereotype.Repository;


/**
 * Created by 10441 on 2016/10/7.
 */
@Repository("corn/dao/Nosql2SqlDao")
public interface Nosql2SqlDao {

    public void insertparame(Parame parame);

}
