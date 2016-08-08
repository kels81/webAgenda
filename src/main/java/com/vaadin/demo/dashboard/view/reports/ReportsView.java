package com.vaadin.demo.dashboard.view.reports;

import com.vaadin.demo.dashboard.component.PerfilGeneralForm;
import com.vaadin.demo.dashboard.component.PerfilUsuarioForm;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.demo.dashboard.event.DashboardEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class ReportsView extends Panel implements View {

    public static String CONFIRM_DIALOG_ID;

    private final VerticalLayout root;

    public ReportsView() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();

        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("sales-view");  //importante
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());
        root.addComponent(buildForm());

        /**
         * FOOTER
         */
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button btn = new Button("Guardar");
        btn.addStyleName(ValoTheme.BUTTON_PRIMARY);

        btn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    //fieldGroup.commit();
                    // Updated user should also be persisted to database. But
                    // not in this demo.

                    Notification success = new Notification(
                            "Paciente registrado exitosamente");
                    success.setDelayMsec(90000);
                    success.setStyleName("bar success small");
                    success.setPosition(Position.TOP_CENTER);
                    success.show(Page.getCurrent());

                    DashboardEventBus.post(new DashboardEvent.ProfileUpdatedEvent());
                    //close();
                } catch (Exception e) {
                    Notification.show("Error while updating profile",
                            Notification.Type.ERROR_MESSAGE);
                }
            }
        });

        footer.addComponent(btn);
        footer.setComponentAlignment(btn, Alignment.TOP_RIGHT);

        root.addComponent(footer);
        root.setExpandRatio(footer, 1);

    }

    private Component buildHeader() {
        VerticalLayout header = new VerticalLayout();
        header.addStyleName("viewheader");

        Responsive.makeResponsive(header);

        Label titleLabel = new Label("Datos de Perfil");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    private Component buildForm() {
        VerticalLayout root = new VerticalLayout();
        //root.setMargin(true);
        root.setMargin(new MarginInfo(false, true, true, true));
        root.setSpacing(true);
        root.addStyleName("sparks");

        HorizontalLayout detailsForm = new HorizontalLayout();
        detailsForm.addStyleName("formulario");
        detailsForm.setWidth(100.0f, Unit.PERCENTAGE);     //importante
        detailsForm.setSpacing(true);

        VerticalLayout pic = new VerticalLayout();
        pic.setSizeUndefined();
        pic.setSpacing(true);
        Image profilePic = new Image(null, new ThemeResource(
                "img/profile-pic-300px.jpg"));
        profilePic.setWidth(150.0f, Unit.PIXELS);
        pic.addComponent(profilePic);

        Button upload = new Button("Cambiar", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show("Not implemented in this demo");
            }
        });
        upload.addStyleName(ValoTheme.BUTTON_TINY);
        pic.addComponent(upload);

        detailsForm.addComponent(pic);

        VerticalLayout perfilForms = new VerticalLayout();
        detailsForm.addComponent(perfilForms);
        detailsForm.setExpandRatio(perfilForms, 1);

        PerfilGeneralForm perfilGralForm = new PerfilGeneralForm();
        PerfilUsuarioForm perfilUserForm = new PerfilUsuarioForm();

        perfilForms.addComponents(perfilGralForm,
                                  perfilUserForm);
        
        root.addComponent(detailsForm);

        return root;
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

}
