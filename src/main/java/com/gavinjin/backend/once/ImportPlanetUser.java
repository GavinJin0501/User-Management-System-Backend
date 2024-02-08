package com.gavinjin.backend.once;

import com.alibaba.excel.EasyExcel;

import java.util.List;

/**
 * Import planet user to the database
 */
public class ImportPlanetUser {
    public static void main(String[] args) {
        String fileName = "/Users/gavin/Documents/GitHub/User-Management-System-Backend/src/main/resources/prodExcel.xlsx";
        List<PlanetUserInfo> totalDataList =
                EasyExcel.read(fileName).head(PlanetUserInfo.class).sheet().doReadSync();
        for (PlanetUserInfo planetUserInfo : totalDataList) {
            System.out.println("sync: " + planetUserInfo);
        }
    }
}
