package org.quarkus.model;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.*;
import org.quarkus.model.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wildfly.security.password.PasswordFactory;
import org.wildfly.security.password.WildFlyElytronPasswordProvider;
import org.wildfly.security.password.interfaces.BCryptPassword;
import org.wildfly.security.password.util.ModularCrypt;

import javax.persistence.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Entity
@Table(name = "users_")
@UserDefinition
public class User extends PanacheEntity
{
    @Username
    public String username;

    @Password
    public String password;

    @Roles
    public String role;

    private static final Logger LOG = LoggerFactory.getLogger(User.class);

    public static User findByUsername(final String username){
        return find("username", username).firstResult();
    }

    public static User add(final UserDTO dto) {
        var user = new User();
        user.username = dto.getUsername();
        user.password = BcryptUtil.bcryptHash(dto.getPassword());
        user.role = dto.getRole();
        user.persist();
        return user;
    }

    public static boolean verifyPassword(String bCryptPasswordHash, String passwordToVerify) {

        try {
            var provider = new WildFlyElytronPasswordProvider();
            var passwordFactory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT, provider);
            var userPasswordDecoded = ModularCrypt.decode(bCryptPasswordHash);
            var userPasswordRestored = passwordFactory.translate(userPasswordDecoded);
            return passwordFactory.verify(userPasswordRestored, passwordToVerify.toCharArray());

        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException e ) {
            LOG.error("Verification failed!", e);
        }
        return false;
    }
}
