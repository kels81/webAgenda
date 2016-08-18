package com.vaadin.demo.dashboard.component;

import com.vaadin.data.Property;
import com.vaadin.demo.dashboard.DashboardUtils;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.demo.dashboard.domain.Movie;
import com.vaadin.demo.dashboard.event.DashboardEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.message.Human;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteQuery;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.vaadin.suggestfield.BeanSuggestionConverter;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

//@SuppressWarnings("serial")
public final class MovieDetailsWindow extends Window {

    //private final DateField txtstartDate;
    //private final DateField txtEndDate;
    private ComboBox cmbHoursStart;
    private ComboBox cmbHoursEnd;
    private AutocompleteTextField autoComplete;                         //PRUEBAS AUTOCOMPLETE
    private Button btnSearch;
    private TextField txtMotivo;
    private HorizontalLayout searchField;

    private SuggestField suggestField;                                  //PRUEBAS SUGGESTFIELD
    private List<CountryBean> items = new ArrayList<CountryBean>();     //PRUEBAS SUGGESTFIELD
    long id = 0;                                                        //PRUEBAS SUGGESTFIELD
    
    private Property.ValueChangeListener valueChangeListenerHourStart;
    private Property.ValueChangeListener valueChangeListenerHourEnd;
    
    private AppointmentForm appointmentForm;
    
    private final DashboardUtils util = new DashboardUtils();

    
    

    //AutocompleteSuggestionProvider suggestionProvider = new CollectionSuggestionProvider(theJavas, MatchMode.CONTAINS, true, Locale.US);
    

