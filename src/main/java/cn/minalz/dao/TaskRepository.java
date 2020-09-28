package cn.minalz.dao;

import cn.minalz.model.SysTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @description: 定时任务查询接口
 * @author: minalz
 * @date: 2020-07-24 23:39
 **/
public interface TaskRepository extends JpaRepository<SysTask,Long>, JpaSpecificationExecutor<SysTask> {

}
