package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.events.LoginEvent;
import org.cshaifasweng.winter.events.TokenSetEvent;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.logging.Logger;

public class LoginScreenController implements Refreshable {

    protected static final Logger log = Logger.getLogger(LoginScreenController.class.getName());

    public LoginScreenController() {
        LoggerUtils.setupLogger(log);
    }

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private void performLogin() {
        LilachService service = APIAccess.getService();
        service.login(username.getText(), password.getText()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                log.finest("Got response code: " + response.code());
                if (response.code() == 200) {
                    log.finest("Got token");
                    String token = response.headers().get("Authorization")
                            .replace("Bearer ", "");
                    Platform.runLater(() -> {
                        EventBus.getDefault().post(new TokenSetEvent(token));
                    });

                    log.finest("Trying to get current user.");
                    service.getCurrentUser().enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            log.finest("GetCurrentUser: " + response.code());
                            if (response.code() != 200)
                                return;
                            log.finest("Got user: " + response.body());
                            APIAccess.setCurrentUser(response.body());
                            // Notify the dashboard
                            Platform.runLater(() -> {
                                EventBus.getDefault().post(new LoginEvent());
                            });

                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable throwable) {
                            log.finest("getCurrentUser failed.");
                            throwable.printStackTrace();
                        }
                    });
                    Platform.runLater(() -> {
                        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
                    });

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {

            }
        });
    }

    @Override
    public void refresh() {

    }
}
