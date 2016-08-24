/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.component;

import com.vaadin.data.Property;
import com.vaadin.demo.dashboard.DashboardUtils;
import com.vaadin.demo.dashboard.view.message.Human;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteQuery;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Eduardo
 */
public class AppointmentForm extends FormLayout {

    private final Label lblDate;
    private final CheckBox allDayField ;
    private final HorizontalLayout searchField;
    private Button btnSearch;
    private final TextField txtMotivo;
    private final AutocompleteTextField autoComplete;                         //PRUEBAS AUTOCOMPLETE
    private ComboBox cmbHoursStart;
    private ComboBox cmbHoursEnd;
    
    private final Property.ValueChangeListener valueChangeListenerHourStart;
    private final Property.ValueChangeListener valueChangeListenerHourEnd;
    private final FieldEvents.TextChangeListener textChangeListener;
    
    private final DashboardUtils util = new DashboardUtils();
    
    public AppointmentForm(Date startTime, Date endTime) {
    addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
    
    SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("hh:mm a");
        
        lblDate = util.createLabelH4(util.setUpperMonth(startTime));
        
        if (startTime != null) {
            cmbHoursStart = util.createComboHours("De", 9, 23, 1);
            cmbHoursStart.select(df.format(startTime));
            //cmbHoursStart.addValueChangeListener(valueChangeListenerHourEnd);
        }

        if (endTime != null) {
            cmbHoursEnd = util.createComboHours("A", 9, 23, 0);
            cmbHoursEnd.select(df.format(endTime));
            //cmbHoursEnd.addValueChangeListener(valueChangeListenerHourStart);
        }
        
        allDayField = util.createCheckBox("Todo el día");
        allDayField.addValueChangeListener((Property.ValueChangeEvent event) -> {
            Object value = event.getProperty().getValue();
            verCheck(value);
    });
        
        searchField = new HorizontalLayout();
        searchField.setWidth(100.0f, Unit.PERCENTAGE);
        searchField.setCaption("Paciente");

        btnSearch = new Button();
        btnSearch.setIcon(FontAwesome.SEARCH);
        btnSearch.setEnabled(false);
        btnSearch.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        
        autoComplete = util.createAutocompleteTextField();
        autoComplete.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        autoComplete.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        autoComplete.setSuggestionProvider(suggestionProvider);
        //autoComplete.addTextChangeListener(textChangeListener);
        //ResetButtonForTextField.extend(autoComplete);

        searchField.addComponents(autoComplete, btnSearch);
        searchField.setExpandRatio(autoComplete, 1);

        txtMotivo = util.createTextField("Motivo de Consulta");
        
        
        /** [ LISTENER O EVENTS ] **/
        
        
        valueChangeListenerHourEnd = (Property.ValueChangeEvent event) -> {
            String value = event.getProperty().getValue().toString().replaceAll("\\s", ":");
            String[] valueSplit = value.split(":");
            int hour = Integer.parseInt(valueSplit[0]);
            String minutes = valueSplit[1];
            String indicator = valueSplit[2];
            
        switch (hour) {
            case 11:
                switch (indicator) {
                    case "AM":
                        indicator = "PM";
                        break;
                    case "PM":
                        indicator = "AM";
                        break;
                }
                hour++;
                break;
            case 12:
                hour = hour - 11;
                break;
            default:
                hour++;
                break;
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
            
        switch (hour) {
            case 12:
                switch (indicator) {
                    case "AM":
                        indicator = "PM";
                        break;
                    case "PM":
                        indicator = "AM";
                        break;
                }
                hour--;
                break;
            case 1:
                hour = hour + 11;
                break;
            default:
                hour--;
                break;
        }
            
            String newHour = (hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour)) + ":" + minutes + " " + indicator;
            cmbHoursStart.setValue(newHour);
        };
        
        textChangeListener = (FieldEvents.TextChangeEvent event) -> {
            btnSearch.setVisible((event.getText().length() <= 0));
        };
        
        addComponents(lblDate,
                      cmbHoursStart,
                      cmbHoursEnd,
                      allDayField,
                      searchField,
                      txtMotivo);
    
    }
    
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
    
    private void verCheck(Object value) {
        if (value.equals(true)) {
            cmbHoursStart.setEnabled(false);
            cmbHoursEnd.setEnabled(false);

        } else {
            cmbHoursStart.setEnabled(true);
            cmbHoursEnd.setEnabled(true);
        }
    }
    
    private final CollectionSuggestionProvider suggestionProvider
            = new CollectionSuggestionProvider(NAMES, MatchMode.CONTAINS, true) {
                @Override
                public Collection<AutocompleteSuggestion> querySuggestions(AutocompleteQuery query) {
                    Collection<AutocompleteSuggestion> suggestions = super.querySuggestions(query);

                    int i = 0;
                    for (AutocompleteSuggestion suggestion : suggestions) {
                        //suggestion.setDescription("This is a description for "
                        //+ suggestion.getValue() + " ...");
                        suggestion.setIcon(Human.USER_36);
                        suggestion.addStyleName("patientIcon");

                    }
                    return suggestions;
                }

            };
    
    
    
}
