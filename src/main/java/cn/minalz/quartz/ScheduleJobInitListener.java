package cn.minalz.quartz;

import cn.minalz.service.IQuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class ScheduleJobInitListener implements CommandLineRunner {

    @Autowired
    IQuartzService quartzService;

    @Override
    public void run(String... arg0) throws Exception {
        try {
            quartzService.initSchedule();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}