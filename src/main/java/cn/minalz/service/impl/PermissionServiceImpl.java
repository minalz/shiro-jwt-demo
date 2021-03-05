package cn.minalz.service.impl;

import cn.minalz.dao.PermissionRepository;
import cn.minalz.model.ScmciwhPermission;
import cn.minalz.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * 权限业务实现类
 */
@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    /**
     * 查询菜单的根节点 id = parent_id
     */
    @Override
    public List<ScmciwhPermission> findRootPermission() {
        List<ScmciwhPermission> permissions = permissionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            // 如果path和ID相等，说明这是一个root节点
            predicates.add(criteriaBuilder.equal(root.get("id"), root.get("parentId")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        return permissions;
    }

    @Override
    public ScmciwhPermission savePermission(ScmciwhPermission permission) {
        // 没有ID就是新增  有ID就是更新
        return permissionRepository.save(permission);
    }

    @Override
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public ScmciwhPermission findById(Long id) {
        Optional<ScmciwhPermission> byId = permissionRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }


}
