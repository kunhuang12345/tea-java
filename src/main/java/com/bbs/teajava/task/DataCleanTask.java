package com.bbs.teajava.task;

import com.bbs.teajava.service.IPapersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author kunhuang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataCleanTask {

    private final IPapersService paperService;

    /**
     * 设置定时清理论文标记为删除的任务
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void cleanDeletedData() {
        try {
            paperService.cleanDeletedData();
            log.info("定时清理任务执行完成");
        } catch (Exception e) {
            log.error("定时清理任务执行失败", e);
        }
    }

    /**
     * 定时任务测试
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void cleanDeletedDataTest() {
        try {
            paperService.cleanDeletedData();
            log.info("定时清理任务执行完成");
        } catch (Exception e) {
            log.error("定时清理任务执行失败", e);
        }
    }

}
