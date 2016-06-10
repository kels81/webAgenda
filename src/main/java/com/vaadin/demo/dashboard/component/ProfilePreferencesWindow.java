package com.vaadin.demo.dashboard.component;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.demo.dashboard.domain.User;
import com.vaadin.demo.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import com.vaadin.demo.dashboard.event.DashboardEvent.ProfileUpdatedEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ProfilePreferencesWindow extends Window {

    public static final String ID = "profilepreferenceswindow";

    private final BeanFieldGroup<User> fieldGroup;
    /*
     * Fields for editing the User object are defined here as class members.
     * They are later bound to a FieldGroup by calling
     * fieldGroup.bindMemberFields(this). The Fields' values don't need to be
     * explicitly set, calling fieldGroup.setItemDataSource(user) synchronizes
     * the fields with the user object.
     */
    @PropertyId("firstName")
    private TextField txtNombre;
    @PropertyId("lastName")
    private TextField txtApPaterno;
    @PropertyId("lastName2")
    private TextField txtApMaterno;
    @PropertyId("title")
    private ComboBox cmbTitulo;
    @PropertyId("male")
    private OptionGroup rdbGenero;
    @PropertyId("email")
    private TextField emailField;
    @PropertyId("location")
    private TextField locationField;
    @PropertyId("phone")
    private TextField phoneField;
    @PropertyId("newsletterSubscription")
    private OptionalSelect<Integer> newsletterField;
    @PropertyId("website")
    private TextField websiteField;
    @PropertyId("bio")
    private TextArea bioField;
    
    //private DateField birthDate;
    private PopupDateField birthDate;
    
    
    // [ TABCONFIGURACION ]
    
    private TextField txtUsuario;
    private TextField txtEmail;
    private TextField txtPassword;
    private TextField txtRepPassword;
    private ComboBox cmbRol;

    private ProfilePreferencesWindow(final User user,
            final boolean preferencesTabOpen) {
        addStyleName("profile-window");
        setId(ID);
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(true);
        setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        detailsWrapper.addComponent(buildProfileTab());
        detailsWrapper.addComponent(buildUserTab());

        if (preferencesTabOpen) {
            detailsWrapper.setSelectedTab(1);
        }

        content.addComponent(buildFooter());

        fieldGroup = new BeanFieldGroup<User>(User.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(user);
    }
       
    // [ PRIMER TAB ]
    private Component buildProfileTab() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Perfil");
        root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
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

        Button upload = new Button("Cambiar…", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
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
        
        //[ PRIMERA SECCION ]        
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

        cmbTitulo = new ComboBox("Title");
        cmbTitulo.setInputPrompt("Please specify");
        cmbTitulo.addItem("Mr.");
        cmbTitulo.addItem("Mrs.");
        cmbTitulo.addItem("Ms.");
        cmbTitulo.setNullSelectionAllowed(false);
        //cmbTitulo.setNewItemsAllowed(true);
        details.addComponent(cmbTitulo);

        rdbGenero = new OptionGroup("Género");
        rdbGenero.addItem(Boolean.FALSE);
        rdbGenero.setItemCaption(Boolean.FALSE, "Femenino");
        rdbGenero.addItem(Boolean.TRUE);
        rdbGenero.setItemCaption(Boolean.TRUE, "Masculino");
        rdbGenero.addStyleName("horizontal");
        details.addComponent(rdbGenero);
        
        //birthDate = new DateField("Fecha Nacimiento");
        birthDate = new PopupDateField("Fecha Nacimiento");
        birthDate.setDateFormat("dd/MMM/yyyy");
        birthDate.setTextFieldEnabled(false);
        details.addComponent(birthDate);
        
        
        
        // [ SEGUNDA SECCION ]
        /*lblSeccion = new Label("Contact Info");
        lblSeccion.addStyleName(ValoTheme.LABEL_H4);
        lblSeccion.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(lblSeccion);

        emailField = new TextField("Email");
        emailField.setWidth("100%");
        emailField.setRequired(true);
        emailField.setNullRepresentation("");
        details.addComponent(emailField);

        locationField = new TextField("Location");
        locationField.setWidth("100%");
        locationField.setNullRepresentation("");
        locationField.setComponentError(new UserError(
                "This address doesn't exist"));
        details.addComponent(locationField);

        phoneField = new TextField("Phone");
        phoneField.setWidth("100%");
        phoneField.setNullRepresentation("");
        details.addComponent(phoneField);

        newsletterField = new OptionalSelect<Integer>();
        newsletterField.addOption(0, "Daily");
        newsletterField.addOption(1, "Weekly");
        newsletterField.addOption(2, "Monthly");
        details.addComponent(newsletterField);

        lblSeccion = new Label("Additional Info");
        lblSeccion.addStyleName(ValoTheme.LABEL_H4);
        lblSeccion.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(lblSeccion);

        websiteField = new TextField("Website");
        websiteField.setInputPrompt("http://");
        websiteField.setWidth("100%");
        websiteField.setNullRepresentation("");
        details.addComponent(websiteField);

        bioField = new TextArea("Bio");
        bioField.setWidth("100%");
        bioField.setRows(4);
        bioField.setNullRepresentation("");
        details.addComponent(bioField);*/

        return root;
    }
    
    // [ SEGUNDO TAB ]
    private Component buildUserTab() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Configuración");
        root.setIcon(FontAwesome.COGS);
        root.setSpacing(true);
        root.setMargin(true);
        root.setSizeFull();
        
        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);

        //[ PRIMERA SECCION ]        
        Label lblSeccion = new Label("Datos Usuario");
        lblSeccion.addStyleName(ValoTheme.LABEL_H4);
        lblSeccion.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(lblSeccion);

        txtUsuario = new TextField("Usuario");
        details.addComponent(txtUsuario);
        txtEmail = new TextField("Email");
        details.addComponent(txtEmail);
        txtPassword = new TextField("Password");
        details.addComponent(txtPassword);
        txtRepPassword = new TextField("Repetir Password");
        details.addComponent(txtRepPassword);
        cmbRol = new ComboBox("Rol");
        cmbRol.addItem("Medico");
        cmbRol.addItem("Psicologo");
        cmbRol.addItem("Nutriologo");
        cmbRol.setNullSelectionAllowed(false);
        cmbRol.setValue("Psicologo");
        details.addComponent(cmbRol);
        
        //root.addComponent(message);
        //root.setComponentAlignment(message, Alignment.MIDDLE_CENTER);
        

        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("OK");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    fieldGroup.commit();
                    // Updated user should also be persisted to database. But
                    // not in this demo.

                    Notification success = new Notification(
                            "Perfil actualizadoexitosamente");
                    success.setDelayMsec(2000);
                    success.setStyleName("bar success small");
                    success.setPosition(Position.TOP_CENTER);
                    success.show(Page.getCurrent());

                    DashboardEventBus.post(new ProfileUpdatedEvent());
                    close();
                } catch (CommitException e) {
                    Notification.show("Error while updating profile",
                            Type.ERROR_MESSAGE);
                }

            }
        });
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    public static void open(final User user, final boolean preferencesTabActive) {
        DashboardEventBus.post(new CloseOpenWindowsEvent());
        Window w = new ProfilePreferencesWindow(user, preferencesTabActive);
        UI.getCurrent().addWindow(w);
        w.focus();
    }
}
