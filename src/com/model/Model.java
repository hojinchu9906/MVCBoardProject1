package com.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by cjw on 2016-03-31.
 */
public interface Model {
    public String handlerRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception;
}
