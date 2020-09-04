package cn.minalz.service;

import cn.minalz.model.ScmciwhUser;

/**
 * 用户管理接口
 */
public interface IUserService {

    /**
     * 根据角色实体增加/更新
     * @param user
     */
    public ScmciwhUser savePermission(ScmciwhUser user);

    /**
     * 根据角色ID删除对应的单条数据
     * @param id
     */
    public void deleteById(Long id);

    /**
     * 根据用户ID查询对应的用户数据
     * @param id
     * @return
     */
    public ScmciwhUser findById(Long id);

}
