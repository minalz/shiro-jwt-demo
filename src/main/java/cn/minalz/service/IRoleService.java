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
}
