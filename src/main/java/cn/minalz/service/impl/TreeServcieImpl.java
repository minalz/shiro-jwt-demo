package cn.minalz.service.impl;

import cn.minalz.dao.PermissionRepository;
import cn.minalz.dto.TreeNode;
import cn.minalz.model.ScmciwhPermission;
import cn.minalz.service.ITreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * 生成权限树的工具类
 */
@Service
public class TreeServcieImpl implements ITreeService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public TreeNode getById(Long id){
        Optional<ScmciwhPermission> byId = permissionRepository.findById(id);
        if(!byId.isPresent()){
            return null;
        }
        ScmciwhPermission permission = byId.get();
        TreeNode treeNode = new TreeNode();
        transferTreeNode(permission, treeNode);
        return treeNode;
    }

    @Override
    public TreeNode getTreeById(Long id) {
        TreeNode treeNode = generateTreeNode(id);
        return treeNode;
    }

    @Override
    public List<TreeNode> getChildrenById(Long id) {
//        List<Permission> permissions = permissionRepository.findByParentId(id);
        List<ScmciwhPermission> permissions = permissionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            // 如果path和ID相等，说明这是一个root节点
            // 那么条件就是id不等于传输进来的值 但是parentI的需要等于传进来的值
            predicates.add(criteriaBuilder.equal(root.get("parentId"), id));
            predicates.add(criteriaBuilder.notEqual(root.get("id"), id ));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        List<TreeNode> nodes = new ArrayList<>();
        permissions.forEach(x -> {
            TreeNode treeNode = new TreeNode();
            transferTreeNode(x, treeNode);
            nodes.add(treeNode);
        });
        return nodes;
    }

    @Override
    public TreeNode generateTreeNode(Long id){
        // 根据节点id查询该节点信息
        TreeNode treeById = getById(id);
        // 根据该节点查询该节点的children节点
        List<TreeNode> children = getChildrenById(id);
        children.forEach(x -> {
            TreeNode treeNode = generateTreeNode(x.getId());
            treeById.getChildren().add(treeNode);
        });
//        treeById.setChildren(children);
        return treeById;
    }

    /**
     * Permission属性转TreeNode属性
     * @return
     */
    private void transferTreeNode(ScmciwhPermission permission, TreeNode treeNode){
        if(permission == null)
            return;
        treeNode.setId(permission.getId());
        treeNode.setName(permission.getPermissionName());
        treeNode.setPId(permission.getParentId());
        treeNode.setSort(permission.getSort());
    }
}
