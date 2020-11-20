package com.healthMonitor.fall2020.utils;

import java.util.UUID;

public class IDTool {

    public static  void main(String [] args)
    {
        System.out.println(IDTool.NewID());
    }

    public static String NewID()
    {
        return UUID.randomUUID().toString().replace("-", "");
    }
}