package fr.turgot.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LinkHelper {

    public static final String JSP_ROOT = "/WEB-INF/jsp/";

    /**
     * Génère le lien vers une JSP dans WEB-INF/jsp ou ses sous-dossiers
     */
    public static String jspLink(String relativePath) {
        return "page?path=" + relativePath; // relativePath = authentification/login
    }

    /**
     * Liste toutes les JSP dans un dossier et ses sous-dossiers
     */
    public static List<String> listAllJSP(String webappRoot) {
        List<String> pages = new ArrayList<>();
        File rootDir = new File(webappRoot + JSP_ROOT);
        scanDirectory(rootDir, "", pages);
        return pages;
    }

    private static void scanDirectory(File dir, String pathPrefix, List<String> pages) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scanDirectory(file, pathPrefix + file.getName() + "/", pages);
            } else if (file.getName().endsWith(".jsp")) {
                String name = file.getName().replace(".jsp", "");
                pages.add(pathPrefix + name); // ex: authentification/login
            }
        }
    }
}
