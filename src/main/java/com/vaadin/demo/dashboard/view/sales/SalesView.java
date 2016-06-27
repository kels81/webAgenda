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

@SuppressWarnings("serial")
public class SalesView extends VerticalLayout implements View {

    private final HorizontalLayout details;
    private TextField txtNombre;
    private TextField txtApPaterno;
    private TextField txtApmaterno;
    private TextField txtEmail;
    private TextField txtTelefono;
    
    private PopupDateField txtFechNac;

    private OptionGroup rdbGenero;
    
    private DashboardUtils util = new DashboardUtils();
    
    public SalesView() {
        //setSizeFull();
        addStyleName("profile-window");     //importante
        Responsive.makeResponsive(this);    //importante
        //setMargin(true);

        addComponent(buildHeader());

        details = buildForm();
        addComponent(details);
        setExpandRatio(details, 1);
        

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

    private HorizontalLayout buildForm() {
        HorizontalLayout detailsForm = new HorizontalLayout();
        detailsForm.setId("dos_tres");
        detailsForm.setWidth(100.0f, Unit.PERCENTAGE);                      //importante
        detailsForm.setHeightUndefined();
        detailsForm.setMargin(new MarginInfo(false, false, true, true));
        detailsForm.setSpacing(true);
        

        FormLayout form1 = new FormLayout();
        form1.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        //form1.setWidth(100.0f, Unit.PERCENTAGE);
        
//        FormLayout form2 = new FormLayout();
//        form2.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        
        detailsForm.addComponent(form1);
//        detailsForm.addComponent(form2);
        
        
        Label lblSeccion = new Label("Datos Generales");
        lblSeccion.setSizeUndefined();
        lblSeccion.addStyleName(ValoTheme.LABEL_H4);
        lblSeccion.addStyleName(ValoTheme.LABEL_COLORED);
        form1.addComponent(lblSeccion);
        
        txtNombre = util.createTextField("Nombre");
        txtApPaterno = util.createTextField("Apellido Paterno");
        txtApmaterno = util.createTextField("Apellido Materno");
        txtFechNac = util.createDateField("Fecha Nacimiento");
        rdbGenero = util.createRadioGenero("Género");
        rdbGenero.select(false);
        txtEmail = util.createTextField("Email");
        txtTelefono = util.createTextField("Teléfono");
        
        form1.addComponent(txtNombre);
        form1.addComponent(txtApPaterno);
        form1.addComponent(txtApmaterno);
        form1.addComponent(txtFechNac);
        form1.addComponent(rdbGenero);
        form1.addComponent(txtEmail);
        form1.addComponent(txtTelefono);
        

        Label label = new Label("");
        form1.addComponent(label);

        
        return detailsForm;

    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

}
