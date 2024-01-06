package com.dzb.service;

import com.dzb.controller.request.PartitionPage;
import com.dzb.entity.Partition;

import java.util.List;

public interface PartitionService {

    List<Partition> getPartitionList(PartitionPage partitionPage);

    List<Partition> getAllPartitionList();
}
