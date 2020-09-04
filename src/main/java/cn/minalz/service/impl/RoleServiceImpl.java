package cn.minalz.service.impl;

import cn.minalz.dao.RoleRepository;
import cn.minalz.model.ScmciwhRole;
import cn.minalz.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 角色管理业务类
 */
@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<ScmciwhRole> findAll() {
        List<ScmciwhRole> roles = roleRepository.findAll();
        return roles;
    }

    @Override
    public ScmciwhRole saveRole(ScmciwhRole role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public ScmciwhRole findById(Long id) {
        Optional<ScmciwhRole> byId = roleRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }
}
