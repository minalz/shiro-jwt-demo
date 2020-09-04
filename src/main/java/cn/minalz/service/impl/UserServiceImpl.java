package cn.minalz.service.impl;

import cn.minalz.dao.UserRepository;
import cn.minalz.model.ScmciwhUser;
import cn.minalz.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户管理业务类
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ScmciwhUser savePermission(ScmciwhUser user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public ScmciwhUser findById(Long id) {
        Optional<ScmciwhUser> byId = userRepository.findById(id);
        if(byId.isPresent()){
            return byId.get();
        }
        log.error("没有用户ID为{}的信息", id);
        return null;
    }
}
