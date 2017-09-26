package batch.demo.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import batch.demo.domain.City;
import batch.demo.mapper.CityMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Rollback
//@Transactional
public class CityMapperTest {

    // 模拟插入10w条数据，对比三种ExecutorType: batch, simple, reuse

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private long count = 100000;
    private long size = 1000;
    private List<City> list = new LinkedList<>();

    @Test
    public void testFindById() throws Exception {
        cityMapper.findById(1l);
    }

    // 4256ms
    @Test
    public void executeDefault() throws Exception {
        System.out.println(sqlSessionTemplate.getExecutorType()); // simple
        for (long i = 1; i <= count; i++) {
            City city = new City(i, "name" + i, "desc" + i);
            list.add(city);
            if (i % size == 0) {
                cityMapper.insertBatch(list);
                list.clear();
            }
        }
    }

    // batch: 4192ms
    // simple: 10533ms，默认类型
    // reuse: 10158ms
    @Test
    public void executorTypeBatch() throws Exception {
        // 重新获取的sqlSession不会将数据写入数据库，需要手动commit
        // 对比不同的executorType
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH);
        CityMapper cityMapper = sqlSession.getMapper(CityMapper.class);

        for (long i = 1; i <= count; i++) {
            City city = new City(i, "name" + i, "desc" + i);
            list.add(city);
            if (i % size == 0) {
                cityMapper.insertBatch(list);
                list.clear();
            }
        }
        sqlSession.commit();
        sqlSession.clearCache();
        sqlSession.close();
    }

    // 很慢
    @Test
    public void insertOne() throws Exception {
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH);
        CityMapper cityMapper = sqlSession.getMapper(CityMapper.class);

        for (long i = 1; i <= count; i++) {
            City city = new City(i, "name" + i, "desc" + i);
            cityMapper.insertOne(city);
            if (i % size == 0) {
                sqlSession.commit();
                sqlSession.clearCache();
            }
        }
        sqlSession.close();
    }
}