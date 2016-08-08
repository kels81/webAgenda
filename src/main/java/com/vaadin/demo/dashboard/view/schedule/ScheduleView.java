package com.vaadin.demo.dashboard.view.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.common.eventbus.Subscribe;
import com.vaadin.demo.dashboard.DashboardUI;
import com.vaadin.demo.dashboard.DashboardUtils;
import com.vaadin.demo.dashboard.component.MovieDetailsWindow;
import com.vaadin.demo.dashboard.domain.Movie;
import com.vaadin.demo.dashboard.domain.Transaction;
import com.vaadin.demo.dashboard.event.DashboardEvent.BrowserResizeEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.CalendarComponentEvents;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.BackwardEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.DateClickEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventResize;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.ForwardEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.MoveEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.RangeSelectEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.RangeSelectHandler;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;
import com.vaadin.ui.components.calendar.handler.BasicBackwardHandler;
import com.vaadin.ui.components.calendar.handler.BasicDateClickHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventMoveHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventResizeHandler;
import com.vaadin.ui.components.calendar.handler.BasicForwardHandler;
import com.vaadin.ui.themes.ValoTheme;
import java.text.DateFormatSymbols;
import java.util.GregorianCalendar;

@SuppressWarnings("serial")
public final class ScheduleView extends CssLayout implements View {

    private final DashboardUtils util = new DashboardUtils();

    private Calendar calendar;
    private GregorianCalendar gregorian;
    private final Component tray;
    private Button nextButton;
    private Button prevButton;
    private Button dayButton;
    private Button weekButton;
    private Button monthButton;
    private final Label captionLabel = new Label("");

    private Date currentMonthsFirstDate;

    /*private enum Mode {
     MONTH, WEEK, DAY;
     }*/
    public ScheduleView() {
        setSizeFull();
        addStyleName("schedule");
        DashboardEventBus.register(this);

        TabSheet tabs = new TabSheet();
        tabs.setSizeFull();
        tabs.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        tabs.addComponent(buildCalendarView());
        tabs.addComponent(buildCatalogView());

        addComponent(tabs);

        tray = buildTray();
        addComponent(tray);

        injectMovieCoverStyles();
    }

    @Override
    public void detach() {
        super.detach();
        // A new instance of ScheduleView is created every time it's navigated
        // to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }

    private void injectMovieCoverStyles() {
        // Add all movie cover images as classes to CSSInject
        String styles = "";
        for (Movie m : DashboardUI.getDataProvider().getMovies()) {
            WebBrowser webBrowser = Page.getCurrent().getWebBrowser();

            String bg = "url(VAADIN/themes/" + UI.getCurrent().getTheme()
                    + "/img/event-title-bg.png), url(" + m.getThumbUrl() + ")";

            // IE8 doesn't support multiple background images
            if (webBrowser.isIE() && webBrowser.getBrowserMajorVersion() == 8) {
                bg = "url(" + m.getThumbUrl() + ")";
            }

            styles += ".v-calendar-event-" + m.getId()
                    + " .v-calendar-event-content {background-image:" + bg
                    + ";}";
        }

        Page.getCurrent().getStyles().add(styles);
    }

    private Component buildCalendarView() {
        VerticalLayout calendarLayout = new VerticalLayout();
        calendarLayout.setCaption("Calendario");
        calendarLayout.setMargin(true);
        //calendarLayout.setSpacing(true);
        calendarLayout.setSizeFull();

        
        HorizontalLayout hori = new HorizontalLayout();
        hori.setWidth(100.0f, Unit.PERCENTAGE);
        hori.setSpacing(true);

        prevButton = new Button("Prev", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                handlePreviousButtonClick();
            }
        });

