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
    public List<ScmciwhPermission> findRootPermission();

}
