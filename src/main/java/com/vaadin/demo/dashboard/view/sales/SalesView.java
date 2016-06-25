package com.vaadin.demo.dashboard.view.sales;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.vaadin.maddon.ListContainer;

import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.timeline.Timeline;
import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.demo.dashboard.DashboardUI;
import com.vaadin.demo.dashboard.domain.Movie;
import com.vaadin.demo.dashboard.domain.MovieRevenue;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SalesView extends VerticalLayout implements View {
    
    private FormLayout details;
    private TextField txtNombre;
    private TextField txtApPaterno;
    private TextField txtApmaterno;
    private OptionGroup rdbGenero;
    private TextField txtEmail;
    private TextField txtTelefono;
    private TextField emailField;

    public SalesView() {
        setSizeFull();
        addStyleName("sales");

        addComponent(buildHeader());

        details = buildForm();
        addComponent(details);
        setExpandRatio(details, 1);
        /*Label h1 = new Label("Text Fields");
         h1.setSizeUndefined();
         h1.addStyleName(ValoTheme.LABEL_H1);
         h1.addStyleName(ValoTheme.LABEL_NO_MARGIN);
         addComponent(h1);*/
    }

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label titleLabel = new Label("Revenue by Movie");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        header.addComponents(titleLabel);

        /*TextField tf = new TextField("Normal");
         tf.setInputPrompt("First name");
         header.addComponent(tf);

         tf = new TextField("Custom color");
         tf.setInputPrompt("Email");
         tf.addStyleName("color1");
         header.addComponent(tf);*/
        return header;
    }

    private FormLayout buildForm() {
        FormLayout form = new FormLayout();
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        
        TextField tf = new TextField("Normal");
        tf.setInputPrompt("First name");
        form.addComponent(tf);

        tf = new TextField("Custom color");
        tf.setInputPrompt("Email");
        tf.addStyleName("color1");
        form.addComponent(tf);

        return form;

    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

}
