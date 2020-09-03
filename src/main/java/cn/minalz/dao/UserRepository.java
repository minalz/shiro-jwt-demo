package cn.minalz.dao;

import cn.minalz.model.ScmciwhUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: minalz
 * @date: 2020-07-24 23:39
 **/
public interface UserRepository extends JpaRepository<ScmciwhUser,Long>, JpaSpecificationExecutor<ScmciwhUser> {
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    Optional<ScmciwhUser> findTopByUsername(String username);

    /**
     * 查询所有对应的角色
     * @param username
     * @return
     */
    List<ScmciwhUser> findRolesByUsername(String username);

    /**
     * 根据用户ID查询对应的用户信息
     */
    Optional<ScmciwhUser> findTopById(Long id);

}
