package com.healthMonitor.fall2020.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/*
Written by: Ruixi Li
Tested by: Ruixi Li
Debugged by: Ruixi Li
 */


public class BaseService {
    public Date LocalDateTodate() {
        LocalDate localDate = LocalDate.now();
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        return date;
    }
}
