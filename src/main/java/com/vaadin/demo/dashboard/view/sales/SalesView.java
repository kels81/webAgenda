package com.vaadin.demo.dashboard.view.sales;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.demo.dashboard.DashboardUtils;
import com.vaadin.demo.dashboard.component.PatientPreferencesWindow;
import com.vaadin.demo.dashboard.domain.User;
import com.vaadin.demo.dashboard.event.DashboardEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.message.Human;
import com.vaadin.demo.dashboard.view.message.Solid;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import org.vaadin.resetbuttonfortextfield.ResetButtonForTextField;

public class SalesView extends Panel implements View {

    private final VerticalLayout root;
    private final DashboardUtils util = new DashboardUtils();

    private TextField txtNombre;
    private TextField txtApPaterno;
    private TextField txtApmaterno;
    private TextField txtEmail;
    private TextField txtTelefono;
    private TextField txtCelular;
    private TextField txtCURP;
    private TextField txtNombreCom;
    private TextField txtTelefonoMed;
    private TextField txtNombreParent;
    private TextField txtTelefonoParent;
    private TextField txtFechaProceso;
    private TextField txtContacto;
    private TextField txtMotConsulta;
    

    private TextArea txAEnfermedades;
    private TextArea txAMedicamento;

    private ComboBox cmbProfesion;
    private ComboBox cmbEstados;
    private ComboBox cmbEdoCivil;
    private ComboBox cmbReligion;
    private ComboBox cmbParentesco;
    private ComboBox cmbGenero;

    private PopupDateField txtFechNac;


