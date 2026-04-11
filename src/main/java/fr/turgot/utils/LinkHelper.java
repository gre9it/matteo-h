package fr.turgot.utils;

/**
 * Utilitaire centralisant les chemins vers les vues JSP protégées.
 */
public class LinkHelper {

    private static final String JSP_BASE = "/WEB-INF/jsp/";

    public static final String LOGIN_VIEW       = JSP_BASE + "login.jsp";
    public static final String REGISTER_VIEW    = JSP_BASE + "register.jsp";
    public static final String REGISTER_SUCCESS = JSP_BASE + "register-success.jsp";
    public static final String CATALOGUE_VIEW   = JSP_BASE + "catalogue.jsp";
    public static final String DASHBOARD_VIEW   = JSP_BASE + "dashboard.jsp";
    public static final String RESSOURCE_FORM   = JSP_BASE + "ressource-form.jsp";
    public static final String ERROR_VIEW       = JSP_BASE + "error.jsp";

    private LinkHelper() {}
}
