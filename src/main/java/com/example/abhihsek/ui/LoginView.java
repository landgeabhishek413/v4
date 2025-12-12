package com.example.abhihsek.ui;

import com.example.abhihsek.entity.User;
import com.example.abhihsek.service.InsuranceService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("")
public class LoginView extends VerticalLayout {

    public LoginView(InsuranceService service) {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        H1 title = new H1("Login");
        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        Button loginButton = new Button("Login", e -> {
            User user = service.authenticate(username.getValue(), password.getValue());
            if (user != null) {
                // Store user in session
                VaadinSession.getCurrent().setAttribute(User.class, user);
                UI.getCurrent().navigate("dashboard");
            } else {
                Notification.show("Invalid credentials");
            }
        });

        add(title, username, password, loginButton);
    }
}
