package com.dzb.service.Impl;

import com.dzb.controller.request.PartitionPage;
import com.dzb.dao.PartitionDao;
import com.dzb.entity.Partition;
import com.dzb.service.PartitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PartitionServiceImpl implements PartitionService {
    @Autowired private PartitionDao partitionDao;

    @Override
    public List<Partition> getPartitionList(PartitionPage partitionPage) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("pageStart",(partitionPage.getPageNum()-1)*partitionPage.getPageSize());
        map.put("pageSize",partitionPage.getPageSize());
        return this.partitionDao.getPartitionList(map);
    }

    @Override
    public List<Partition> getAllPartitionList() {
        return this.partitionDao.getAllPartitionList();
    }
}