        nextButton = new Button("Next", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                handleNextButtonClick();
            }
        });

        dayButton = new Button("DIA", new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                BasicDateClickHandler handler = (BasicDateClickHandler) calendar
                        .getHandler(CalendarComponentEvents.DateClickEvent.EVENT_ID);
                handler.dateClick(new CalendarComponentEvents.DateClickEvent(calendar,
                        new GregorianCalendar().getTime()));
            }
        });

        weekButton = new Button("SEMANA", new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                java.util.Calendar initialView = java.util.Calendar.getInstance();
                initialView.add(java.util.Calendar.DAY_OF_WEEK,
                        -initialView.get(java.util.Calendar.DAY_OF_WEEK) + 2);
                calendar.setStartDate(initialView.getTime());

                initialView.add(java.util.Calendar.DAY_OF_WEEK, 6);
                calendar.setEndDate(initialView.getTime());
            }
        });

        monthButton = new Button("MES", new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                switchToMonthView();
            }
        });

        prevButton.addStyleName(ValoTheme.BUTTON_TINY);
        prevButton.addStyleName("borderless-colored");
        prevButton.setIcon(FontAwesome.ANGLE_LEFT);
        nextButton.addStyleName(ValoTheme.BUTTON_TINY);
        nextButton.addStyleName("borderless-colored");
        nextButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
        nextButton.setIcon(FontAwesome.ANGLE_RIGHT);

        //dayButton.addStyleName(ValoTheme.BUTTON_TINY);
        //weekButton.addStyleName(ValoTheme.BUTTON_TINY);
        //monthButton.addStyleName(ValoTheme.BUTTON_SMALL);
        //monthButton.addStyleName("borderless-colored");
        
        captionLabel.addStyleName(ValoTheme.LABEL_H4);
        captionLabel.addStyleName(ValoTheme.LABEL_COLORED);
        
        hori.addComponent(prevButton);
        hori.addComponent(captionLabel);
        //hori.addComponent(monthButton);
        hori.addComponent(nextButton);

        //CssLayout group = new CssLayout();
        //group.addStyleName("v-component-group");
        //group.addComponent(dayButton);
        //group.addComponent(weekButton);
        //group.addComponent(monthButton);
        //hori.addComponent(group);

        
        hori.setComponentAlignment(prevButton, Alignment.MIDDLE_LEFT);
        hori.setComponentAlignment(captionLabel, Alignment.MIDDLE_CENTER);
        //hori.setComponentAlignment(monthButton, Alignment.MIDDLE_CENTER);
        //hori.setComponentAlignment(group, Alignment.MIDDLE_CENTER);
        hori.setComponentAlignment(nextButton, Alignment.MIDDLE_RIGHT);

        
        calendarLayout.addComponent(hori);

        calendar = new Calendar(new MovieEventProvider());
        calendar.setSizeFull();
        calendar.setWeeklyCaptionFormat("dd MMM yyyy");
        calendar.setFirstVisibleHourOfDay(9);       //PRIMER HORA DEL DIA
        calendar.setLastVisibleHourOfDay(22);       //ULTIMA HORA DEL DIA
        
        calendarLayout.addComponent(calendar);
        calendarLayout.setExpandRatio(calendar, 1.0f);

        

        setHandlers();
        //VISTA A MOSTRAR CUANDO SE INICIA 
        //VISTA SEMANA
        /*java.util.Calendar initialView = java.util.Calendar.getInstance();
         initialView.add(java.util.Calendar.DAY_OF_WEEK,
         -initialView.get(java.util.Calendar.DAY_OF_WEEK) + 1);
         calendar.setStartDate(initialView.getTime());

         initialView.add(java.util.Calendar.DAY_OF_WEEK, 6);
         calendar.setEndDate(initialView.getTime());*/
        //VISTA MES
        switchToMonthView();
        updateCaptionLabel();

        return calendarLayout;
    }

    private Component buildCatalogView() {
        CssLayout catalog = new CssLayout();
        catalog.setCaption("Catalog");
        catalog.addStyleName("catalog");

        for (final Movie movie : DashboardUI.getDataProvider().getMovies()) {
            VerticalLayout frame = new VerticalLayout();
            frame.addStyleName("frame");
            frame.setWidthUndefined();

            Image poster = new Image(null, new ExternalResource(
                    movie.getThumbUrl()));
            poster.setWidth(100.0f, Unit.PIXELS);
            poster.setHeight(145.0f, Unit.PIXELS);
            frame.addComponent(poster);

            Label titleLabel = new Label(movie.getTitle());
            titleLabel.setWidth(120.0f, Unit.PIXELS);
            frame.addComponent(titleLabel);

            frame.addLayoutClickListener(new LayoutClickListener() {
                @Override
                public void layoutClick(final LayoutClickEvent event) {
                    if (event.getButton() == MouseButton.LEFT) {
                        MovieDetailsWindow.open(movie, null, null);
                    }
                }
            });
            catalog.addComponent(frame);
        }
        return catalog;
    }

    private Component buildTray() {
        final HorizontalLayout tray = new HorizontalLayout();
        tray.setWidth(100.0f, Unit.PERCENTAGE);
        tray.addStyleName("tray");
        tray.setSpacing(true);
        tray.setMargin(true);

        Label warning = new Label(
                "You have unsaved changes made to the schedule");
        warning.addStyleName("warning");
        warning.addStyleName("icon-attention");
        tray.addComponent(warning);
        tray.setComponentAlignment(warning, Alignment.MIDDLE_LEFT);
        tray.setExpandRatio(warning, 1);

        ClickListener close = new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                setTrayVisible(false);
            }
        };

        Button confirm = new Button("Confirm");
        confirm.addStyleName(ValoTheme.BUTTON_PRIMARY);
        confirm.addClickListener(close);
        tray.addComponent(confirm);
        tray.setComponentAlignment(confirm, Alignment.MIDDLE_LEFT);

        Button discard = new Button("Discard");
        discard.addClickListener(close);
        discard.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                calendar.markAsDirty();
            }
        });
        tray.addComponent(discard);
        tray.setComponentAlignment(discard, Alignment.MIDDLE_LEFT);
        return tray;
    }

    private void setTrayVisible(final boolean visible) {
        final String styleReveal = "v-animate-reveal";
        if (visible) {
            tray.addStyleName(styleReveal);
        } else {
            tray.removeStyleName(styleReveal);
        }
    }

    @Subscribe
    public void browserWindowResized(final BrowserResizeEvent event) {
        if (Page.getCurrent().getBrowserWindowWidth() < 800) {
            calendar.setEndDate(calendar.getStartDate());
        }
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    private void setHandlers() {
        //MANEJAR EL CLICK SOBRE UN EVENTO YA AGENDADO
        calendar.setHandler(new EventClickHandler() {
            @Override
            public void eventClick(final EventClick event) {
                setTrayVisible(false);
                MovieEvent movieEvent = (MovieEvent) event.getCalendarEvent();
                MovieDetailsWindow.open(movieEvent.getStart(), movieEvent.getEnd(), false);
            }
        });

        //MANEJAR CLICK CUANDO ARRASTRAS UN EVENTO A OTRO HORARIO
        calendar.setHandler(new BasicEventMoveHandler() {
            @Override
            public void eventMove(final MoveEvent event) {
                CalendarEvent calendarEvent = event.getCalendarEvent();
                if (calendarEvent instanceof MovieEvent) {
                    MovieEvent editableEvent = (MovieEvent) calendarEvent;

                    Date newFromTime = event.getNewStart();

                    // Update event dates
                    long length = editableEvent.getEnd().getTime()
                            - editableEvent.getStart().getTime();
                    setDates(editableEvent, newFromTime,
                            new Date(newFromTime.getTime() + length));
                    setTrayVisible(true);
                }
            }

            protected void setDates(final MovieEvent event, final Date start, final Date end) {
                event.start = start;
                event.end = end;
            }
        });

        calendar.setHandler(new BasicEventResizeHandler() {
            @Override
            public void eventResize(final EventResize event) {
                Notification.show("You're not allowed to change the movie duration");
            }
        });

        //MANEJAR SELECCION DE HORAS EN EL CALENDARIO
        calendar.setHandler(new RangeSelectHandler() {
            @Override
            public void rangeSelect(final RangeSelectEvent event) {
                //Notification.show("Has seleccionado una fecha: " + event.getStart() + " " + event.getEnd());
                setTrayVisible(false);
                MovieDetailsWindow.open(event.getStart(), event.getEnd(), true);
            }
        });

        //MANEJAR CLICK EN EL ENCABEZADO DE LA FECHA EN WEEK-VIEW Y MONTH-VIEW
        calendar.setHandler(new BasicDateClickHandler() {
            @Override
            public void dateClick(DateClickEvent event) {
                updateCaptionLabel(event);
                //Notification.show("Has seleccionado una fecha: " + event.getDate());
                //DEFAULT BEHAVIOR
                super.dateClick(event);
            }
        });

        //MANEJAR CLICK FLECHAS EN BACK Y FORWARD EN EL ENCABEZADO DE LA FECHA EN WEEK-VIEW Y DAY-VIEW
        calendar.setHandler(new BasicBackwardHandler() {
            @Override
            protected void setDates(BackwardEvent event, Date start, Date end) {
                event.getComponent().setStartDate(start);
                event.getComponent().setEndDate(end);
                updateCaptionLabel(event);
            }
        });

        calendar.setHandler(new BasicForwardHandler() {
            @Override
            protected void setDates(ForwardEvent event, Date start, Date end) {
                event.getComponent().setStartDate(start);
                event.getComponent().setEndDate(end);
                updateCaptionLabel(event);
            }
        });
    }

    private class MovieEventProvider implements CalendarEventProvider {

        @Override
        public List<CalendarEvent> getEvents(final Date startDate,
                final Date endDate) {
            // Transactions are dynamically fetched from the backend service
            // when needed.
            Collection<Transaction> transactions = DashboardUI
                    .getDataProvider().getTransactionsBetween(startDate,
                            endDate);
            List<CalendarEvent> result = new ArrayList<CalendarEvent>();
            for (Transaction transaction : transactions) {
                Movie movie = DashboardUI.getDataProvider().getMovie(
                        transaction.getMovieId());
                Date end = new Date(transaction.getTime().getTime()
                        + movie.getDuration() * 60 * 1000);
                result.add(new MovieEvent(transaction.getTime(), end, movie));
            }
            return result;
        }
    }

    public final class MovieEvent implements CalendarEvent {

        private Date start;
        private Date end;
        private Movie movie;

        public MovieEvent(final Date start, final Date end, final Movie movie) {
            this.start = start;
            this.end = end;
            this.movie = movie;
        }

        @Override
        public Date getStart() {
            return start;
        }

        @Override
        public Date getEnd() {
            return end;
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public String getStyleName() {
            return String.valueOf(movie.getId());
        }

        @Override
        public boolean isAllDay() {
            return false;
        }

        public Movie getMovie() {
            return movie;
        }

        public void setMovie(final Movie movie) {
            this.movie = movie;
        }

        public void setStart(final Date start) {
            this.start = start;
        }

        public void setEnd(final Date end) {
            this.end = end;
        }

        @Override
        public String getCaption() {
            return movie.getTitle();
        }

    }

    private void handlePreviousButtonClick() {
        previousMonth();
    }

    private void handleNextButtonClick() {
        nextMonth();
    }

    private void previousMonth() {
        rollMonth(-1);
    }

    private void nextMonth() {
        rollMonth(1);
    }

    private void rollMonth(int direction) {

        int rollAmount = gregorian.get(GregorianCalendar.DAY_OF_MONTH) - 1;
        gregorian.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);
        currentMonthsFirstDate = gregorian.getTime();

        gregorian.setTime(currentMonthsFirstDate);
        gregorian.add(GregorianCalendar.MONTH, direction);

        currentMonthsFirstDate = gregorian.getTime();
        calendar.setStartDate(currentMonthsFirstDate);

        updateCaptionLabel();
        gregorian.add(GregorianCalendar.MONTH, 1);
        gregorian.add(GregorianCalendar.DATE, -1);
        calendar.setEndDate(gregorian.getTime());
    }

    private void updateCaptionLabel() {
        DateFormatSymbols s = new DateFormatSymbols();
        String month = s.getShortMonths()[gregorian.get(GregorianCalendar.MONTH)];
        int idxPrevMonth = gregorian.get(GregorianCalendar.MONTH) != 0 ? gregorian.get(GregorianCalendar.MONTH) - 1 : 11;       //0=Enero
        int idxNextMonth = gregorian.get(GregorianCalendar.MONTH) != 11 ? gregorian.get(GregorianCalendar.MONTH) + 1 : 0;;      //11=Diciembre
        String prevMonth = s.getShortMonths()[idxPrevMonth];
        String nextMonth = s.getShortMonths()[idxNextMonth];
        //monthButton.setCaption(month.toUpperCase() + " " + gregorian.get(GregorianCalendar.YEAR));
        captionLabel.setValue(month.toUpperCase() + " " + gregorian.get(GregorianCalendar.YEAR));
        prevButton.setCaption(prevMonth.toUpperCase());
        nextButton.setCaption(nextMonth.toUpperCase());
    }

    private void updateCaptionLabel(BackwardEvent event) {
        java.util.Calendar cal = event.getComponent().getInternalCalendar();
        cal.setTime(event.getComponent().getStartDate());       //SE PUEDE USAR StartDate o EndDate
        updateLabelMonth(cal);
    }

    private void updateCaptionLabel(ForwardEvent event) {
        java.util.Calendar cal = event.getComponent().getInternalCalendar();
        cal.setTime(event.getComponent().getStartDate());       //SE PUEDE USAR StartDate o EndDate
        updateLabelMonth(cal);
    }

    private void updateCaptionLabel(DateClickEvent event) {
        Date clickedDate = event.getDate();
        java.util.Calendar cal = event.getComponent().getInternalCalendar();
        cal.setTime(clickedDate);
        updateLabelMonth(cal);
    }

    public void updateLabelMonth(java.util.Calendar cal) {
        DateFormatSymbols s = new DateFormatSymbols();
        String month = s.getShortMonths()[cal.get(GregorianCalendar.MONTH)];
        int idxPrevMonth = cal.get(GregorianCalendar.MONTH) != 0 ? cal.get(GregorianCalendar.MONTH) - 1 : 11;       //0=Enero
        int idxNextMonth = cal.get(GregorianCalendar.MONTH) != 11 ? cal.get(GregorianCalendar.MONTH) + 1 : 0;;      //11=Diciembre
        String prevMonth = s.getShortMonths()[idxPrevMonth];
        String nextMonth = s.getShortMonths()[idxNextMonth];
        //monthButton.setCaption(month.toUpperCase() + " " + cal.get(GregorianCalendar.YEAR));
        captionLabel.setValue(month.toUpperCase() + " " + cal.get(GregorianCalendar.YEAR));
        prevButton.setCaption(prevMonth.toUpperCase());
        nextButton.setCaption(nextMonth.toUpperCase());
    }

    public void switchToMonthView() {
        /**
         * LAS SIGUIENTES LINEAS SON PARA CONOCER LA FECHA ACTUAL Y CON ESTA
         * FECHA PODER MOSTRAR EL MES ACTUAL
         */
        Date today = new Date();
        gregorian = new GregorianCalendar();
        gregorian.setTime(today);
        calendar.getInternalCalendar().setTime(today);

        /**
         * Calendar getStartDate (and getEndDate) has some strange logic which
         * returns Monday of the current internal time if no start date has been
         * set
         */
        calendar.setStartDate(calendar.getStartDate());
        calendar.setEndDate(calendar.getEndDate());

        int rollAmount = gregorian.get(GregorianCalendar.DAY_OF_MONTH) - 1;
        gregorian.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);

        calendar.setStartDate(gregorian.getTime());

        gregorian.add(GregorianCalendar.MONTH, 1);
        gregorian.add(GregorianCalendar.DATE, -1);

        calendar.setEndDate(gregorian.getTime());
    }

    public static boolean isThisYear(java.util.Calendar calendar, Date date) {
        calendar.setTime(new Date());
        int thisYear = calendar.get(java.util.Calendar.YEAR);
        calendar.setTime(date);
        return calendar.get(java.util.Calendar.YEAR) == thisYear;
    }

    public static boolean isThisMonth(java.util.Calendar calendar, Date date) {
        calendar.setTime(new Date());
        int thisMonth = calendar.get(java.util.Calendar.MONTH);
        calendar.setTime(date);
        return calendar.get(java.util.Calendar.MONTH) == thisMonth;
    }
}
