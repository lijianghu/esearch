package com.hand.hand.service.Impl;

import com.hand.hand.EsUtils;
import com.hand.hand.mapper.ContactPersonMapper;
import com.hand.hand.service.ContactPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ContactPersonServiceImpl implements ContactPersonService {

    @Autowired
    private EsUtils esUtils;

    @Override
    public List<Map<String, Object>>  searchKey(String id, String name){
        List list = esUtils.searchKey(id, name);
        return list;
    }

}
