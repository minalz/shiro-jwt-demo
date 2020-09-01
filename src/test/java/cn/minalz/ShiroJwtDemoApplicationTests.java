package cn.minalz;

import cn.minalz.dao.PermissionRepository;
import cn.minalz.dao.RoleRepository;
import cn.minalz.dao.UserRepository;
import cn.minalz.dto.TreeNode;
import cn.minalz.model.ScmciwhPermission;
import cn.minalz.model.ScmciwhRole;
import cn.minalz.model.ScmciwhUser;
import cn.minalz.service.ITreeService;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShiroJwtDemoApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private ITreeService treeService;

    @Test
    public void contextLoads() {
        System.out.println("ceshi");
    }

    @Test
    @Transactional
    public void test1(){
//        List<ScmciwhUserModel> all = scmciwhUserRepository.findAll();
//        Object o = JSON.toJSON(all);
        ScmciwhUser scmciwhUserModel = userRepository.findById(1l).get();
        Object o = JSON.toJSON(scmciwhUserModel);
        System.out.println(o);
        System.out.println("--------------");

    }

    @Test
    public void test2(){
        List<ScmciwhPermission> all = permissionRepository.findAll();
        Object o = JSON.toJSON(all);
        System.out.println(o);
    }

    @Test
    public void test3(){
        TreeNode treeById = treeService.getTreeById(6l);
        TreeNode treeById2 = treeService.getTreeById(7l);
        Object o = JSON.toJSON(treeById);
        Object o2 = JSON.toJSON(treeById2);
        System.out.println(o);
        System.out.println(o2);
    }

    @Test
    public void test4(){
        List<ScmciwhRole> all = roleRepository.findAll();
        Object o = JSON.toJSON(all);
        System.out.println(o);
    }

}
