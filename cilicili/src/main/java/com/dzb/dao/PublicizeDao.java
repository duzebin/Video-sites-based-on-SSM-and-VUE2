package com.dzb.dao;

import com.dzb.entity.Publicize;

import java.util.List;

public interface PublicizeDao {
    List<Publicize> getPublicizeByType(String type);
}
