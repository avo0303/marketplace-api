package com.andrewsha.marketplace.domain.user.permission;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.andrewsha.marketplace.exception.UserServiceException;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PermissionSetRepository userPermissionRepository;

    public Permission findPermission(Long id) {
        return this.permissionRepository.findById(id).orElseThrow(
                () -> new UserServiceException("permission with id " + id + " does not exists"));
    }
    
    public Permission findPermissionByName(String name) {
        return this.permissionRepository.findByName(name).orElseThrow(
                () -> new UserServiceException("permission with name " + name + " does not exists"));
    }

    public PermissionSet findPermissionSet(PermissionSet set) {
        return this.userPermissionRepository.findOne(Example.of(set)).orElseThrow(
                () -> new UserServiceException("given permission set does not exists in database"));
    }

    public Set<Permission> getPermissions(Set<PermissionEnum> actions,
            Class<? extends Object> type) {
        Set<Permission> permissions = new HashSet<>();
        for (PermissionEnum action : actions) {
            Permission permission = new Permission(action, type);
            Optional<Permission> optional =
                    this.permissionRepository.findByName(permission.getName());
            if (!optional.isPresent()) {
                permissions.add(this.permissionRepository.save(permission));
            } else {
                permissions.add(optional.get());
            }
        }
        return permissions;
    }

    public PermissionSet save(PermissionSet permissionSet) {
        return this.userPermissionRepository.save(permissionSet);
    }

//    public PermissionSet getPermissionSet(String scope, Set<Permission> permissions) {
//        List<PermissionSet> userPermissionsList = this.userPermissionRepository.findByScope(scope);
//        if (!userPermissionsList.isEmpty()) {
//            for (PermissionSet set : userPermissionsList) {
//                if (set.getPermissions().equals(permissions)) {
//                    return set;
//                }
//            }
//        }
//        PermissionSet newSet = new PermissionSet();
//        newSet.setScope(scope);
//        newSet.setPermissions(permissions);
//        return this.userPermissionRepository.save(newSet);
//    }
}
