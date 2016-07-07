package com.vaadin.demo.dashboard.view.reports;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.demo.dashboard.DashboardUtils;
import com.vaadin.demo.dashboard.event.DashboardEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;


@SuppressWarnings("serial")
public class ReportsView extends Panel implements View {
    public static String CONFIRM_DIALOG_ID;

    private final VerticalLayout root;

    private TextField txtNombre;
    private TextField txtApPaterno;
    private TextField txtApMaterno;
    private TextField txtEmail;
    private TextField txtUsuario;
    private TextField txtPassword;
    private TextField txtRepPassword;
    private ComboBox cmbRol;
    private OptionGroup rdbGenero;
    private PopupDateField birthDate;

    public ReportsView() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        Responsive.makeResponsive(this);    

        root = new VerticalLayout();
        root.addStyleName("form-content");  //importante
        root.addComponent(buildHeader());

        root.addComponent(buildForm());
        root.addComponent(buildFooter());
        
        setContent(root);
        Responsive.makeResponsive(root);
    }

    private Component buildHeader() {
        VerticalLayout header = new VerticalLayout();
        header.setMargin(new MarginInfo(true, false, false, true));
        header.addStyleName("viewheader");

        Responsive.makeResponsive(header);

        Label titleLabel = new Label("Datos de Perfil");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        header.addComponents(titleLabel);

        return header;
    }

    private Component buildForm() {

        HorizontalLayout root = new HorizontalLayout();
        //root.setCaption("Perfil");
        //root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);     //importante
        root.setSpacing(true);
        root.setMargin(true);
        root.addStyleName("profile-form");

        VerticalLayout pic = new VerticalLayout();
        pic.setSizeUndefined();
        pic.setSpacing(true);
        Image profilePic = new Image(null, new ThemeResource(
                "img/profile-pic-300px.jpg"));
        profilePic.setWidth(100.0f, Unit.PIXELS);
        pic.addComponent(profilePic);

        Button upload = new Button("Cambiar…", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show("Not implemented in this demo");
            }
        });
        upload.addStyleName(ValoTheme.BUTTON_TINY);
        pic.addComponent(upload);

        root.addComponent(pic);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);

        Label lblSeccion = new Label("Datos Generales");
        lblSeccion.addStyleName(ValoTheme.LABEL_H4);
        lblSeccion.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(lblSeccion);

        txtNombre = new TextField("Nombre");
        details.addComponent(txtNombre);
        txtApPaterno = new TextField("Apellido Paterno");
        details.addComponent(txtApPaterno);
        txtApMaterno = new TextField("Apellido Materno");
        details.addComponent(txtApMaterno);

        DashboardUtils util = new DashboardUtils();
        birthDate = util.createDateField("Fecha Nacimiento");
        details.addComponent(birthDate);

        rdbGenero = util.createRadioGenero("Género");
        details.addComponent(rdbGenero);

        Label lblSeccion2 = new Label("Datos Usuario");
        lblSeccion2.addStyleName(ValoTheme.LABEL_H4);
        lblSeccion2.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(lblSeccion2);

        txtUsuario = new TextField("Usuario");
        details.addComponent(txtUsuario);
        txtEmail = new TextField("Email");
        details.addComponent(txtEmail);
        txtPassword = new TextField("Password");
        details.addComponent(txtPassword);
        txtRepPassword = new TextField("Repetir Password");
        details.addComponent(txtRepPassword);
        cmbRol = new ComboBox("Rol");
        cmbRol.addItem("Médico");
        cmbRol.addItem("Psicólogo");
        cmbRol.addItem("Nutriólogo");
        cmbRol.setNullSelectionAllowed(false);
        details.addComponent(cmbRol);
        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidth(100.0f, Unit.PERCENTAGE);
        footer.setMargin(new MarginInfo(false, true, true, true));

        Button ok = new Button("OK");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    //fieldGroup.commit();
                    // Updated user should also be persisted to database. But
                    // not in this demo.

                    Notification success = new Notification(
                            "Paciente registrado exitosamente");
                    success.setDelayMsec(2000);
                    success.setStyleName("bar success small");
                    success.setPosition(Position.TOP_CENTER);
                    success.show(Page.getCurrent());

                    DashboardEventBus.post(new DashboardEvent.ProfileUpdatedEvent());
                    //close();
                } catch (Exception e) {
                    Notification.show("Error while entering profile",
                            Notification.Type.ERROR_MESSAGE);
                }
            }
        });
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    }
