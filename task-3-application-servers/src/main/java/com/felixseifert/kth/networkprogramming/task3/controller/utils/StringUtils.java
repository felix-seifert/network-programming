package com.felixseifert.kth.networkprogramming.task3.controller.utils;

import java.util.List;

public class StringUtils {

    public static boolean areListsEqual(List<String> staticList, List<String> dynamicList){
        return staticList.equals(dynamicList);
    }
}
