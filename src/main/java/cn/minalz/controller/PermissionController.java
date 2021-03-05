package cn.minalz.controller;

import cn.minalz.common.ResponseData;
import cn.minalz.dto.TreeNode;
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

import java.util.List;
import java.util.Set;

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
     *
     * @return
     */
    @ApiOperation(value = "查询所有权限信息")
    @PostMapping("/list")
    public ResponseData list() {
        ResponseData responseData = new ResponseData();
        List<TreeNode> treeNodes = treeService.getTrees();
        responseData.list = treeNodes;
        Object o = JSON.toJSON(treeNodes);
        log.info("o ==> {}", o);
        return responseData;
    }

    /**
     * 根据传入的权限Id查询权限信息(包括自身)
     *
     * @return
     */
    @ApiOperation(value = "根据ID查询权限信息")
    @PostMapping("/list/{id}")
    public ResponseData listById(@PathVariable("id") Long id) {
        ResponseData responseData = new ResponseData();
        TreeNode treeNode = treeService.getTreeById(id);
        responseData.list.add(treeNode);
        Object o = JSON.toJSON(treeNode);
        log.info("o ==> {}", o);
        return responseData;
    }

    /**
     * 根据当前已登录的用户，查询对应的菜单权限
     *
     * @return
     */
    @PostMapping("/list/user")
    public ResponseData listByUser() {
        // 查询当前用户的ID
        Long id = BaseUtils.getCurrentId();
        // 根据用户ID查询对应的用户信息
        ScmciwhUser user = userService.findById(id);
        // 查询所有的角色
        Set<ScmciwhRole> roles = user.getRoles();
        List<TreeNode> treeNodesSet = treeService.getTreeByRoles(roles);

        ResponseData responseData = new ResponseData();
        responseData.list.addAll(treeNodesSet);
        Object o = JSON.toJSON(treeNodesSet);
        log.info("treeNodesSet ==> {}", o);
        return responseData;
    }

}
