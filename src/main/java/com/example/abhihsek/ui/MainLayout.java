package com.example.abhihsek.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
    }

    private void createHeader() {
        H1 logo = new H1("Insurance App");
        logo.addClassNames("text-l", "m-m");

        Button logout = new Button("Logout", e -> {
            VaadinSession.getCurrent().getSession().invalidate();
            getUI().ifPresent(ui -> ui.getPage().setLocation("/"));
        });

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);

        com.vaadin.flow.component.orderedlayout.VerticalLayout drawer = new com.vaadin.flow.component.orderedlayout.VerticalLayout(
                new com.vaadin.flow.router.RouterLink("Policies", DashboardView.class));

        com.example.abhihsek.entity.User user = VaadinSession.getCurrent()
                .getAttribute(com.example.abhihsek.entity.User.class);
        if (user != null && "AGENT".equals(user.getRole())) {
            drawer.add(new com.vaadin.flow.router.RouterLink("Customers", CustomerView.class));
        }

        drawer.add(new com.vaadin.flow.router.RouterLink("Claims", ClaimView.class));
        addToDrawer(drawer);
    }
}
