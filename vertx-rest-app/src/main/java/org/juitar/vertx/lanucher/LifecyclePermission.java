package org.juitar.vertx.lanucher;

import java.security.BasicPermission;

/**
 * @author sha1n
 * Date: 11/16/13
 */
public class LifecyclePermission extends BasicPermission implements ApplicationPermission {

    public LifecyclePermission(String name) {
        super(name);
    }
}
