package cn.minalz.service;

import cn.minalz.model.ScmciwhPermission;

import java.util.List;

/**
 * 权限表接口
 */
public interface IPermissionService {

    /**
     * 查询菜单的根节点 id = parent_id
     */
    List<ScmciwhPermission> findRootPermission();

    /**
     * 根据权限实体增加/更新
     *
     * @param permission
     */
    ScmciwhPermission savePermission(ScmciwhPermission permission);

    /**
     * 根据权限ID删除对应的单条数据
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据权限ID查询对应的单条数据
     *
     * @param id
     * @return
     */
    ScmciwhPermission findById(Long id);


}
