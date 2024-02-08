package com.gavinjin.backend.once;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelDataListener implements ReadListener<PlanetUserInfo> {
    /**
     * Invoked for each line of the Excel
     *
     * @param data
     * @param content
     */
    @Override
    public void invoke(PlanetUserInfo data, AnalysisContext content) {
        System.out.println(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("All complete!");
    }
}
