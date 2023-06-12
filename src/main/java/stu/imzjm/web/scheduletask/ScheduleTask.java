package stu.imzjm.web.scheduletask;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import stu.imzjm.dao.StatisticMapper;
import stu.imzjm.service.MailService;

@Component
public class ScheduleTask {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private StatisticMapper statisticMapper;

    @Resource
    private MailService mailService;

    @Scheduled(cron = "0 0 12 1 * ?")
    //@Scheduled(cron = "0 */3 * * * ?")      //测试, 没隔 3 分钟执行一次调度任务
    public void sendEmail() {
        long totalVisit = statisticMapper.getTotalVisit();
        long totalComment = statisticMapper.getTotalComment();

        String content =
                "博客系统总访问量为: " + totalVisit + " 人次" + "\n" +
                        "博客系统总评论数为: " + totalComment + " 条" + "\n";
        mailService.sendSimpleEmail(null, "个人博客系统流量统计情况", content);
        logger.info("统计邮件发送成功!");
    }

}
