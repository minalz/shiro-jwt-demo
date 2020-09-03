package cn.minalz.service;

import cn.minalz.model.ScmciwhUser;

/**
 * 用户管理接口
 */
public interface IUserService {
    ScmciwhUser findById(Long id);
}
