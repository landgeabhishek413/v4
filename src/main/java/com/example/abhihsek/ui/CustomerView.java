package com.example.abhihsek.ui;

import com.example.abhihsek.entity.User;
import com.example.abhihsek.service.InsuranceService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "customers", layout = MainLayout.class)
public class CustomerView extends VerticalLayout {

    private final InsuranceService service;
    private final Grid<User> grid = new Grid<>(User.class);

    public CustomerView(InsuranceService service) {
        this.service = service;

        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        if (currentUser == null || !"AGENT".equals(currentUser.getRole())) {
            add(new H2("Access Denied"));
            return;
        }

        setSizeFull();

        H2 title = new H2("Customer Management");
        Button addBtn = new Button("Add Customer", e -> openAddDialog());

        configureGrid();

        add(title, addBtn, grid);
        updateList();
    }

    private void configureGrid() {
        grid.setColumns("name", "username", "role");
    }

    private void updateList() {
        grid.setItems(service.findAllCustomers());
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        TextField name = new TextField("Name");
        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");

        Button save = new Button("Save", e -> {
            User user = new User();
            user.setName(name.getValue());
            user.setUsername(username.getValue());
            user.setPassword(password.getValue());
            user.setRole("CUSTOMER");

            service.saveUser(user);
            updateList();
            dialog.close();
            Notification.show("Customer created");
        });

        dialog.add(new VerticalLayout(name, username, password, save));
        dialog.open();
    }
}
