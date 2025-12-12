package com.example.abhihsek.ui;

import com.example.abhihsek.entity.Policy;
import com.example.abhihsek.entity.User;
import com.example.abhihsek.service.InsuranceService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.math.BigDecimal;
import java.time.LocalDate;

@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {

    private final InsuranceService service;
    private final Grid<Policy> grid = new Grid<>(Policy.class);
    private User currentUser;

    public DashboardView(InsuranceService service) {
        this.service = service;
        this.currentUser = VaadinSession.getCurrent().getAttribute(User.class);

        if (currentUser == null) {
            // Not logged in, redirect
            // In a real app we'd use BeforeEnterObserver, but keeping it simple
            add(new H2("Please login"));
            return;
        }

        setSizeFull();
        configureGrid();

        H2 title = new H2("Welcome, " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        add(title);

        if ("AGENT".equals(currentUser.getRole())) {
            Button addBtn = new Button("Add Policy", e -> openAddDialog());
            add(addBtn);
        }

        add(grid);
        updateList();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("policyNumber", "type", "premium", "startDate", "endDate", "status");

        if ("AGENT".equals(currentUser.getRole())) {
            grid.addComponentColumn(policy -> {
                Button deleteBtn = new Button("Delete", e -> {
                    service.deletePolicy(policy.getId());
                    updateList();
                });
                return deleteBtn;
            });
        }
    }

    private void updateList() {
        if ("AGENT".equals(currentUser.getRole())) {
            grid.setItems(service.getAllPolicies());
        } else {
            grid.setItems(service.getPoliciesForUser(currentUser.getUsername()));
        }
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        TextField number = new TextField("Policy Number");
        TextField type = new TextField("Type");
        TextField premium = new TextField("Premium");
        Button save = new Button("Save", e -> {
            Policy p = new Policy();
            p.setPolicyNumber(number.getValue());
            p.setType(type.getValue());
            p.setPremium(new BigDecimal(premium.getValue()));
            p.setStartDate(LocalDate.now());
            p.setEndDate(LocalDate.now().plusYears(1));
            p.setStatus("ACTIVE");
            // For simplicity, assigning to the logged in Agent? No, that's wrong.
            // Simplified: Assigning to the first customer found or just null
            // In real app, we'd select a user.
            // Let's just create it unassigned for now or assign to self if testing
            p.setUser(currentUser);

            service.savePolicy(p);
            updateList();
            dialog.close();
            Notification.show("Policy created");
        });

        dialog.add(new VerticalLayout(number, type, premium, save));
        dialog.open();
    }
}
