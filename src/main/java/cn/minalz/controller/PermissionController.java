package cn.minalz.controller;

import cn.minalz.common.ResponseData;
import cn.minalz.dto.TreeNode;
import cn.minalz.model.ScmciwhPermission;
import cn.minalz.model.ScmciwhRole;
import cn.minalz.model.ScmciwhUser;
import cn.minalz.service.IPermissionService;
import cn.minalz.service.ITreeService;
import cn.minalz.service.IUserService;
import cn.minalz.utils.BaseUtils;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 菜单管理控制层
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/api/permission")
@Slf4j
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITreeService treeService;

    /**
     * 查询所有权限信息
     * @return
     */
    @ApiOperation(value = "查询所有权限信息")
    @PostMapping("/list")
    public ResponseData list(){
        ResponseData responseData = new ResponseData();
        // PC权限
        TreeNode byId6 = treeService.getTreeById(6L);
        // PDA权限
        TreeNode byId7 = treeService.getTreeById(7L);
        TreeNode treeNode = new TreeNode();
        treeNode.getChildren().add(byId6);
        treeNode.getChildren().add(byId7);
        responseData.list.add(treeNode);
        Object o = JSON.toJSON(treeNode);
        log.info("o ==> {}", o);
        return responseData;
    }

    /**
     * 根据传入的权限Id查询权限信息(包括自身)
     * @return
     */
    @ApiOperation(value = "根据ID查询权限信息")
    @PostMapping("/list/{id}")
    public ResponseData listById(@PathVariable("id")Long id){
        ResponseData responseData = new ResponseData();
        TreeNode treeNode = treeService.getTreeById(id);
        responseData.list.add(treeNode);
        Object o = JSON.toJSON(treeNode);
        log.info("o ==> {}", o);
        return responseData;
    }

    /**
     * 根据当前已登录的用户，查询对应的菜单权限
     * @return
     */
    @PostMapping("/list/user")
    public ResponseData listByUser(){
        // 查询当前用户的ID
        Long id = BaseUtils.getCurrentId();
        // 根据用户ID查询对应的用户信息
        ScmciwhUser user = userService.findById(id);
        // 查询所有的角色
        Set<ScmciwhRole> roles = user.getRoles();
        Set<ScmciwhPermission> permissionSet = new HashSet<>();
        // 根据角色查询出来权限信息
        roles.forEach(x -> {
            Set<ScmciwhPermission> permissions = x.getPermissions();
            permissionSet.addAll(permissions);
        });
        // 权限数据预处理
        Map<Long, List<ScmciwhPermission>> permissionMap = new HashMap<>();
        Set<Long> rootIds = new HashSet<>();
        Set<TreeNode> treeNodes = new HashSet<>();
        permissionSet.forEach(x -> {
            if(!permissionMap.containsKey(x.getParentId())){
                permissionMap.put(x.getParentId(), new ArrayList<>());
            }
            permissionMap.get(x.getParentId()).add(x);
            // 根节点
            if(x.getId() == x.getParentId()){
                rootIds.add(x.getId());
            }
            TreeNode treeNode = new TreeNode();
            transferTreeNode(x,treeNode);
            treeNodes.add(treeNode);
        });

        Set<TreeNode> treeNodes1 = transDepartment(treeNodes);
        System.out.println(treeNodes1);

        ResponseData responseData = new ResponseData();
        TreeNode treeNode = treeService.getTreeById(id);
        responseData.list.add(treeNode);
        Object o = JSON.toJSON(treeNode);
        log.info("o ==> {}", o);
        return responseData;
    }

    public Set<TreeNode> transDepartment(Set<TreeNode> rootList){
        Set<TreeNode> nodeList = new HashSet<>();
        for (TreeNode treeNode : rootList) {
            if (treeNode.getId()==treeNode.getPId()){//表明是一级父类
                nodeList.add(treeNode);
            }
            treeNode.getChildren().addAll(setChildren(treeNode.getId(),rootList));
        }
        return nodeList;
    }
    public Set<TreeNode> setChildren(Long id, Set<TreeNode> list ){
        Set<TreeNode> childList = new HashSet<>();
        for (TreeNode treeNode : list) {
            if (id == treeNode.getPId()){
                childList.add(treeNode);
            }
        }
        for (TreeNode treeNode : childList) {
            treeNode.getChildren().addAll(setChildren(treeNode.getId(),list));
        }
        if (childList.size()==0){
            return null;
        }
        return childList;
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
