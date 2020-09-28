package cn.minalz.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@DisallowConcurrentExecution //作业不并发
@Component
public class HelloWorldJob implements Job {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println("欢迎使用minalblog这是一个定时任务  --minalz!"+ sdf.format(new Date()));

    }

}