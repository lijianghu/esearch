package com.hand.hand.mapper;

import java.util.List;
import java.util.Map;

public interface ContactPersonMapper {

    List<Map<String, Object>> findContactPersonList(String name);

}
