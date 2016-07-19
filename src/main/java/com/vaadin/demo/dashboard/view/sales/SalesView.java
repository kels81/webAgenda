package com.vaadin.demo.dashboard.view.sales;

import com.google.common.eventbus.Subscribe;
import com.vaadin.demo.dashboard.DashboardUI;
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
import com.vaadin.demo.dashboard.component.SparklineChart;
import com.vaadin.demo.dashboard.component.TopGrossingMoviesChart;
import com.vaadin.demo.dashboard.component.TopSixTheatersChart;
import com.vaadin.demo.dashboard.component.TopTenMoviesTable;
import com.vaadin.demo.dashboard.data.dummy.DummyDataGenerator;
import com.vaadin.demo.dashboard.domain.DashboardNotification;
import com.vaadin.demo.dashboard.event.DashboardEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.dashboard.DashboardEdit;
import static com.vaadin.demo.dashboard.view.dashboard.DashboardView.EDIT_ID;
import static com.vaadin.demo.dashboard.view.dashboard.DashboardView.TITLE_ID;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Window;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

@SuppressWarnings("serial")
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

    private Window notificationsWindow;
    private CssLayout dashboardPanels;

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

//        root.addComponent(buildSparklines());
//
//        Component content = buildContent();
//        root.addComponent(content);
//        root.setExpandRatio(content, 1);
        Component content = buildSparklines();
        root.addComponent(content);
        root.setExpandRatio(content, 1);

    }

    private Component buildSparklines() {
        VerticalLayout root = new VerticalLayout();
        root.setMargin(true);
        root.setSpacing(true);
        root.addStyleName("sparks");
        
        HorizontalLayout detailsForm = new HorizontalLayout();
        detailsForm.addStyleName("formulario");
        //detailsForm.addStyleName("sparks");
        detailsForm.setWidth(100.0f, Unit.PERCENTAGE);                      //importante
        //detailsForm.setMargin(new MarginInfo(false, true, true, true));
        detailsForm.setSpacing(true);
        
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidth(100.0f, Unit.PERCENTAGE);

        Label caption = new Label("Datos");
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        toolbar.addComponent(caption);

        root.addComponent(createLabel("Cosas"));

        FormLayout form1 = new FormLayout();
        form1.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        FormLayout form2 = new FormLayout();
        form2.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        detailsForm.addComponent(form1);
        detailsForm.addComponent(form2);

        FieldEvents.FocusListener focusListener = new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent event) {
                event.getComponent().addStyleName("blue-caption");
            }
        };

        FieldEvents.BlurListener blurListener = new FieldEvents.BlurListener() {
            @Override
            public void blur(FieldEvents.BlurEvent event) {
                event.getComponent().removeStyleName("blue-caption");
            }
        };

        //Label lblSeccion = util.createLabelH4("Datos Generales");
        //CREACION FORMULARIO 1
        txtNombre = util.createTextField("Nombre(s)");
        txtNombre.addFocusListener(focusListener);
        txtNombre.addBlurListener(blurListener);
        txtApPaterno = util.createTextField("Apellido Paterno");
        txtApmaterno = util.createTextField("Apellido Materno");
        txtFechNac = util.createDateField("Fecha Nacimiento");
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

        //return detailsForm;
        return root;
    }

    private Component buildHeader() {
        VerticalLayout header = new VerticalLayout();
        header.addStyleName("viewheader");

        Responsive.makeResponsive(header);

        Label titleLabel = new Label("Datos Personales");
        //titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    private Component buildContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);

        dashboardPanels.addComponent(buildTopGrossingMovies());
        dashboardPanels.addComponent(buildNotes());
        dashboardPanels.addComponent(buildTop10TitlesByRevenue());
        dashboardPanels.addComponent(buildPopularMovies());

        return dashboardPanels;
    }

    private Component buildTopGrossingMovies() {
        TopGrossingMoviesChart topGrossingMoviesChart = new TopGrossingMoviesChart();
        topGrossingMoviesChart.setSizeFull();
        return createContentWrapper(topGrossingMoviesChart);
    }

    private Component buildNotes() {
        TextArea notes = new TextArea("Notes");
        notes.setValue("Remember to:\n· Zoom in and out in the Sales view\n· Filter the transactions and drag a set of them to the Reports tab\n· Create a new report\n· Change the schedule of the movie theater");
        notes.setSizeFull();
        notes.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
        Component panel = createContentWrapper(notes);
        panel.addStyleName("notes");
        return panel;
    }

    private Component buildTop10TitlesByRevenue() {
        Component contentWrapper = createContentWrapper(new TopTenMoviesTable());
        contentWrapper.addStyleName("top10-revenue");
        return contentWrapper;
    }

    private Component buildPopularMovies() {
        return createContentWrapper(new TopSixTheatersChart());
    }

    private Component createContentWrapper(final Component content) {
        final CssLayout slot = new CssLayout();
        slot.setWidth("100%");
        slot.addStyleName("dashboard-panel-slot");

        CssLayout card = new CssLayout();
        card.setWidth("100%");
        card.addStyleName(ValoTheme.LAYOUT_CARD);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("dashboard-panel-toolbar");
        toolbar.setWidth("100%");

        Label caption = new Label("Datos");
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.setCaption(null);

        toolbar.addComponent(caption);
        toolbar.setExpandRatio(caption, 1);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

        card.addComponents(toolbar, content);
        slot.addComponent(card);
        return toolbar;
    }

    private Component createLabel(String labelCaption) {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidth(100.0f, Unit.PERCENTAGE);
        //toolbar.addStyleName("dashboard-panel-slot");

        Label caption = new Label(labelCaption);
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        toolbar.addComponent(caption);

        return toolbar;
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    private void toggleMaximized(final Component panel, final boolean maximized) {
        for (Iterator<Component> it = root.iterator(); it.hasNext();) {
            it.next().setVisible(!maximized);
        }
        dashboardPanels.setVisible(true);

        for (Iterator<Component> it = dashboardPanels.iterator(); it.hasNext();) {
            Component c = it.next();
            c.setVisible(!maximized);
        }

        if (maximized) {
            panel.setVisible(true);
            panel.addStyleName("max");
        } else {
            panel.removeStyleName("max");
        }
    }

}
