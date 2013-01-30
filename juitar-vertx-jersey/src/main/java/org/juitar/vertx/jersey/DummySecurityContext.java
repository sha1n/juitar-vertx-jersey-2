package org.juitar.vertx.jersey;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * This is a dummy implementation of the {@link SecurityContext} interface. Just for the POC to work.
 *
 * @author sha1n
 * Date: 1/30/13
 */
public class DummySecurityContext implements SecurityContext {

    public static class DummyPrincipal implements Principal {

        private final String name;

        public DummyPrincipal(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DummyPrincipal that = (DummyPrincipal) o;

            if (!name.equals(that.name)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    private final Principal principal;

    public DummySecurityContext(Principal principal) {
        this.principal = principal;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }
}
