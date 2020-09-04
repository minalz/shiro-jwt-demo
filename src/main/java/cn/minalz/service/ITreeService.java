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
     * @param id
     * @return
     */
    public TreeNode getById(Long id);

    /**
     * 根据权限id获取对应的权限数据(单树)
     * @param id
     * @return
     */
    public TreeNode getTreeById(Long id);

    /**
     * 根据权限的根节点查询对应的权限数据(可能是多树，如果是多个根节点的话)
     * @return
     */
    public List<TreeNode> getTrees();

    /**
     * 根据父id获取对应的子node
     * @param id
     * @return
     */
    public List<TreeNode> getChildrenById(Long id);

    /**
     * 根据父id生成树结构(单树)
     * @param id
     * @return
     */
    public TreeNode generateTreeNode(Long id);

    /**
     * 根据单独的一个角色查询对应的树节点
     */
    public List<TreeNode> getTreeByRole(ScmciwhRole role);

    /**
     * 根据角色集合查询对应的树节点
     */
    public List<TreeNode> getTreeByRoles(Set<ScmciwhRole> roles);

}
