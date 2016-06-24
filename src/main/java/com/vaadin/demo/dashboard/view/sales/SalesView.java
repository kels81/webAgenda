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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class SalesView extends VerticalLayout implements View {
    private CssLayout dashboardPanels;
    private ComboBox movieSelect;

       
    public SalesView() {
        setSizeFull();
        addStyleName("sales");
        setSpacing(true);
        addComponent(buildHeader());

        Component content = buildContent();
        addComponent(content);
        setExpandRatio(content, 1);
        initMovieSelect();
        // Add first 4 by default
        List<Movie> subList = new ArrayList<Movie>(DashboardUI
                .getDataProvider().getMovies()).subList(0, 4);
        for (Movie m : subList) {
            addDataSet(m);
        }
        
    }
    
    private Component buildContent() {
        dashboardPanels = new CssLayout();
        dashboardPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(dashboardPanels);
        setSizeFull();
        setSpacing(true);

        dashboardPanels.addComponent(buildBody());
        dashboardPanels.addComponent(buildBody2());
        dashboardPanels.addComponent(buildBody3());
        dashboardPanels.addComponent(buildBody4());
        dashboardPanels.addComponent(buildBody5());
        dashboardPanels.addComponent(buildBody6());
        dashboardPanels.addComponent(buildBody7());

        return dashboardPanels;
    }
    
    private void initMovieSelect() {
        Collection<Movie> movies = DashboardUI.getDataProvider().getMovies();
        Container movieContainer = new ListContainer<Movie>(Movie.class, movies);
        movieSelect.setContainerDataSource(movieContainer);
    }

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label titleLabel = new Label("Datos Personales");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponents(titleLabel, buildToolbar());

        return header;
    }

    private Component buildToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("toolbar");
        toolbar.setSpacing(true);

        movieSelect = new ComboBox();
        movieSelect.setItemCaptionPropertyId("title");
        movieSelect.addShortcutListener(new ShortcutListener("Add",
                KeyCode.ENTER, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                addDataSet((Movie) movieSelect.getValue());
            }
        });

        final Button add = new Button("Add");
        add.setEnabled(false);
        add.addStyleName(ValoTheme.BUTTON_PRIMARY);

        CssLayout group = new CssLayout(movieSelect, add);
        group.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        toolbar.addComponent(group);

        movieSelect.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(final ValueChangeEvent event) {
                add.setEnabled(event.getProperty().getValue() != null);
            }
        });

        final Button clear = new Button("Clear");
        clear.addStyleName("clearbutton");
        clear.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                //timeline.removeAllGraphDataSources();
                initMovieSelect();
                clear.setEnabled(false);
            }
        });
        toolbar.addComponent(clear);

        add.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                addDataSet((Movie) movieSelect.getValue());
                clear.setEnabled(true);
            }
        });

        return toolbar;
    }
    private Component buildBody() {
        HorizontalLayout body = new HorizontalLayout();
        
        body.addStyleName("viewbody");
        body.setSpacing(true);
        setSizeFull();
        Responsive.makeResponsive(body);

        Label lbNom = new Label("Nombre : ");
        lbNom.addStyleName(ValoTheme.LABEL_H3);
        lbNom.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        TextField txtNom = new TextField();
        Label lbLOPD = new Label("LOPD : ");
        lbLOPD.addStyleName(ValoTheme.LABEL_H3);
        lbLOPD.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        TextField txtLOPD = new TextField();
        Label lbTel1 = new Label("Telefono 1 : ");
        lbTel1.addStyleName(ValoTheme.LABEL_H3);
        lbTel1.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        TextField txtTel1 = new TextField();
        final Button btnLOPD = new Button("Add");
        btnLOPD.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnLOPD.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                //timeline.removeAllGraphDataSources();
                initMovieSelect();
                btnLOPD.setEnabled(false);
            }
        });
        body.addComponents(lbNom, txtNom, lbTel1, txtTel1, lbLOPD, txtLOPD, btnLOPD);
        
        return body;
    }

    private Component buildBody2() {
        HorizontalLayout body2 = new HorizontalLayout();
        
        body2.addStyleName("profile-form");
        body2.setSpacing(true);
        setSizeFull();
        Responsive.makeResponsive(body2);
        Label lbDoc = new Label("Documento : ");
        lbDoc.addStyleName(ValoTheme.LABEL_H3);
        lbDoc.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        TextField txtDoc = new TextField();
        Label lbTel2= new Label("Telefono 2 : ");
        lbTel2.addStyleName(ValoTheme.LABEL_H3);
        lbTel2.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        TextField txtTel2 = new TextField();
        Label lbFoto = new Label("Foto : ");
        lbFoto.addStyleName(ValoTheme.LABEL_H3);
        lbFoto.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        //TextField txtFoto = new TextField();
        final Button btnFoto= new Button("Add");
        btnFoto.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnFoto.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                //timeline.removeAllGraphDataSources();
                initMovieSelect();
                btnFoto.setEnabled(false);
            }
        });
        body2.addComponents(lbDoc, txtDoc, lbTel2, txtTel2, lbFoto, btnFoto);

        return body2;
    }
    
     private Component buildBody3() {
        HorizontalLayout body3 = new HorizontalLayout();
        
        body3.addStyleName("viewbody3");
        body3.setSpacing(true);
        setSizeFull();
        Responsive.makeResponsive(body3);
        Label lbFecNac = new Label("Fecha Nacimiento : ");
        lbFecNac.addStyleName(ValoTheme.LABEL_H3);
        lbFecNac.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        TextField txtFecNac = new TextField();
        Label lbDir= new Label("Dirección : ");
        lbDir.addStyleName(ValoTheme.LABEL_H3);
        lbDir.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        TextField txtDir = new TextField();
        
        body3.addComponents(lbFecNac, txtFecNac, lbDir, txtDir);

        return body3;
     }
     
     private Component buildBody4() {
        HorizontalLayout body4 = new HorizontalLayout();
        
        body4.addStyleName("viewbody4");
        body4.setSpacing(true);
        setSizeFull();
        Responsive.makeResponsive(body4);
        Label lbSexo = new Label("Sexo : ");
        lbSexo.addStyleName(ValoTheme.LABEL_H3);
        lbSexo.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        ComboBox cbmSexo = new ComboBox();
        cbmSexo.setItemCaptionPropertyId("title");
        cbmSexo.addShortcutListener(new ShortcutListener("Add",
                KeyCode.ENTER, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                //addDataSet((Movie) movieSelect.getValue());
            }
        });

        Label lbCP= new Label("Código Postal : ");
        lbCP.addStyleName(ValoTheme.LABEL_H3);
        lbCP.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        TextField txtCP = new TextField();
        
        body4.addComponents(lbSexo, cbmSexo, lbCP, txtCP);

        return body4;
    }
     
     private Component buildBody5() {
        HorizontalLayout body5 = new HorizontalLayout();
        
        body5.addStyleName("viewbody5");
        body5.setSpacing(true);
        setSizeFull();
        Responsive.makeResponsive(body5);
        Label lbEmail = new Label("E-mail : ");
        lbEmail.addStyleName(ValoTheme.LABEL_H3);
        lbEmail.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        TextField txtEmail = new TextField();
        Label lbPob= new Label("Población : ");
        lbPob.addStyleName(ValoTheme.LABEL_H3);
        lbPob.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        TextField txtPob = new TextField();
        
        body5.addComponents(lbEmail, txtEmail, lbPob, txtPob);

        return body5;
    }
     
     private Component buildBody6() {
        HorizontalLayout body6 = new HorizontalLayout();
        
        body6.addStyleName("viewbody6");
        body6.setSpacing(true);
        setSizeFull();
        Responsive.makeResponsive(body6);
        Label lbProf = new Label("Profesión : ");
        lbProf.addStyleName(ValoTheme.LABEL_H3);
        lbProf.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        TextField txtProf = new TextField();
        Label lbPais= new Label("País : ");
        lbPais.addStyleName(ValoTheme.LABEL_H3);
        lbPais.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        TextField txtPais = new TextField();
        
        body6.addComponents(lbProf, txtProf, lbPais, txtPais);

        return body6;
    }
     
     private Component buildBody7() {
        HorizontalLayout body7 = new HorizontalLayout();
        
        body7.addStyleName("viewbody7");
        body7.setSpacing(true);
        setSizeFull();
        Responsive.makeResponsive(body7);
        Label lbAle = new Label("Alertas : ");
        lbAle.addStyleName(ValoTheme.LABEL_H3);
        lbAle.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        TextArea  txtAler = new TextArea();
        Label lbObs= new Label("Observaciones : ");
        lbObs.addStyleName(ValoTheme.LABEL_H3);
        lbObs.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        TextArea  txtObs = new TextArea();
        
        body7.addComponents(lbAle, txtAler, lbObs, txtObs);

        return body7;
    }
     
    private void addDataSet(final Movie movie) {
        movieSelect.removeItem(movie);
        movieSelect.setValue(null);

        Collection<MovieRevenue> dailyRevenue = DashboardUI.getDataProvider()
                .getDailyRevenuesByMovie(movie.getId());

        ListContainer<MovieRevenue> dailyRevenueContainer = new TempMovieRevenuesContainer(
                dailyRevenue);

        dailyRevenueContainer.sort(new Object[] { "timestamp" },
                new boolean[] { true });
        }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    private class TempMovieRevenuesContainer extends
            ListContainer<MovieRevenue> {

        public TempMovieRevenuesContainer(
                final Collection<MovieRevenue> collection) {
            super(MovieRevenue.class, collection);
        }

        // This is only temporarily overridden until issues with
        // BeanComparator get resolved.
        @Override
        public void sort(final Object[] propertyId, final boolean[] ascending) {
            final boolean sortAscending = ascending[0];
            Collections.sort(getBackingList(), new Comparator<MovieRevenue>() {
                @Override
                public int compare(final MovieRevenue o1, final MovieRevenue o2) {
                    int result = o1.getTimestamp().compareTo(o2.getTimestamp());
                    if (!sortAscending) {
                        result *= -1;
                    }
                    return result;
                }
            });
        }

    }
}
