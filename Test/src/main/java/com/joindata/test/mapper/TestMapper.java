package com.joindata.test.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface TestMapper
{
    public List<String> test();
}
