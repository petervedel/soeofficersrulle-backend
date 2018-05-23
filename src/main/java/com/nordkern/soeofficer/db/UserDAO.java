package com.nordkern.soeofficer.db;

import com.google.inject.Inject;
import com.nordkern.soeofficer.api.User;
import com.nordkern.soeofficer.api.UserLogin;
import com.nordkern.soeofficer.api.UserSearch;
import com.nordkern.soeofficer.core.PasswordUtility;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mortenfrank on 24/11/2017.
 */
@Slf4j
public class UserDAO extends AbstractDAO<User> {

    @Inject
    public UserDAO(SessionFactory factory) {
        super(factory);
    }

    public User findById(Long id) {
        return get(id);
    }

    public User create(User user) throws Exception {
        user.setPassword(PasswordUtility.getSaltedHash(user.getPassword()));
        return persist(user);
    }

    public List<User> findByCriteria(UserSearch userSearch) {
        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        List<Predicate> conditionsList = new ArrayList<>();
        List<User> result = new ArrayList<>();

        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        Predicate username = builder.equal(root.get("username"),userSearch.getUsername());
        Predicate email = builder.equal(root.get("email"),userSearch.getEmail());

        if (userSearch.getUsername() == null && userSearch.getEmail() == null) {
            result = list(namedQuery("com.nordkern.soeofficer.api.User.findAll"));
            return result;
        }
        if (userSearch.getUsername() != null) {
            conditionsList.add(username);
        }
        if (userSearch.getEmail() != null) {
            conditionsList.add(email);
        }

        List<User> users = currentSession().createQuery(query.select(root).where(builder.and((conditionsList.toArray(new Predicate[]{}))))).getResultList();
        result = new ArrayList<>(users);

        return result;
    }

    public void update(User user) throws Exception {
        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
            user.setPassword(findById(user.getId()).getPassword());
        } else {
            user.setPassword(PasswordUtility.getSaltedHash(user.getPassword()));
        }
        currentSession().merge(user);
    }

    public void delete(Integer id) {
        User user = findById(id.longValue());
        currentSession().delete(user);
    }

    public User verifyPasswordForUser(UserLogin userLogin) throws Exception {
        List<Object> passwords = new ArrayList<>();
        if (userLogin.getUsername() != null) {
            Query q = currentSession().createNativeQuery("SELECT password FROM user WHERE username = :username");
            q.setParameter("username",userLogin.getUsername());
            passwords = q.getResultList();
        } else if (userLogin.getEmail() != null) {
            Query q = currentSession().createNativeQuery("SELECT password FROM user WHERE email = :email");
            q.setParameter("email",userLogin.getEmail());
            passwords = q.getResultList();
        } else {
            return null;
        }
        String storedPassword;
        if (passwords.size() == 0) {
            return null;
        } else {
            storedPassword = passwords.get(0).toString();
        }
        if (PasswordUtility.check(userLogin.getPassword(),storedPassword)) {
            return getUserByName(userLogin.getUsername());
        } else {
            return null;
        }
    }

    public User getUserByName(String username) {
        List<User> users = list(namedQuery("com.nordkern.soeofficer.api.User.gerUserByName").setParameter("username", username));

        if (users.size() == 0) {
            return null;
        } else {
            return users.get(0);
        }
    }

    public User getUserByEmail(String email) {
        List<User> users = list(namedQuery("com.nordkern.soeofficer.api.User.gerUserByEmail").setParameter("email", email));

        if (users.size() == 0) {
            return null;
        } else {
            return users.get(0);
        }
    }

    public boolean setUserLogin(UserLogin userLogin) throws Exception {
        User user;
        if (userLogin.getUsername() != null) {
            user = getUserByName(userLogin.getUsername());
        } else {
            user = getUserByEmail(userLogin.getEmail());
        }

        if (user == null) {
            return false;
        }

        String hashedPassword = PasswordUtility.getSaltedHash(userLogin.getPassword());
        namedQuery("com.nordkern.soeofficer.api.User.setPassword").setParameter("password",hashedPassword).setParameter("id",user.getId()).executeUpdate();

        return true;
    }

    public List<User> findAll() {
        return list(namedQuery("com.nordkern.soeofficer.api.User.findAll"));
    }
}
