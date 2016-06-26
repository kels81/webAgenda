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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SalesView extends VerticalLayout implements View {

    private final HorizontalLayout details;
    //private final FormLayout details;
    private TextField txtNombre;
    private TextField txtApPaterno;
    private TextField txtApmaterno;
    private OptionGroup rdbGenero;
    private TextField txtEmail;
    private TextField txtTelefono;

    public SalesView() {
        //setSizeFull();
        //addStyleName("sales");
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
        header.addStyleName("viewheader");
        Responsive.makeResponsive(header);

        Label titleLabel = new Label("Revenue by Movie");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        header.addComponents(titleLabel);

        return header;
    }

    private HorizontalLayout buildForm() {
        //private FormLayout buildForm() {
        HorizontalLayout detailsForm = new HorizontalLayout();
        detailsForm.setWidth(100.0f, Unit.PERCENTAGE);                      //importante
        detailsForm.setMargin(new MarginInfo(false, true, true, true));
        detailsForm.setSpacing(true);

        FormLayout form1 = new FormLayout();
        form1.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        //form.setWidth(100.0f, Unit.PERCENTAGE);                      //importante
        FormLayout form2 = new FormLayout();
        form2.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        //form.setWidth(100.0f, Unit.PERCENTAGE);                      //importante
        detailsForm.addComponent(form1);
        detailsForm.addComponent(form2);

        Label lblSeccion = new Label("Datos Generales");
        lblSeccion.addStyleName(ValoTheme.LABEL_H4);
        lblSeccion.addStyleName(ValoTheme.LABEL_COLORED);
        form1.addComponent(lblSeccion);

        TextField tf = new TextField("Normal");
        tf.setInputPrompt("First name");
        form1.addComponent(tf);

        tf = new TextField("Color");
        tf.setInputPrompt("Email");
        tf.addStyleName("color1");
        form1.addComponent(tf);

        Label label = new Label("");
        //label.setSizeUndefined();
        form1.addComponent(label);

        Label lblSeccion2 = new Label("Datos Generales");
        lblSeccion2.addStyleName(ValoTheme.LABEL_H4);
        lblSeccion2.addStyleName(ValoTheme.LABEL_COLORED);
        form2.addComponent(lblSeccion2);

        TextField tf2 = new TextField("Normal");
        tf2.setInputPrompt("First name");
        form2.addComponent(tf2);

        tf2 = new TextField("Custom");
        tf2.setInputPrompt("Email");
        tf2.addStyleName("color1");
        form2.addComponent(tf2);
              
        Label label2 = new Label("");
        //label.setSizeUndefined();
        form2.addComponent(label2);

        //return form;
        return detailsForm;

    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

}
