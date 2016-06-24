package com.vaadin.demo.dashboard.component;

import com.vaadin.data.Property;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.demo.dashboard.domain.Movie;
import com.vaadin.demo.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.schedule.ScheduleView;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("serial")
public final class MovieDetailsWindow extends Window {

    private final Label synopsis = new Label();
    private DateField txtstartDate;
    private DateField txtEndDate;
    private ComboBox cmbHoursStart;
    private ComboBox cmbHoursEnd;

    private MovieDetailsWindow(final Movie movie, final Date startTime, final Date endTime) {
        addStyleName("moviedetailswindow");
        Responsive.makeResponsive(this);

        setCaption(movie.getTitle());
        center();
        setCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        setContent(content);

        Panel detailsWrapper = new Panel(buildMovieDetails(startTime, endTime, false));
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.PANEL_BORDERLESS);
        detailsWrapper.addStyleName("scroll-divider");
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        content.addComponent(buildFooter());
    }

    private MovieDetailsWindow(final Date startTime, final Date endTime, boolean newEvent) {
        addStyleName("moviedetailswindow");
        //addStyleName("profile-window");
        Responsive.makeResponsive(this);

        center();
        setCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        detailsWrapper.addComponent(buildMovieDetails(startTime, endTime, newEvent));

        content.addComponent(buildFooter());
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("Close");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                close();
            }
        });
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    /*private Component buildMovieDetails(final Movie movie,
     final Date startTime, final Date endTime) {
     HorizontalLayout details = new HorizontalLayout();
     details.setWidth(100.0f, Unit.PERCENTAGE);
     details.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
     details.setMargin(true);
     details.setSpacing(true);

     final Image coverImage = new Image(null, new ExternalResource(
     movie.getThumbUrl()));
     coverImage.addStyleName("cover");
     details.addComponent(coverImage);

     Component detailsForm = buildDetailsForm(movie, startTime, endTime);
     details.addComponent(detailsForm);
     details.setExpandRatio(detailsForm, 1);

     return details;
     }*/
    private Component buildMovieDetails(final Date startTime, final Date endTime, boolean newEvent) {
        VerticalLayout details = new VerticalLayout();
        String caption = newEvent ? "Nueva Cita" : "Editar Cita";
        details.setCaption(caption);
        details.setIcon(FontAwesome.CALENDAR_CHECK_O);
        details.setWidth(100.0f, Unit.PERCENTAGE);
        details.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        details.setMargin(true);
        details.setSpacing(true);

//        Component label = buildLabel(startTime);
//        details.addComponent(label);
        Component detailsForm = buildDetailsForm(startTime, endTime);
        details.addComponent(detailsForm);
        details.setExpandRatio(detailsForm, 1);

        return details;
    }

    /*private Component buildDetailsForm(final Movie movie, final Date startTime,
     final Date endTime) {
     FormLayout fields = new FormLayout();
     fields.setSpacing(false);
     fields.setMargin(false);

     Label label;
     SimpleDateFormat df = new SimpleDateFormat();
     if (startTime != null) {
     df.applyPattern("dd MMM yyyy");            
     //label = new Label(df.format(startTime));
     //Obteniendo mes de la fecha
     String strMes = df.format(startTime).substring(df.format(startTime).indexOf(" ") + 1, df.format(startTime).length() - 4);
     //Primera letra del mes en Mayuscula
     label = new Label(df.format(startTime).replaceFirst(strMes.substring(0, 1), strMes.substring(0, 1).toUpperCase()));
     label.setSizeUndefined();
     label.setCaption("Date");
     fields.addComponent(label);

     df.applyPattern("hh:mm a");
     label = new Label(df.format(startTime));
     label.setSizeUndefined();
     label.setCaption("Starts");
     fields.addComponent(label);
     }

     if (endTime != null) {
     label = new Label(df.format(endTime));
     label.setSizeUndefined();
     label.setCaption("Ends");
     fields.addComponent(label);
     }

     label = new Label(movie.getDuration() + " minutes");
     label.setSizeUndefined();
     label.setCaption("Duration");
     fields.addComponent(label);

     synopsis.setData(movie.getSynopsis());
     synopsis.setCaption("Synopsis");
     updateSynopsis(movie, false);
     fields.addComponent(synopsis);

     final Button more = new Button("Moreâ€¦");
     more.addStyleName(ValoTheme.BUTTON_LINK);
     fields.addComponent(more);
     more.addClickListener(new ClickListener() {
     @Override
     public void buttonClick(final ClickEvent event) {
     updateSynopsis(null, true);
     event.getButton().setVisible(false);
     MovieDetailsWindow.this.focus();
     }
     });

     return fields;
     }*/
    private Component buildDetailsForm(final Date startTime, final Date endTime) {
        FormLayout fields = new FormLayout();
        fields.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        fields.setSpacing(false);
        fields.setMargin(false);

        txtstartDate = createDateField("Start date");

        Label lblDate = new Label(setUpperMonth(startTime));
        lblDate.addStyleName(ValoTheme.LABEL_H4);
        lblDate.addStyleName(ValoTheme.LABEL_COLORED);
        fields.addComponent(lblDate);
                
        Label label;
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("hh:mm a");
        if (startTime != null) {
            cmbHoursStart = createComboHours("De",9,23);
            cmbHoursStart.setNullSelectionAllowed(false);
            cmbHoursStart.setTextInputAllowed(false);
            cmbHoursStart.select(df.format(startTime));
            fields.addComponent(cmbHoursStart);
        }

        if (endTime != null) {
            cmbHoursEnd = createComboHours("A",9,23);
            cmbHoursEnd.setNullSelectionAllowed(false);
            cmbHoursEnd.setTextInputAllowed(false);
            cmbHoursEnd.select(df.format(endTime));
            fields.addComponent(cmbHoursEnd);
        }
        
        CheckBox allDayField = createCheckBox("All-day");
        allDayField.addValueChangeListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = -7104996493482558021L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object value = event.getProperty().getValue();
                System.out.println("value = " + value);
                /*if (value instanceof Boolean && Boolean.TRUE.equals(true)) {
                    setFormDateResolution(value);

                } else {
                    setFormDateResolution(value);
                }*/
            }

        });
        fields.addComponent(allDayField);

        label = new Label("120 minutes");
        label.setSizeUndefined();
        label.setCaption("Duration");
        fields.addComponent(label);

        synopsis.setData("A longer description, which should display correctly.");
        synopsis.setCaption("Synopsis");
        //updateSynopsis(movie, false);
        fields.addComponent(synopsis);

        final Button more = new Button("More…");
        more.addStyleName(ValoTheme.BUTTON_LINK);
        fields.addComponent(more);
        more.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                updateSynopsis(null, true);
                event.getButton().setVisible(false);
                MovieDetailsWindow.this.focus();
            }
        });

        return fields;
    }

    private void updateSynopsis(final Movie m, final boolean expand) {
        String synopsisText = synopsis.getData().toString();
        if (m != null) {
            synopsisText = m.getSynopsis();
            synopsis.setData(m.getSynopsis());
        }
        if (!expand) {
            synopsisText = synopsisText.length() > 300 ? synopsisText
                    .substring(0, 300) + "â€¦" : synopsisText;

        }
        synopsis.setValue(synopsisText);
    }

    public static void open(final Movie movie, final Date startTime, final Date endTime) {
        DashboardEventBus.post(new CloseOpenWindowsEvent());
        Window w = new MovieDetailsWindow(movie, startTime, endTime);
        UI.getCurrent().addWindow(w);
        w.focus();
    }

    public static void open(final Date startTime, final Date endTime, boolean newEvent) {
        DashboardEventBus.post(new CloseOpenWindowsEvent());
        Window w = new MovieDetailsWindow(startTime, endTime, newEvent);
        UI.getCurrent().addWindow(w);
        w.focus();
    }

    private DateField createDateField(String caption) {
        DateField f = new DateField(caption);

        f.setResolution(Resolution.MINUTE);

        return f;
    }

    private String setUpperMonth(Date date) {
        String upperMonth = "";
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("dd MMMM yyyy");
        //Obteniendo mes de la fecha
        upperMonth = df.format(date).substring(df.format(date).indexOf(" ") + 1, df.format(date).length() - 4);
        //Primera letra del mes en Mayuscula
        upperMonth = df.format(date).replaceFirst(upperMonth.substring(0, 1), upperMonth.substring(0, 1).toUpperCase());

        return upperMonth;
    }

    private ComboBox createComboHours(String caption, Integer hourStart, Integer hourEnd) {
        ComboBox hours = new ComboBox(caption);
        /*SimpleDateFormat df12 = new SimpleDateFormat();
        SimpleDateFormat df24 = new SimpleDateFormat();
        df12.applyPattern("hh:mm a");
        df24.applyPattern("HH:mm");*/
        String h = "";
        String type = "";
        for (int i = hourStart; i <= hourEnd; i++) {
            h = (i < 10 ? "0" + String.valueOf(i) : i > 12 && i < 22? "0" + String.valueOf(i-12) : i >= 22 ? String.valueOf(i-12) : String.valueOf(i));
            type = (i < 12 ? " AM" : i >= 12 ? " PM" : "");
            //System.out.println("i"+i);
            //hours.addItem(df12.format(i));
            if (i == 24) {
                hours.addItem(h + ":00" + type);
            } else {
                hours.addItem(h + ":00" + type);
                hours.addItem(h + ":30" + type);
            }
        }

        return hours;
    }
    
    private void setFormDateResolution(boolean resolution) {
        if (cmbHoursStart != null && cmbHoursEnd != null) {
            cmbHoursStart.setReadOnly(resolution);
            cmbHoursEnd.setReadOnly(resolution);
        }
    }
    
    private CheckBox createCheckBox(String caption) {
        CheckBox cb = new CheckBox(caption);
        cb.setImmediate(true);
        return cb;
    }
}
