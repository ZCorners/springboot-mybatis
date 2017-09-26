package batch.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import batch.demo.mapper.CityMapper;
import batch.demo.domain.City;

@RestController
@RequestMapping("/api/city")
public class CityRestController {

    @Autowired
    private CityMapper cityMapper;

    @GetMapping("/{id}")
    public City findById(@PathVariable Long id) {
        return cityMapper.findById(id);
    }

}