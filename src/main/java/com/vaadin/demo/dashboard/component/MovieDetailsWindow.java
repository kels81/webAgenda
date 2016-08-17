package com.vaadin.demo.dashboard.component;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.demo.dashboard.domain.Movie;
import com.vaadin.demo.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
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
import java.util.regex.Pattern;
import org.vaadin.suggestfield.BeanSuggestionConverter;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.client.SuggestFieldSuggestion;

@SuppressWarnings("serial")
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

    /*
     *PRUEBAS AUTOCOMPLETETEXTFIELD
     */
    Collection<String> NAMES = Arrays.asList(new String[]{
        "Olga Almanza Arellano",
        "Graciela Alvarado Alvarado",
        "Enriqueta Almanza Delgado",
        "Ma Marco Cardona Alvarez",
        "Alicia Delgado Rodriguez",
        "Graciela Delgado Guerra",
        "Amparo Chavez Rodriguez",
        "Gonzalo Chavarria Villalobos",
        "Carolina Escobedo Alvarez",
        "Magdalena Hernandez Becerra",
        "Elvira Hernandez Martinez",
        "Martha Martinez Salas",
        "Juan Arellano Garcia",
        "Obdelia Reyes Delgado",
        "Belia Elvia Arellano Garcia",
        "Ma Carmen Prieto Duron",
        "Vela Segovia Arroyo",
        "Celestina Herrera Gonzalez",
        "Angelica Macias Almanza",
        "Gonzalo Macias Almanza",
        "Lorena Hernandez Macias",
        "Enrique Arellano Garcia",
        "Elias Arellano Garcia",});

    //AutocompleteSuggestionProvider suggestionProvider = new CollectionSuggestionProvider(theJavas, MatchMode.CONTAINS, true, Locale.US);
    private final CollectionSuggestionProvider suggestionProvider
            = new CollectionSuggestionProvider(NAMES, MatchMode.CONTAINS, true) {
                @Override
                public Collection<AutocompleteSuggestion> querySuggestions(AutocompleteQuery query) {
                    Collection<AutocompleteSuggestion> suggestions = super.querySuggestions(query);

                    int i = 0;
                    for (AutocompleteSuggestion suggestion : suggestions) {
                        //suggestion.setDescription("This is a description for "
                        //+ suggestion.getValue() + " ...");
                        //suggestion.setIcon(new ThemeResource("img/user_icon.png"));
                        //suggestion.setIcon(FontAwesome.USER);
                        //suggestion.setIcon(MaterialIcon.ACCOUNT_CIRCLE);
                        //suggestion.setIcon(Feather.HEAD);
                        suggestion.setIcon(Human.USER_36);
                        suggestion.addStyleName("patientIcon");

                    }
                    return suggestions;
                }

            };

    private MovieDetailsWindow(final Movie movie, final Date startTime, final Date endTime) {
        addStyleName("moviedetailswindow");
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
        setClosable(false);
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

        Component detailsForm = buildDetailsForm(startTime, endTime);
        details.addComponent(detailsForm);

        return details;
    }

    private Component buildDetailsForm(final Date startTime, final Date endTime) {
        System.out.println("endTime = " + endTime);
        System.out.println("startTime = " + startTime);
        FormLayout fields = new FormLayout();
        fields.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        //fields.setSpacing(false);
        //fields.setMargin(false);

        //txtstartDate = createDateField("Start date");
        Label lblDate = new Label(setUpperMonth(startTime));
        lblDate.addStyleName(ValoTheme.LABEL_H4);
        lblDate.addStyleName(ValoTheme.LABEL_COLORED);
        fields.addComponent(lblDate);

        valueChangeListenerHourEnd = (Property.ValueChangeEvent event) -> {
            String value = event.getProperty().getValue().toString().replaceAll("\\s", ":");
            String[] valueSplit = value.split(":");
            int hour = Integer.parseInt(valueSplit[0]);
            String minutes = valueSplit[1];
            String indicator = valueSplit[2];
            
            if (hour == 11) {
                switch (indicator) {
                    case "AM":
                        indicator = "PM";
                        break;
                    case "PM":
                        indicator = "AM";
                        break;
                }
                hour++;
            } else if (hour == 12) {
                hour = hour - 11;
            } else {
                hour++;
            }
            
            String newHour = (hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour)) + ":" + minutes + " " + indicator;
            cmbHoursEnd.setValue(newHour);
        };
        
        valueChangeListenerHourStart = (Property.ValueChangeEvent event) -> {
            String value = event.getProperty().getValue().toString().replaceAll("\\s", ":");
            String[] valueSplit = value.split(":");
            int hour = Integer.parseInt(valueSplit[0]);
            String minutes = valueSplit[1];
            String indicator = valueSplit[2];
            
            if (hour == 12) {
                switch (indicator) {
                    case "AM":
                        indicator = "PM";
                        break;
                    case "PM":
                        indicator = "AM";
                        break;
                }
                hour--;
            } else if (hour == 1) {
                hour = hour + 11;
            } else {
                hour--;
            }
            
            String newHour = (hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour)) + ":" + minutes + " " + indicator;
            cmbHoursStart.setValue(newHour);
        };
        
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("hh:mm a");
        if (startTime != null) {
            cmbHoursStart = createComboHours("De", 9, 23, 1);
            cmbHoursStart.setNullSelectionAllowed(false);
            cmbHoursStart.setTextInputAllowed(false);
            cmbHoursStart.select(df.format(startTime));
            cmbHoursStart.addValueChangeListener(valueChangeListenerHourEnd);
            fields.addComponent(cmbHoursStart);
        }

        if (endTime != null) {
            cmbHoursEnd = createComboHours("A", 9, 23, 0);
            cmbHoursEnd.setNullSelectionAllowed(false);
            cmbHoursEnd.setTextInputAllowed(false);
            cmbHoursEnd.select(df.format(endTime));
            cmbHoursEnd.addValueChangeListener(valueChangeListenerHourStart);
            fields.addComponent(cmbHoursEnd);
        }

        CheckBox allDayField = createCheckBox("Todo el día");
        allDayField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object value = event.getProperty().getValue();
                verCheck(value);
            }
        });
        fields.addComponent(allDayField);

        searchField = new HorizontalLayout();
        searchField.setWidth(100.0f, Unit.PERCENTAGE);
        searchField.setCaption("Paciente");

        btnSearch = new Button();
        btnSearch.setIcon(FontAwesome.SEARCH);
        btnSearch.setEnabled(false);
        btnSearch.addStyleName(ValoTheme.BUTTON_BORDERLESS);

        //LISTENERS AUTOCOMPLETE FIELD
        FieldEvents.TextChangeListener textChangeListener = new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                btnSearch.setVisible((event.getText().length() <= 0));
            }
        };

