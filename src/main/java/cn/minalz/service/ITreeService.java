package cn.minalz.service;

import cn.minalz.dto.TreeNode;
import cn.minalz.model.ScmciwhRole;

import java.util.List;
import java.util.Set;

/**
 * 查询权限树的接口
 */
public interface ITreeService {

    /**
     * 根据权限id获取对应的TreeNode
     *
     * @param id
     * @return
     */
    TreeNode getById(Long id);

    /**
     * 根据权限id获取对应的权限数据(单树)
     *
     * @param id
     * @return
     */
    TreeNode getTreeById(Long id);

    /**
     * 根据权限的根节点查询对应的权限数据(可能是多树，如果是多个根节点的话)
     *
     * @return
     */
    List<TreeNode> getTrees();

    /**
     * 根据父id获取对应的子node
     *
     * @param id
     * @return
     */
    List<TreeNode> getChildrenById(Long id);

    /**
     * 根据父id生成树结构(单树)
     *
     * @param id
     * @return
     */
    TreeNode generateTreeNode(Long id);

    /**
     * 根据单独的一个角色查询对应的树节点
     */
    List<TreeNode> getTreeByRole(ScmciwhRole role);

    /**
     * 根据角色集合查询对应的树节点
     */
    List<TreeNode> getTreeByRoles(Set<ScmciwhRole> roles);

}