    private MovieDetailsWindow(final Movie movie, final Date startTime, final Date endTime) {
        //addStyleName("moviedetailswindow");
        Responsive.makeResponsive(this);

        setCaption(movie.getTitle());
        center();
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
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
        addStyleName("profile-window");
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        //addCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
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

        detailsWrapper.addComponent(buildMovieDetails(startTime, endTime, newEvent));

        buildItems(); //PRUEBAS PARA SUGGESTFIELD

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

    private Component buildMovieDetails(final Date startTime, final Date endTime, boolean newEvent) {
        HorizontalLayout details = new HorizontalLayout();
        String caption = newEvent ? "Nueva Cita" : "Editar Cita";
        details.setCaption(caption);
        details.setIcon(FontAwesome.CALENDAR_CHECK_O);
        details.setWidth(100.0f, Unit.PERCENTAGE);      //importante
        details.setMargin(true);

        appointmentForm = new AppointmentForm(startTime, endTime);
        details.addComponent(appointmentForm);

        return details;
    }

//    private Component buildDetailsForm(final Date startTime, final Date endTime) {
//        FormLayout fields = new FormLayout();
//        fields.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
//
//        Label lblDate = new Label(setUpperMonth(startTime));
//        lblDate.addStyleName(ValoTheme.LABEL_H4);
//        lblDate.addStyleName(ValoTheme.LABEL_COLORED);
//        fields.addComponent(lblDate);
//
//        valueChangeListenerHourEnd = (Property.ValueChangeEvent event) -> {
//            String value = event.getProperty().getValue().toString().replaceAll("\\s", ":");
//            String[] valueSplit = value.split(":");
//            int hour = Integer.parseInt(valueSplit[0]);
//            String minutes = valueSplit[1];
//            String indicator = valueSplit[2];
//            
//            if (hour == 11) {
//                switch (indicator) {
//                    case "AM":
//                        indicator = "PM";
//                        break;
//                    case "PM":
//                        indicator = "AM";
//                        break;
//                }
//                hour++;
//            } else if (hour == 12) {
//                hour = hour - 11;
//            } else {
//                hour++;
//            }
//            
//            String newHour = (hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour)) + ":" + minutes + " " + indicator;
//            cmbHoursEnd.setValue(newHour);
//        };
//        
//        valueChangeListenerHourStart = (Property.ValueChangeEvent event) -> {
//            String value = event.getProperty().getValue().toString().replaceAll("\\s", ":");
//            String[] valueSplit = value.split(":");
//            int hour = Integer.parseInt(valueSplit[0]);
//            String minutes = valueSplit[1];
//            String indicator = valueSplit[2];
//            
//            if (hour == 12) {
//                switch (indicator) {
//                    case "AM":
//                        indicator = "PM";
//                        break;
//                    case "PM":
//                        indicator = "AM";
//                        break;
//                }
//                hour--;
//            } else if (hour == 1) {
//                hour = hour + 11;
//            } else {
//                hour--;
//            }
//            
//            String newHour = (hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour)) + ":" + minutes + " " + indicator;
//            cmbHoursStart.setValue(newHour);
//        };
//        
//        SimpleDateFormat df = new SimpleDateFormat();
//        df.applyPattern("hh:mm a");
//        if (startTime != null) {
//            cmbHoursStart = util.createComboHours("De", 9, 23, 1);
//            cmbHoursStart.select(df.format(startTime));
//            cmbHoursStart.addValueChangeListener(valueChangeListenerHourEnd);
//            fields.addComponent(cmbHoursStart);
//        }
//
//        if (endTime != null) {
//            cmbHoursEnd = util.createComboHours("A", 9, 23, 0);
//            cmbHoursEnd.select(df.format(endTime));
//            cmbHoursEnd.addValueChangeListener(valueChangeListenerHourStart);
//            fields.addComponent(cmbHoursEnd);
//        }
//
//        CheckBox allDayField = createCheckBox("Todo el día");
//        allDayField.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//                Object value = event.getProperty().getValue();
//                verCheck(value);
//            }
//        });
//        fields.addComponent(allDayField);
//
//        searchField = new HorizontalLayout();
//        searchField.setWidth(100.0f, Unit.PERCENTAGE);
//        searchField.setCaption("Paciente");
//
//        btnSearch = new Button();
//        btnSearch.setIcon(FontAwesome.SEARCH);
//        btnSearch.setEnabled(false);
//        btnSearch.addStyleName(ValoTheme.BUTTON_BORDERLESS);
//
//        //LISTENERS AUTOCOMPLETE FIELD
//        FieldEvents.TextChangeListener textChangeListener = (FieldEvents.TextChangeEvent event) -> {
//            btnSearch.setVisible((event.getText().length() <= 0));
//        };
//
////        suggestField = new SuggestField();
////        suggestField.setImmediate(true);
////        suggestField.setWidth(100.0f, Unit.PERCENTAGE);
////        suggestField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
////        suggestField.setSuggestionHandler(new SuggestField.SuggestionHandler() {
////            @Override
////            public List<Object> searchItems(String query) {
////                System.out.println("Query: " + query);
////                return handleSearchQuery(query);
////            }
////        });
////        suggestField.setSuggestionConverter(new CountrySuggestionConverter());
////        searchField.addComponents(suggestField, btnSearch);
////        searchField.setExpandRatio(suggestField, 1);
////        fields.addComponent(suggestField);
//        autoComplete = new AutocompleteTextField();
//        autoComplete.setImmediate(true);
//        autoComplete.setWidth(100.0f, Unit.PERCENTAGE);
//        autoComplete.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//        autoComplete.setCache(true);
//        autoComplete.setDelay(150);
//        autoComplete.setMinChars(3);
//        autoComplete.setScrollBehavior(ScrollBehavior.NONE);
//        autoComplete.setSuggestionLimit(0);
//        autoComplete.setSuggestionProvider(suggestionProvider);
//        autoComplete.addTextChangeListener(textChangeListener);
//        //ResetButtonForTextField.extend(autoComplete);
//
//        searchField.addComponents(autoComplete, btnSearch);
//        searchField.setExpandRatio(autoComplete, 1);
//        fields.addComponent(searchField);
//
//        txtMotivo = util.createTextField("Motivo de Consulta");
//        fields.addComponent(txtMotivo);
//
////        Label lblPru = new Label(
////                MaterialIcon.ACCOUNT_CIRCLE.getHtml() + " "
////                + Feather.HEAD.getHtml() + " "
////                + Ui_Kit.USER_1.getHtml() + " "
////                + Essential.USER_3.getHtml() + " "
////                + Multimedia.AVATAR.getHtml() + " "
////                + Human.USER_36.getHtml(), ContentMode.HTML);
////        lblPru.addStyleName("patientIcon");
//        //fields.addComponent(lblPru);
//        return fields;
//    }

    public static void open(final Movie movie, final Date startTime, final Date endTime) {
        DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent());
        Window w = new MovieDetailsWindow(movie, startTime, endTime);
        UI.getCurrent().addWindow(w);
        w.focus();
    }