//        suggestField = new SuggestField();
//        suggestField.setImmediate(true);
//        suggestField.setWidth(100.0f, Unit.PERCENTAGE);
//        suggestField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//        suggestField.setSuggestionHandler(new SuggestField.SuggestionHandler() {
//            @Override
//            public List<Object> searchItems(String query) {
//                System.out.println("Query: " + query);
//                return handleSearchQuery(query);
//            }
//        });
//        suggestField.setSuggestionConverter(new CountrySuggestionConverter());
//        searchField.addComponents(suggestField, btnSearch);
//        searchField.setExpandRatio(suggestField, 1);
//        fields.addComponent(suggestField);
        autoComplete = new AutocompleteTextField();
        autoComplete.setImmediate(true);
        autoComplete.setWidth(100.0f, Unit.PERCENTAGE);
        autoComplete.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        autoComplete.setCache(true);
        autoComplete.setDelay(150);
        autoComplete.setMinChars(3);
        autoComplete.setScrollBehavior(ScrollBehavior.NONE);
        autoComplete.setSuggestionLimit(0);
        autoComplete.setSuggestionProvider(suggestionProvider);
        autoComplete.addTextChangeListener(textChangeListener);
        //ResetButtonForTextField.extend(autoComplete);

        searchField.addComponents(autoComplete, btnSearch);
        searchField.setExpandRatio(autoComplete, 1);
        fields.addComponent(searchField);

        txtMotivo = new TextField("Motivo de Consulta");
        fields.addComponent(txtMotivo);

//        Label lblPru = new Label(
//                MaterialIcon.ACCOUNT_CIRCLE.getHtml() + " "
//                + Feather.HEAD.getHtml() + " "
//                + Ui_Kit.USER_1.getHtml() + " "
//                + Essential.USER_3.getHtml() + " "
//                + Multimedia.AVATAR.getHtml() + " "
//                + Human.USER_36.getHtml(), ContentMode.HTML);
//        lblPru.addStyleName("patientIcon");
        //fields.addComponent(lblPru);
        return fields;
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

    private ComboBox createComboHours(String caption, Integer hourStart, Integer hourEnd, Integer comodin) {
        ComboBox hours = new ComboBox(caption);
        /*SimpleDateFormat df12 = new SimpleDateFormat();
         SimpleDateFormat df24 = new SimpleDateFormat();
         df12.applyPattern("hh:mm a");
         df24.applyPattern("HH:mm");*/
        hourStart = comodin == 0 ? hourStart + 1 : hourStart;
        hourEnd = comodin == 1 ? hourEnd - 1 : hourEnd;
        
        for (int i = hourStart; i <= hourEnd; i++) {
            String h = (i < 10 ? "0" + String.valueOf(i) : i > 12 && i < 22 ? "0" + String.valueOf(i - 12) : i >= 22 ? String.valueOf(i - 12) : String.valueOf(i));
            String type = (i < 12 ? " AM" : i >= 12 ? " PM" : "");
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

    private CheckBox createCheckBox(String caption) {
        CheckBox cb = new CheckBox(caption);
        cb.setImmediate(true);
        return cb;
    }

    private void verCheck(Object value) {
        if (value.equals(true)) {
            cmbHoursStart.setEnabled(false);
            cmbHoursEnd.setEnabled(false);

        } else {
            cmbHoursStart.setEnabled(true);
            cmbHoursEnd.setEnabled(true);
        }
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