    public SalesView() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("sales-view");
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());
        //root.addComponent(buildToolBar());
        root.addComponent(buildForm());
        //root.addComponent(buildConsulta());
        //root.addComponent(buildEnfermedades());

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
                    success.setDelayMsec(1000);
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
        header.setSpacing(true);
        header.addStyleName("viewheader");

        Responsive.makeResponsive(header);

        Label titleLabel = new Label("Pacientes");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        
        header.addComponents(titleLabel,buildToolBar());
        

        return header;
    }
    
    private Component buildToolBar() {
        HorizontalLayout tools = new HorizontalLayout();
        tools.setWidth(100.0f, Unit.PERCENTAGE);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        
        TextField filter = new TextField();
        filter.setInputPrompt("Buscar por nombre y/o apellidos");
        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        filter.setWidth(100.0f, Unit.PERCENTAGE);
        ResetButtonForTextField.extend(filter);
                
        Button addPaciente = new Button("Paciente");
        addPaciente.setDescription("Agregar Paciente");
        addPaciente.setIcon(FontAwesome.PLUS_CIRCLE);
        addPaciente.addStyleName(ValoTheme.BUTTON_PRIMARY);
        addPaciente.addClickListener((Button.ClickEvent event) -> {
            try {
                User user = (User) VaadinSession.getCurrent().getAttribute(
                        User.class.getName());
                PatientPreferencesWindow.open(user, true);
            } catch (Exception e) {
                
            }
        });
        
        
        tools.addComponents(filter,addPaciente);
        tools.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
        tools.setComponentAlignment(addPaciente, Alignment.MIDDLE_RIGHT);
        
        
        return tools;
    }

    private Component buildForm() {
        VerticalLayout root = new VerticalLayout();
        root.setSpacing(true);
        root.addStyleName("sparks");

        HorizontalLayout detailsForm = new HorizontalLayout();
        detailsForm.addStyleName("formulario");
        detailsForm.setWidth(100.0f, Unit.PERCENTAGE);                      //importante
        detailsForm.setSpacing(true);

        root.addComponent(createLabel("Información General"));

        FormLayout form1 = new FormLayout();
        form1.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        
        FormLayout form2 = new FormLayout();
        form2.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        

        detailsForm.addComponent(form1);
        detailsForm.addComponent(form2);


        //CREACION FORMULARIO 1
        txtNombre = util.createTextField("Nombre(s)");
        //txtNombre.addFocusListener(focusListener);
        //txtNombre.addBlurListener(blurListener);
        txtApPaterno = util.createTextField("Apellido Paterno");
        txtApmaterno = util.createTextField("Apellido Materno");
        txtFechNac = util.createDateFieldNac("Fecha Nacimiento");
        cmbGenero = util.createComboGenero("Género");
        cmbEstados = util.createComboEstados("Estado Nacimiento");
        txtCURP = util.createTextField("CURP");

        form1.addComponent(txtNombre);
        form1.addComponent(txtApPaterno);
        form1.addComponent(txtApmaterno);
        form1.addComponent(txtFechNac);
        form1.addComponent(cmbGenero);
        form1.addComponent(cmbEstados);
        form1.addComponent(txtCURP);

        //CREACION FORMULARIO 2
        cmbProfesion = util.createComboProfesion("Profesión");
        cmbEdoCivil = util.createComboEdoCivil("Estado Civil");
        cmbReligion = util.createComboReligion("Religión");
        txtEmail = util.createTextField("Email");
        txtTelefono = util.createTextField("Teléfono");
        txtCelular = util.createTextField("Tel. Celular");

        form2.addComponent(cmbProfesion);
        form2.addComponent(cmbEdoCivil);
        form2.addComponent(cmbReligion);
        form2.addComponent(txtEmail);
        form2.addComponent(txtTelefono);
        form2.addComponent(txtCelular);

        root.addComponent(detailsForm);

        return root;
    }

    private Component buildEnfermedades() {
        VerticalLayout root = new VerticalLayout();
        //root.setMargin(true);
        root.setMargin(new MarginInfo(false, true, true, true));
        root.setSpacing(true);
        root.addStyleName("sparks");

        HorizontalLayout detailsForm = new HorizontalLayout();
        detailsForm.addStyleName("formulario");
        detailsForm.setWidth(100.0f, Unit.PERCENTAGE);                      //importante
        detailsForm.setSpacing(true);

        root.addComponent(createLabel("Información Enfermedad"));

        FormLayout form1 = new FormLayout();
        form1.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        FormLayout form2 = new FormLayout();
        form2.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        detailsForm.addComponent(form1);
        detailsForm.addComponent(form2);

        txAEnfermedades = util.createTextArea("Enfermedad(s)");
        txAEnfermedades.addStyleName("notes");
        txAMedicamento = util.createTextArea("Toma algún medicamento");
        txAMedicamento.addStyleName("color1");

        form1.addComponent(txAEnfermedades);
        form2.addComponent(txAMedicamento);

        root.addComponent(detailsForm);

        return root;

    }

    private Component buildConsulta() {
        VerticalLayout root = new VerticalLayout();
        //root.setMargin(true);
        root.setMargin(new MarginInfo(false, true, true, true));
        root.setSpacing(true);
        root.addStyleName("sparks");

        //HorizontalLayout detailsForm = new HorizontalLayout();
        GridLayout detailsForm = new GridLayout(2,3);       //#columnas, #filas
        detailsForm.addStyleName("formulario");
        detailsForm.setWidth(100.0f, Unit.PERCENTAGE);                      //importante
        detailsForm.setSpacing(true);
        
        //root.addComponent(createLabel("Información Médico"));
        
        FormLayout form1 = new FormLayout();
        form1.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        FormLayout form2 = new FormLayout();
        form2.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        //form2.setWidth("100%");
        
        FormLayout form11 = new FormLayout();
        //toolbar.setWidth(100.0f, Unit.PERCENTAGE);
        form11.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        Label lbl = new Label("Prueba");
        TextField txtField = new TextField("Prueba");
        //txtField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        Label lbl2 = new Label("Prueba2");
        TextField txtField2 = new TextField("Prueba2");
        //txtField2.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        HorizontalLayout wrap = new HorizontalLayout();
        wrap.setCaption("Sede");
        wrap.addComponents(txtField,txtField2);

        //form11.addComponents(util.createLabelH4("INFORMACIÓN MÉDICO"),txtField,txtField2);
        form11.addComponents(txtField,txtField2);
        //root.addComponent(form11);


        detailsForm.addComponent(form11,0,0);
        detailsForm.addComponent(form1,1,0);
        
        //detailsForm.addComponent(form11);
        //detailsForm.addComponent(form1);
        detailsForm.addComponent(form2,0,1,1,1);

        txtNombreCom = util.createTextField("Médico Tratante");
        txtTelefonoMed = util.createTextField("Telefono Médico Tratante ");
        txtMotConsulta = util.createTextField("Motivo de consulta");
        txtFechaProceso = util.createTextField("Fecha de Inicio de proceso");
        txtFechaProceso.setValue(util.getToday());
        txtFechaProceso.setEnabled(false);
        txtContacto = util.createTextField("Contacto de emergencia");
        txtNombreParent = util.createTextField("Nombre");
        txtTelefonoParent = util.createTextField("Telefono");
        cmbParentesco = util.createComboParentesco("Parentesco");

        form1.addComponent(txtNombreCom);
        form1.addComponent(txtTelefonoMed);
        form1.addComponent(txtMotConsulta);
        form1.addComponent(txtFechaProceso);
        form2.addComponent(txtContacto);
        form2.addComponent(txtNombreParent);
        form2.addComponent(txtTelefonoParent);
        form2.addComponent(cmbParentesco);

        root.addComponent(detailsForm);

        return root;
    }

    private Component createLabel(String labelCaption) {
        FormLayout label = new FormLayout();
        //toolbar.setWidth(100.0f, Unit.PERCENTAGE);
        label.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        label.addComponent(util.createLabelH4(labelCaption));

        return label;
    }
    

    @Override
    public void enter(final ViewChangeEvent event) {
    }

}
