package org.cshaifasweng.winter.events;

public class DashboardSwitchEvent {
    public final String pageName;

    public DashboardSwitchEvent(String pageName) {
        this.pageName = pageName;
    }
}
