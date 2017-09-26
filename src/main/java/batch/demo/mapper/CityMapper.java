package batch.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import batch.demo.domain.City;

@Mapper
public interface CityMapper {

    City findById(Long id);

    void insertOne(City city);

    void insertBatch(List<City> list);
}
