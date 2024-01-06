package com.dzb.dao;

import com.dzb.entity.Partition;

import java.util.List;
import java.util.Map;

public interface PartitionDao {


    List<Partition> getPartitionList(Map<String, Object> map);

    List<Partition> getAllPartitionList();
}
