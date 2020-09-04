package cn.minalz.service;

import cn.minalz.model.ScmciwhRole;

import java.util.List;

/**
 * 角色管理接口
 */
public interface IRoleService {

    /**
     * 查询所有的角色
     * @return
     */
    List<ScmciwhRole> findAll();

    /**
     * 根据角色实体增加/更新
     * @param role
     */
    public ScmciwhRole savePermission(ScmciwhRole role);

    /**
     * 根据角色ID删除对应的单条数据
     * @param id
     */
    public void deleteById(Long id);

    /**
     * 根据角色ID查询对应的单条数据
     * @param id
     * @return
     */
    public ScmciwhRole findById(Long id);
}
