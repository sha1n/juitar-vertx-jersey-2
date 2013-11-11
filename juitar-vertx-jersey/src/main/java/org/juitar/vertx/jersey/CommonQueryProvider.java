package org.juitar.vertx.jersey;

import javax.ws.rs.QueryParam;

/**
 * @author sha1n
 * Date: 9/11/13
 */
public class CommonQueryProvider {

    private String layout = "";
    private String filter = "";
    private String order = "";
    private String skip = "";
    private String size = "";

    @QueryParam("order")
    public void setOrder(String order) {
        this.order = order;
    }

    @QueryParam("size")
    public void setSize(String size) {
        this.size = size;
    }

    @QueryParam("skip")
    public void setSkip(String skip) {
        this.skip = skip;
    }

    @QueryParam("filter")
    public void setFilter(String filter) {
        this.filter = filter;
    }

    @QueryParam("layout")
    public void setLayout(String layout) {
        this.layout = layout;
    }

    @Override
    public String toString() {
        return "CommonQueryProvider{" +
                "layout='" + layout + '\'' +
                ", filter='" + filter + '\'' +
                ", order='" + order + '\'' +
                ", skip='" + skip + '\'' +
                ", size='" + size + '\'' +
                '}';
    }

}
