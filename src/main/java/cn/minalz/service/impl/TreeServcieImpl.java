package cn.minalz.service.impl;

import cn.minalz.dao.PermissionRepository;
import cn.minalz.dto.TreeNode;
import cn.minalz.model.ScmciwhPermission;
import cn.minalz.model.ScmciwhRole;
import cn.minalz.service.IPermissionService;
import cn.minalz.service.ITreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 生成权限树的工具类
 */
@Service
public class TreeServcieImpl implements ITreeService {

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public TreeNode getById(Long id) {
        Optional<ScmciwhPermission> byId = permissionRepository.findById(id);
        if (!byId.isPresent()) {
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
    public List<TreeNode> getTrees() {
        List<ScmciwhPermission> rootPermissions = permissionService.findRootPermission();
        List<TreeNode> treeNodes = new ArrayList<>();
        rootPermissions.forEach(x -> {
            TreeNode treeById = getTreeById(x.getId());
            treeNodes.add(treeById);
        });
        return treeNodes.stream().sorted(Comparator.comparing(TreeNode::getSort)).collect(Collectors.toList());
    }

    @Override
    public List<TreeNode> getChildrenById(Long id) {
//        List<Permission> permissions = permissionRepository.findByParentId(id);
        List<ScmciwhPermission> permissions = permissionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            // 如果path和ID相等，说明这是一个root节点
            // 那么条件就是id不等于传输进来的值 但是parentId得需要等于传进来的值
            predicates.add(criteriaBuilder.equal(root.get("parentId"), id));
            predicates.add(criteriaBuilder.notEqual(root.get("id"), id));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
        List<TreeNode> nodes = new ArrayList<>();
        permissions.forEach(x -> {
            TreeNode treeNode = new TreeNode();
            transferTreeNode(x, treeNode);
            nodes.add(treeNode);
        });
        return nodes.stream().sorted(Comparator.comparing(TreeNode::getSort)).collect(Collectors.toList());
    }

    @Override
    public TreeNode generateTreeNode(Long id) {
        // 根据节点id查询该节点信息
        TreeNode treeById = getById(id);
        // 根据该节点查询该节点的children节点
        List<TreeNode> children = getChildrenById(id);
        children.forEach(x -> {
            TreeNode treeNode = generateTreeNode(x.getId());
            treeById.getChildren().add(treeNode);
        });
        return treeById;
    }

    @Override
    public List<TreeNode> getTreeByRole(ScmciwhRole role) {
        Set<ScmciwhPermission> permissions = role.getPermissions();
        // 权限数据预处理
        Map<Long, List<ScmciwhPermission>> permissionMap = new HashMap<>();
        Set<Long> rootIds = new HashSet<>();
        List<TreeNode> treeNodes = new ArrayList<>();
        permissions.forEach(x -> {
            if (!permissionMap.containsKey(x.getParentId())) {
                permissionMap.put(x.getParentId(), new ArrayList<>());
            }
            permissionMap.get(x.getParentId()).add(x);
            // 根节点
            if (x.getId() == x.getParentId()) {
                rootIds.add(x.getId());
            }
            TreeNode treeNode = new TreeNode();
            transferTreeNode(x, treeNode);
            treeNodes.add(treeNode);
        });
        List<TreeNode> treeNodesList = transDepartment(treeNodes);
        return treeNodesList;
    }

    @Override
    public List<TreeNode> getTreeByRoles(Set<ScmciwhRole> roles) {
        Set<ScmciwhPermission> permissionSet = new HashSet<>();
        // 根据角色查询出来权限信息
        roles.forEach(x -> {
            Set<ScmciwhPermission> permissions = x.getPermissions();
            permissionSet.addAll(permissions);
        });
        // 权限数据预处理
        Map<Long, List<ScmciwhPermission>> permissionMap = new HashMap<>();
        Set<Long> rootIds = new HashSet<>();
        List<TreeNode> treeNodes = new ArrayList<>();
        permissionSet.forEach(x -> {
            if (!permissionMap.containsKey(x.getParentId())) {
                permissionMap.put(x.getParentId(), new ArrayList<>());
            }
            permissionMap.get(x.getParentId()).add(x);
            // 根节点
            if (x.getId() == x.getParentId()) {
                rootIds.add(x.getId());
            }
            TreeNode treeNode = new TreeNode();
            transferTreeNode(x, treeNode);
            treeNodes.add(treeNode);
        });
        List<TreeNode> treeNodesList = transDepartment(treeNodes);
        return treeNodesList;
    }

    private List<TreeNode> transDepartment(List<TreeNode> rootList) {
        List<TreeNode> nodeList = new ArrayList<>();
        for (TreeNode treeNode : rootList) {
            //表明是一级父类
            if (treeNode.getId() == treeNode.getPId()) {
                nodeList.add(treeNode);
                List<TreeNode> treeNodes = setChildren(treeNode.getId(), rootList);
                if (treeNodes != null) {
                    treeNode.getChildren().addAll(treeNodes);
                }
            }
        }
        return nodeList.stream().sorted(Comparator.comparing(TreeNode::getSort)).collect(Collectors.toList());
    }

    private List<TreeNode> setChildren(Long id, List<TreeNode> list) {
        List<TreeNode> childList = new ArrayList<>();
        for (TreeNode treeNode : list) {
            if (id == treeNode.getPId() && id != treeNode.getId()) {
                childList.add(treeNode);
            }
        }
        for (TreeNode treeNode : childList) {
            List<TreeNode> treeNodes = setChildren(treeNode.getId(), list);
            if (treeNodes != null) {
                treeNode.getChildren().addAll(treeNodes);
            }
        }
        if (childList.size() == 0) {
            return null;
        }
        // 根据sort升序排列
        return childList.stream().sorted(Comparator.comparing(TreeNode::getSort)).collect(Collectors.toList());
    }

    /**
     * Permission属性转TreeNode属性
     *
     * @return
     */
    private void transferTreeNode(ScmciwhPermission permission, TreeNode treeNode) {
        if (permission == null) {
            return;
        }
        treeNode.setId(permission.getId());
        treeNode.setName(permission.getPermissionName());
        treeNode.setPId(permission.getParentId());
        treeNode.setSort(permission.getSort());
    }
}
