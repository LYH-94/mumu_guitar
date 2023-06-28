package controllers;

import javax.servlet.http.HttpServletRequest;

public interface TrolleyController {
    public String getTrolleyByUserId(HttpServletRequest req);
}
