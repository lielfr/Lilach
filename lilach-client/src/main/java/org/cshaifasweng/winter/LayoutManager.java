package org.cshaifasweng.winter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Pair;

import java.io.IOException;

public class LayoutManager {

    private static LayoutManager instance;

    private LayoutManager() {
    }

    public static LayoutManager getInstance() {
        if (instance == null) {
            instance = new LayoutManager();
        }
        return instance;
    }

    public Pair<Parent, Object> getFXML(String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(LayoutManager.class.getResource(name + ".fxml"));
        Parent ret = loader.load();
//        if (!controllers.containsKey(name))
//            controllers.put(name, new PriorityQueue<>());
//        controllers.get(name).add(loader.getController());
        Object controller = loader.getController();
        return new Pair<>(ret, controller);
    }
}
