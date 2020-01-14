package org.cshaifasweng.winter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LayoutManager {

    private static LayoutManager instance;
    private Map<String, Parent> layouts;
    private Map<String, Object> controllers;

    private LayoutManager() {
        layouts = new HashMap<>();
        controllers = new HashMap<>();
    }

    public static LayoutManager getInstance() {
        if (instance == null) {
            instance = new LayoutManager();
        }
        return instance;
    }

    private Parent loadFXML(String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(LayoutManager.class.getResource(name + ".fxml"));
        Parent ret = loader.load();
        controllers.put(name, loader.getController());
        return ret;
    }

    public Parent getFXML(String name) throws IOException {
        if (!layouts.containsKey(name)) {
            layouts.put(name, loadFXML(name));
        }
        return layouts.get(name);
    }

    public Object getController(String name) throws IOException {
        if (!controllers.containsKey(name))
            getFXML(name);
        return controllers.get(name);
    }
}
