package com.atguigu.realtime.gmallpublisher.controller;

import com.alibaba.fastjson.JSON;
import com.atguigu.realtime.gmallpublisher.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lzc
 * @Date 2020/9/4 15:53
 */
@RestController
public class PublisherController {
    //http://localhost:8070/realtime-total?date=2020-02-11

    @Autowired
    PublisherService service;

    @GetMapping("/realtime-total")
    public String realtimeTotal(String date) {
        Long dau = service.getDau(date);

        List<Map<String, Object>> result = new ArrayList<>();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", "dau");
        map1.put("name", "新增日活");
        map1.put("value", dau);
        result.add(map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", "new_mid");
        map2.put("name", "新增设备");
        map2.put("value", 233);
        result.add(map2);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("id", "order_amount");
        map3.put("name", "新增交易额");
        map3.put("value", service.getTotalAmount(date));
        result.add(map3);

        return JSON.toJSONString(result);
    }


    // http://localhost:8070/realtime-hour?id=dau&date=2020-02-11
    // http://localhost:8070/realtime-hour?id=order_amount&date=2020-02-14
    @GetMapping("/realtime-hour")
    public String realtimeHour(String id, String date) {
        if ("dau".equals(id)) {
            Map<String, Long> today = service.getHourDau(date);
            Map<String, Long> yesterday = service.getHourDau(getYesterday(date));

            HashMap<String, Map<String, Long>> result = new HashMap<>();
            result.put("yesterday", yesterday);
            result.put("today", today);

            return JSON.toJSONString(result);
        }else if("order_amount".equals(id)){
            Map<String, BigDecimal> today = service.getHourTotalAmount(date);
            Map<String, BigDecimal> yesterday = service.getHourTotalAmount(getYesterday(date));

            HashMap<String, Map<String, BigDecimal>> result = new HashMap<>();
            result.put("yesterday", yesterday);
            result.put("today", today);
            return JSON.toJSONString(result);
        }
        return "ok";

    }

    private String getYesterday(String date) {
        return LocalDate.parse(date).minusDays(1).toString();
    }

}
/*
[{"id":"dau","name":"新增日活","value":1200},
{"id":"new_mid","name":"新增设备","value":233 },
{"id":"order_amount","name":"新增交易额","value":1000.2 }]



{"yesterday":{"11":383,"12":123,"17":88,"19":200 },
"today":{"12":38,"13":1233,"17":123,"19":688 }}


 */