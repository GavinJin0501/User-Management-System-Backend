package com.gavinjin.backend.once;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;

import java.util.List;

/**
 * Import Excel file and process it
 */
public class ImportExcel {
    /**
     * Read the data from the excel
     */
    public static void main(String[] args) {
        String fileName = "/Users/gavin/Documents/GitHub/User-Management-System-Backend/src/main/resources/prodExcel.xlsx";
        asyncReadExcel(fileName);
        syncReadExcel(fileName);
    }

    /**
     * Asynchronously read the Excel data file using listener
     *
     * @param fileName
     */
    public static void asyncReadExcel(String fileName) {
        EasyExcel.read(fileName, PlanetUserInfo.class, new ExcelDataListener()).sheet().doRead();
    }

    /**
     * Synchronously read the Excel data file using listener
     *
     * @param fileName
     */
    public static void syncReadExcel(String fileName) {
        List<PlanetUserInfo> totalDataList =
                EasyExcel.read(fileName).head(PlanetUserInfo.class).sheet().doReadSync();
        for (PlanetUserInfo planetUserInfo : totalDataList) {
            System.out.println("sync: " + planetUserInfo);
        }
    }
}
