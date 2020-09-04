package cn.minalz;

import cn.minalz.common.ResponseData;
import cn.minalz.config.redis.RedisConstant;
import cn.minalz.dao.PermissionRepository;
import cn.minalz.dao.RoleRepository;
import cn.minalz.dao.UserRepository;
import cn.minalz.dto.TreeNode;
import cn.minalz.dto.UserRedisToken;
import cn.minalz.model.ScmciwhPermission;
import cn.minalz.model.ScmciwhRole;
import cn.minalz.model.ScmciwhUser;
import cn.minalz.service.IPermissionService;
import cn.minalz.service.ITreeService;
import cn.minalz.utils.RedisUtil;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShiroJwtDemoApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private ITreeService treeService;
    @Autowired
    private RedisUtil redisUtil;

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

    /**
     * 查询所有权限数据(所有根节点的权限数据，多数)
     */
    @Test
    public void test3(){
        ResponseData responseData = new ResponseData();
        List<TreeNode> treeNodes = treeService.getTrees();
        responseData.list = treeNodes;
        Object o = JSON.toJSON(treeNodes);
        System.out.println("o -- " + o);
    }

    @Test
    public void test4(){
        List<ScmciwhRole> all = roleRepository.findAll();
        Object o = JSON.toJSON(all);
        System.out.println(o);
    }

    @Test
    public void test5(){
        UserRedisToken userRedisToken1 = new UserRedisToken("token1",new Date().getTime());
        redisUtil.set(RedisConstant.REDIS_STORAGE_USERNAME_PREFIX+"cjgly" + "_" + "token1",userRedisToken1,600);
        UserRedisToken userRedisToken2 = new UserRedisToken("token2",new Date().getTime());
        redisUtil.set(RedisConstant.REDIS_STORAGE_USERNAME_PREFIX+"cjgly" + "_" + "token2",userRedisToken2,600);
        UserRedisToken userRedisToken3 = new UserRedisToken("token3",new Date().getTime());
        redisUtil.set(RedisConstant.REDIS_STORAGE_USERNAME_PREFIX+"cjgly" + "_" + "token3",userRedisToken3,600);
        UserRedisToken userRedisToken4 = new UserRedisToken("token4",new Date().getTime());
        redisUtil.set(RedisConstant.REDIS_STORAGE_USERNAME_PREFIX+"cjgly" + "_" + "token4",userRedisToken4,600);
        UserRedisToken userRedisToken5 = new UserRedisToken("token5",new Date().getTime());
        redisUtil.set(RedisConstant.REDIS_STORAGE_USERNAME_PREFIX+"cjgly" + "_" + "token5",userRedisToken5,600);
        Set<String> keys1 = redisUtil.keys(RedisConstant.REDIS_STORAGE_USERNAME_PREFIX + "normal" + "_" + "*");
        System.out.println("keys1.size() -- " + keys1.size());
        Set<String> keys2 = redisUtil.keys(RedisConstant.REDIS_STORAGE_USERNAME_PREFIX + "cjgly" + "_" + "*");
        List<UserRedisToken> uList = new ArrayList<>();
        if(keys2 != null && keys2.size() >= 5){
            // 对redis中的key进行监控 判断还有多少用户登录
            keys2.forEach(x -> {
                Object o = redisUtil.get(x);
                if(o != null){
                    UserRedisToken u = JSON.parseObject(o.toString(), UserRedisToken.class);
                    System.out.println("u -- " + u);
                    uList.add(u);
                }
            });
            // 默认按照生序排列 然后取出第一个符合的元素 进行删除(踢出)
            Optional<UserRedisToken> first = uList.stream().sorted(Comparator.comparing(UserRedisToken::getTimestamp)).findFirst();
            if(first.isPresent()){
                String token = first.get().getToken();
                redisUtil.del(token);
                System.out.println("踢出token -- " + token);
            }
        }
        Long count = redisUtil.count(RedisConstant.REDIS_STORAGE_USERNAME_PREFIX + "cjgly" + "_" + "*");
        System.out.println("count -- " + count);

    }

    @Test
    public void test6(){
        List<ScmciwhPermission> rootPermission = permissionService.findRootPermission();
        Object o = JSON.toJSON(rootPermission);
        System.out.println("o -- " + o);
    }

    @Test
    public void test7(){
        ScmciwhPermission permission = permissionService.findById(12l);
        permission.setPath(permission.getPath()+"test");
        permissionService.savePermission(permission);
//        permissionService.deleteById(12l);
    }

}