    public static void open(final Date startTime, final Date endTime, boolean newEvent) {
        DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent());
        Window w = new MovieDetailsWindow(startTime, endTime, newEvent);
        UI.getCurrent().addWindow(w);
        w.focus();
    }


    

    

    /*
     *PRUEBAS SUGGESTFIELD
     */
    private List<Object> handleSearchQuery(String query) {
        if ("".equals(query) || query == null) {
            return Collections.emptyList();
        }
        List<CountryBean> result = new ArrayList<CountryBean>();

        for (CountryBean country : items) {
            if (country.getName().toLowerCase().startsWith(query.toLowerCase())) {
                result.add(country);
            }
        }
        System.out.println("Total: " + result.size());

        return new ArrayList<Object>(result);
    }

    public static class CountryBean implements Serializable {

        private Long id;
        private String name;

        public CountryBean() {
            // TODO Auto-generated constructor stub
        }

        public CountryBean(Long id, String name) {
            super();
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "CountryBean [id=" + id + ", name=" + name + "]";
        }

    }

    private void buildItems() {

        // @formatter:off
        items.addAll(Arrays
                .asList(new CountryBean(id++, "Afghanistan"),
                        new CountryBean(id++, "Albania"),
                        new CountryBean(id++, "Algeria"),
                        new CountryBean(id++, "Andorra"),
                        new CountryBean(id++, "Angola"),
                        new CountryBean(id++, "Antigua and Barbuda"),
                        new CountryBean(id++, "Argentina"),
                        new CountryBean(id++, "Armenia"),
                        new CountryBean(id++, "Australia"),
                        new CountryBean(id++, "Austria"),
                        new CountryBean(id++, "Azerbaijan"),
                        new CountryBean(id++, "Bahamas"),
                        new CountryBean(id++, "Bahrain"),
                        new CountryBean(id++, "Bangladesh"),
                        new CountryBean(id++, "Barbados"),
                        new CountryBean(id++, "Belarus"),
                        new CountryBean(id++, "Belgium"),
                        new CountryBean(id++, "Belize"),
                        new CountryBean(id++, "Benin"),
                        new CountryBean(id++, "Bhutan"),
                        new CountryBean(id++, "Bolivia"),
                        new CountryBean(id++, "Bosnia and Herzegovina"),
                        new CountryBean(id++, "Botswana"),
                        new CountryBean(id++, "Brazil"),
                        new CountryBean(id++, "Brunei Darussalam"),
                        new CountryBean(id++, "Bulgaria"),
                        new CountryBean(id++, "Burkina Faso"),
                        new CountryBean(id++, "Burundi"),
                        new CountryBean(id++, "Cabo Verde"),
                        new CountryBean(id++, "Cambodia"),
                        new CountryBean(id++, "Cameroon"),
                        new CountryBean(id++, "Canada"),
                        new CountryBean(id++, "Central African Republic"),
                        new CountryBean(id++, "Chad"),
                        new CountryBean(id++, "Chile"),
                        new CountryBean(id++, "China"),
                        new CountryBean(id++, "Colombia"),
                        new CountryBean(id++, "Comoros"),
                        new CountryBean(id++, "Congo"),
                        new CountryBean(id++,
                                "Democratic Republic of the Congo"),
                        new CountryBean(id++, "Costa Rica"),
                        new CountryBean(id++, "Côte d'Ivoire"),
                        new CountryBean(id++, "Croatia"),
                        new CountryBean(id++, "Cuba"),
                        new CountryBean(id++, "Cyprus"),
                        new CountryBean(id++, "Czech Republic"),
                        new CountryBean(id++, "Denmark"),
                        new CountryBean(id++, "Djibouti"),
                        new CountryBean(id++, "Dominica"),
                        new CountryBean(id++, "Dominican Republic"),
                        new CountryBean(id++, "Ecuador"),
                        new CountryBean(id++, "Egypt"),
                        new CountryBean(id++, "El Salvador"),
                        new CountryBean(id++, "Equatorial Guinea"),
                        new CountryBean(id++, "Eritrea"),
                        new CountryBean(id++, "Estonia"),
                        new CountryBean(id++, "Ethiopia"),
                        new CountryBean(id++, "Fiji"),
                        new CountryBean(id++, "Finland"),
                        new CountryBean(id++, "France"),
                        new CountryBean(id++, "Gabon"),
                        new CountryBean(id++, "Gambia"),
                        new CountryBean(id++, "Georgia"),
                        new CountryBean(id++, "Germany"),
                        new CountryBean(id++, "Ghana"),
                        new CountryBean(id++, "Greece"),
                        new CountryBean(id++, "Grenada"),
                        new CountryBean(id++, "Guatemala"),
                        new CountryBean(id++, "Guinea"),
                        new CountryBean(id++, "Guinea-Bissau"),
                        new CountryBean(id++, "Guyana"),
                        new CountryBean(id++, "Haiti"),
                        new CountryBean(id++, "Honduras"),
                        new CountryBean(id++, "Hungary"),
                        new CountryBean(id++, "Iceland"),
                        new CountryBean(id++, "India"),
                        new CountryBean(id++, "Indonesia"),
                        new CountryBean(id++, "Iran (Islamic Republic of)"),
                        new CountryBean(id++, "Iraq"),
                        new CountryBean(id++, "Ireland"),
                        new CountryBean(id++, "Israel"),
                        new CountryBean(id++, "Italy"),
                        new CountryBean(id++, "Jamaica"),
                        new CountryBean(id++, "Japan"),
                        new CountryBean(id++, "Jordan"),
                        new CountryBean(id++, "Kazakhstan"),
                        new CountryBean(id++, "Kenya"),
                        new CountryBean(id++, "Kiribati"),
                        new CountryBean(id++,
                                "Democratic People's Republic of Korea"),
                        new CountryBean(id++, "Republic of Korea"),
                        new CountryBean(id++, "Kuwait"),
                        new CountryBean(id++, "Kyrgyzstan"),
                        new CountryBean(id++,
                                "Lao People's Democratic Republic"),
                        new CountryBean(id++, "Latvia"),
                        new CountryBean(id++, "Lebanon"),
                        new CountryBean(id++, "Lesotho"),
                        new CountryBean(id++, "Liberia"),
                        new CountryBean(id++, "Libya"),
                        new CountryBean(id++, "Liechtenstein"),
                        new CountryBean(id++, "Lithuania"),
                        new CountryBean(id++, "Luxembourg"),
                        new CountryBean(id++,
                                "The former Yugoslav Republic of Macedonia"),
                        new CountryBean(id++, "Madagascar"),
                        new CountryBean(id++, "Malawi"),
                        new CountryBean(id++, "Malaysia"),
                        new CountryBean(id++, "Maldives"),
                        new CountryBean(id++, "Mali"),
                        new CountryBean(id++, "Malta"),
                        new CountryBean(id++, "Marshall Islands"),
                        new CountryBean(id++, "Mauritania"),
                        new CountryBean(id++, "Mauritius"),
                        new CountryBean(id++, "Mexico"),
                        new CountryBean(id++,
                                "Micronesia (Federated States of)"),
                        new CountryBean(id++, "Republic of Moldova"),
                        new CountryBean(id++, "Monaco"),
                        new CountryBean(id++, "Mongolia"),
                        new CountryBean(id++, "Montenegro"),
                        new CountryBean(id++, "Morocco"),
                        new CountryBean(id++, "Mozambique"),
                        new CountryBean(id++, "Myanmar"),
                        new CountryBean(id++, "Namibia"),
                        new CountryBean(id++, "Nauru"),
                        new CountryBean(id++, "Nepal"),
                        new CountryBean(id++, "Netherlands"),
                        new CountryBean(id++, "New Zealand"),
                        new CountryBean(id++, "Nicaragua"),
                        new CountryBean(id++, "Niger"),
                        new CountryBean(id++, "Nigeria"),
                        new CountryBean(id++, "Norway"),
                        new CountryBean(id++, "Oman"),
                        new CountryBean(id++, "Pakistan"),
                        new CountryBean(id++, "Palau"),
                        new CountryBean(id++, "Panama"),
                        new CountryBean(id++, "Papua New Guinea"),
                        new CountryBean(id++, "Paraguay"),
                        new CountryBean(id++, "Peru"),
                        new CountryBean(id++, "Philippines"),
                        new CountryBean(id++, "Poland"),
                        new CountryBean(id++, "Portugal"),
                        new CountryBean(id++, "Qatar"),
                        new CountryBean(id++, "Romania"),
                        new CountryBean(id++, "Russian Federation"),
                        new CountryBean(id++, "Rwanda"),
                        new CountryBean(id++, "Saint Kitts and Nevis"),
                        new CountryBean(id++, "Saint Lucia"),
                        new CountryBean(id++,
                                "Saint Vincent and the Grenadines"),
                        new CountryBean(id++, "Samoa"),
                        new CountryBean(id++, "San Marino"),
                        new CountryBean(id++, "Sao Tome and Principe"),
                        new CountryBean(id++, "Saudi Arabia"),
                        new CountryBean(id++, "Senegal"),
                        new CountryBean(id++, "Serbia"),
                        new CountryBean(id++, "Seychelles"),
                        new CountryBean(id++, "Sierra Leone"),
                        new CountryBean(id++, "Singapore"),
                        new CountryBean(id++, "Slovakia"),
                        new CountryBean(id++, "Slovenia"),
                        new CountryBean(id++, "Solomon Islands"),
                        new CountryBean(id++, "Somalia"),
                        new CountryBean(id++, "South Africa"),
                        new CountryBean(id++, "South Sudan"),
                        new CountryBean(id++, "Spain"),
                        new CountryBean(id++, "Sri Lanka"),
                        new CountryBean(id++, "Sudan"),
                        new CountryBean(id++, "Suriname"),
                        new CountryBean(id++, "Swaziland"),
                        new CountryBean(id++, "Sweden"),
                        new CountryBean(id++, "Switzerland"),
                        new CountryBean(id++, "Syrian Arab Republic"),
                        new CountryBean(id++, "Tajikistan"),
                        new CountryBean(id++, "United Republic of Tanzania"),
                        new CountryBean(id++, "Thailand"),
                        new CountryBean(id++, "Timor-Leste"),
                        new CountryBean(id++, "Togo"),
                        new CountryBean(id++, "Tonga"),
                        new CountryBean(id++, "Trinidad and Tobago"),
                        new CountryBean(id++, "Tunisia"),
                        new CountryBean(id++, "Turkey"),
                        new CountryBean(id++, "Turkmenistan"),
                        new CountryBean(id++, "Tuvalu"),
                        new CountryBean(id++, "Uganda"),
                        new CountryBean(id++, "Ukraine"),
                        new CountryBean(id++, "United Arab Emirates"),
                        new CountryBean(id++,
                                "United Kingdom of Great Britain and Northern Ireland"),
                        new CountryBean(id++, "United States of America"),
                        new CountryBean(id++, "Uruguay"), new CountryBean(id++,
                                "Uzbekistan"),
                        new CountryBean(id++, "Vanuatu"), new CountryBean(id++,
                                "Venezuela (Bolivarian Republic of)"),
                        new CountryBean(id++, "Viet Nam"), new CountryBean(
                                id++, "Yemen"),
                        new CountryBean(id++, "Zambia"), new CountryBean(id++,
                                "Zimbabwe")));
    }

    private class CountrySuggestionConverter extends BeanSuggestionConverter {

        public CountrySuggestionConverter() {
            super(CountryBean.class, "id", "name", "name");
        }

        @Override
        public Object toItem(SuggestFieldSuggestion suggestion) {
            CountryBean result = null;
            for (CountryBean bean : items) {
                if (bean.getId().toString().equals(suggestion.getId())) {
                    result = bean;
                    break;
                }
            }
            assert result != null : "This should not be happening";
            return result;
        }

    }

}
