package com.vaadin.demo.dashboard.view.sales;

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
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class SalesView extends Panel implements View {

    //private final HorizontalLayout details;
    private final VerticalLayout root;
    private TextField txtNombre;
    private TextField txtApPaterno;
    private TextField txtApmaterno;
    private TextField txtEmail;
    private TextField txtTelefono;
    private TextField txtCURP;
    
    private ComboBox cmbProfesion;
    private ComboBox cmbEstados;
    private ComboBox cmbEdoCivil;
    private ComboBox cmbReligion;
    
    private PopupDateField txtFechNac;

    private OptionGroup rdbGenero;
    
    private DashboardUtils util = new DashboardUtils();
    
    
    public SalesView() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        Responsive.makeResponsive(this);    
        
        root = new VerticalLayout();
        root.addStyleName("form-content");  //importante
        root.addComponent(buildHeader());
       
        root.addComponent(buildForm());
        //details = buildForm();
       // root.addComponent(details);
        //root.setExpandRatio(details, 1);
        
        root.addComponent(buildFooter());
        
        
        
        setContent(root);
        Responsive.makeResponsive(root);
    }

    private Component buildHeader() {
        VerticalLayout header = new VerticalLayout();
        header.setMargin(new MarginInfo(true, false, false, true));
        header.addStyleName("viewheader");
        
        Responsive.makeResponsive(header);

        Label titleLabel = new Label("Datos Personales");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        header.addComponents(titleLabel);

        return header;
    }

    private Component buildForm() {
        HorizontalLayout detailsForm = new HorizontalLayout();
        detailsForm.addStyleName("formulario");
        
        
        detailsForm.setWidth(100.0f, Unit.PERCENTAGE);                      //importante
        detailsForm.setMargin(new MarginInfo(false, true, true, true));
        detailsForm.setSpacing(true);
        
        FormLayout form1 = new FormLayout();
        form1.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        
        FormLayout form2 = new FormLayout();
        form2.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        
        FormLayout form3 = new FormLayout();
        form3.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        
        detailsForm.addComponent(form1);
        detailsForm.addComponent(form2);
        detailsForm.addComponent(form3);
        
        
        //Label lblSeccion = util.createLabelH4("Datos Generales");
        
        
        txtNombre   = util.createTextField("Nombre(s)");
        txtApPaterno= util.createTextField("Apellido Paterno");
        txtApmaterno= util.createTextField("Apellido Materno");
        txtFechNac  = util.createDateField("Fecha Nacimiento");
        rdbGenero   = util.createRadioGenero("Género");
        rdbGenero.select(false);
        cmbEstados  = util.createComboEstados("Estado Nacimiento");
        txtCURP     = util.createTextField("CURP");
        
        
        
        form1.addComponent(txtNombre);
        form1.addComponent(txtApPaterno);
        form1.addComponent(txtApmaterno);
        form1.addComponent(txtFechNac);
        form1.addComponent(rdbGenero);
        form1.addComponent(cmbEstados);
        
        
        ComboBox cmbProfesion2= util.createComboProfesion("Profesión");
        ComboBox cmbEdoCivil2 = util.createComboEdoCivil("Estado Civil");
        ComboBox cmbReligion2 = util.createComboReligion("Religión");
        TextField txtEmail2    = util.createTextField("Email");
        TextField txtTelefono2 = util.createTextField("Teléfono");
        
        form2.addComponent(cmbProfesion2);
        form2.addComponent(cmbEdoCivil2);
        form2.addComponent(cmbReligion2);
        form2.addComponent(txtEmail2);
        form2.addComponent(txtTelefono2);
        
        
        ComboBox cmbProfesion3= util.createComboProfesion("Profesión3");
        ComboBox cmbEdoCivil3 = util.createComboEdoCivil("Estado Civil3");
        ComboBox cmbReligion3 = util.createComboReligion("Religión3");
        TextField txtEmail3    = util.createTextField("Email3");
        TextField txtTelefono3 = util.createTextField("Teléfono3");
        
        form3.addComponent(cmbProfesion3);
        form3.addComponent(cmbEdoCivil3);
        form3.addComponent(cmbReligion3);
        form3.addComponent(txtEmail3);
        form3.addComponent(txtTelefono3);
        
        
        TextField txtNombre2 = util.createTextField("Nombre");
        TextField txtApPaterno2 = util.createTextField("Apellido Paterno");
        TextField txtApmaterno2 = util.createTextField("Apellido Materno");
        PopupDateField txtFechNac2 = util.createDateField("Fecha Nacimiento");
        OptionGroup rdbGenero2 = util.createRadioGenero("Género");
        rdbGenero2.select(true);
        //TextField txtEmail2 = util.createTextField("Email");
        //TextField txtTelefono2 = util.createTextField("Teléfono");
        
        form2.addComponent(txtNombre2);
        form2.addComponent(txtApPaterno2);
        form2.addComponent(txtApmaterno2);
        form2.addComponent(txtFechNac2);
        //form2.addComponent(rdbGenero2);
        //form2.addComponent(txtEmail2);
        //form2.addComponent(txtTelefono2);
        
        
        return detailsForm;

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
                    Notification.show("Error while updating profile",
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
