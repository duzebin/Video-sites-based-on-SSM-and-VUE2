package com.dzb.service;

import com.dzb.entity.Publicize;

import java.util.List;

public interface PublicizeService {
    List<Publicize> getPublicizeByType(String type);
}
