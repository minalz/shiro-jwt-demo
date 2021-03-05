package cn.minalz.controller;

import cn.minalz.common.ResponseData;
import cn.minalz.model.ScmciwhRole;
import cn.minalz.service.IRoleService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色管理控制层
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/api/role")
@Slf4j
public class RoleController {

    @Autowired
    private IRoleService roleServiceImpl;

    /**
     * 查询所有角色信息
     *
     * @return
     */
    @ApiOperation(value = "查询所有角色信息")
    @PostMapping("/list")
    public ResponseData list() {
        ResponseData responseData = new ResponseData();
        List<ScmciwhRole> roles = roleServiceImpl.findAll();
        responseData.list = roles;
        Object o = JSON.toJSON(roles);
        log.info("权限列表 ==> {}", o);
        return responseData;
    }
}
