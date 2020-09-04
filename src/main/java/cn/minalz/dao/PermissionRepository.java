package cn.minalz.dao;

import cn.minalz.model.ScmciwhPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @description:权限持久层类
 * @author: minalz
 * @date: 2020-07-24 23:39
 **/
public interface PermissionRepository extends JpaRepository<ScmciwhPermission,Long>, JpaSpecificationExecutor<ScmciwhPermission> {
    /**
     * 根据父id查询符合的权限数据
     * @param id
     * @return
     */
    List<ScmciwhPermission> findByParentId(Long id);

    /**
     * 根据path路径查找符合的权限数据
     * @param id
     * @return
     */
    List<ScmciwhPermission> findByPath(Long id);

}
