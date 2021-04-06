package cn.minalz.dao;

import cn.minalz.model.ScmciwhQuartzTaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @description: 定时任务查询接口
 * @author: minalz
 * @date: 2020-07-24 23:39
 **/
public interface QuartzRepository extends JpaRepository<ScmciwhQuartzTaskModel, Integer>, JpaSpecificationExecutor<ScmciwhQuartzTaskModel> {

    /**
     * 根据ID集合查询数据
     * @param ids
     * @return
     */
    List<ScmciwhQuartzTaskModel> findByIdIn(Integer[] ids);
}
