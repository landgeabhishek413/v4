package com.example.abhihsek.ui;

import com.example.abhihsek.entity.Claim;
import com.example.abhihsek.entity.Policy;
import com.example.abhihsek.entity.User;
import com.example.abhihsek.service.InsuranceService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Route(value = "claims", layout = MainLayout.class)
public class ClaimView extends VerticalLayout {

    private final InsuranceService service;
    private final Grid<Claim> grid = new Grid<>(Claim.class);
    private final User currentUser;

    public ClaimView(InsuranceService service) {
        this.service = service;
        this.currentUser = VaadinSession.getCurrent().getAttribute(User.class);

        if (currentUser == null) {
            add(new H2("Please login"));
            return;
        }

        setSizeFull();
        H2 title = new H2("Claims Management");
        add(title);

        if ("CUSTOMER".equals(currentUser.getRole())) {
            Button fileClaimBtn = new Button("File Claim", e -> openFileClaimDialog());
            add(fileClaimBtn);
        }

        configureGrid();
        add(grid);
        updateList();
    }

    private void configureGrid() {
        grid.addColumn(c -> c.getPolicy().getPolicyNumber()).setHeader("Policy");
        grid.addColumn(Claim::getClaimNumber).setHeader("Claim #");
        grid.addColumn(Claim::getDescription).setHeader("Description");
        grid.addColumn(Claim::getAmount).setHeader("Amount");
        grid.addColumn(Claim::getStatus).setHeader("Status");

        if ("AGENT".equals(currentUser.getRole())) {
            grid.addComponentColumn(claim -> {
                Button approveBtn = new Button("Approve", e -> updateStatus(claim, "APPROVED"));
                Button rejectBtn = new Button("Reject", e -> updateStatus(claim, "REJECTED"));

                // Only show if pending
                if (!"PENDING".equalsIgnoreCase(claim.getStatus())) {
                    approveBtn.setEnabled(false);
                    rejectBtn.setEnabled(false);
                }

                return new com.vaadin.flow.component.orderedlayout.HorizontalLayout(approveBtn, rejectBtn);
            });
        }
    }

    private void updateStatus(Claim claim, String status) {
        claim.setStatus(status);
        service.saveClaim(claim);
        updateList();
        Notification.show("Claim " + status);
    }

    private void updateList() {
        if ("AGENT".equals(currentUser.getRole())) {
            grid.setItems(service.getAllClaims());
        } else {
            grid.setItems(service.getClaimsForUser(currentUser.getUsername()));
        }
    }

    private void openFileClaimDialog() {
        Dialog dialog = new Dialog();

        // Find policies for this user to select against
        List<Policy> myPolicies = service.getPoliciesForUser(currentUser.getUsername());
        ComboBox<Policy> policySelect = new ComboBox<>("Policy");
        policySelect.setItems(myPolicies);
        policySelect.setItemLabelGenerator(Policy::getPolicyNumber);

        TextField claimNum = new TextField("Claim Number (e.g., CLM-001)");
        TextField desc = new TextField("Description");
        NumberField amount = new NumberField("Amount");

        Button submit = new Button("Submit", e -> {
            if (policySelect.getValue() == null) {
                Notification.show("Select a policy");
                return;
            }
            Claim c = new Claim();
            c.setClaimNumber(claimNum.getValue());
            c.setDescription(desc.getValue());
            c.setAmount(BigDecimal.valueOf(amount.getValue()));
            c.setClaimDate(LocalDate.now());
            c.setStatus("PENDING");
            c.setPolicy(policySelect.getValue());

            service.saveClaim(c);
            updateList();
            dialog.close();
            Notification.show("Claim filed successfully");
        });

        dialog.add(new VerticalLayout(policySelect, claimNum, desc, amount, submit));
        dialog.open();
    }
}
