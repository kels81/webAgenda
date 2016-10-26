/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard;

import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Eduardo
 */
public class DashboardUtils {

    public CheckBox createCheckBox(String caption) {
        CheckBox cb = new CheckBox(caption);
        cb.setImmediate(true);
        return cb;
    }

    public TextField createTextField(String caption) {
        TextField f = new TextField(caption);
        f.setNullRepresentation("");
        f.addFocusListener(focusListener);
        f.addBlurListener(blurListener);
        return f;
    }
    
    public AutocompleteTextField createAutocompleteTextField() {
        AutocompleteTextField f = new AutocompleteTextField();
        f.setNullRepresentation("");
        f.setImmediate(true);
        f.setCache(true);
        f.setDelay(150);
        f.setMinChars(3);
        f.setScrollBehavior(ScrollBehavior.NONE);
        f.setSuggestionLimit(0);
        return f;
    }

    public TextArea createTextArea(String caption) {
        TextArea f = new TextArea(caption);
        f.setNullRepresentation("");
        f.setRows(3);
        f.addFocusListener(focusListener);
        f.addBlurListener(blurListener);
        return f;
    }

    public PopupDateField createDateFieldNac(String caption) {
        PopupDateField f = new PopupDateField(caption);
        f.setDateFormat("dd MMM yyyy");
        f.setRangeEnd(new Date());
        f.setTextFieldEnabled(false);
        return f;
    }

    public PopupDateField createDateField(String caption) {
        PopupDateField f = new PopupDateField(caption);
        f.setDateFormat("dd MMM yyyy");
        f.setTextFieldEnabled(false);
        return f;
    }

    public OptionGroup createRadioGenero(String caption) {
        OptionGroup f = new OptionGroup(caption);
        f.addItem(Boolean.FALSE);
        f.setItemCaption(Boolean.FALSE, "Femenino");
        f.addItem(Boolean.TRUE);
        f.setItemCaption(Boolean.TRUE, "Masculino");
        //f.addStyleName("horizontal");
        return f;
    }

    public Label createLabelH4(String caption) {
        Label lbl = new Label(caption);
        lbl.addStyleName(ValoTheme.LABEL_H4);
        lbl.addStyleName(ValoTheme.LABEL_COLORED);
        return lbl;
    }

    public ComboBox createComboEstados(String caption) {
        ComboBox cmb = new ComboBox(caption);
        //cmb.setInputPrompt("Seleccione Estado");
        cmb.setNullSelectionAllowed(false);
        cmb.addFocusListener(focusListener);
        cmb.addBlurListener(blurListener);
        cmb.addItems("Aguascalientes", "Baja California", "Baja California Sur", "Campeche",
                "Chiapas", "Chihuahua", "Coahuila", "Colima", "Distrito Federal", "Durango",
                "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "M�xico", "Michoac�n", "Morelos",
                "Nayarit", "Nuevo Le�n", "Oaxaca", "Puebla", "Quer�taro", "Quintana Roo", "San Luis Potos�",
                "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucat�n", "Zacatecas");
        return cmb;
    }

    public ComboBox createComboParentesco(String caption) {
        ComboBox cmb = new ComboBox(caption);
        //cmb.setInputPrompt("Seleccione Parentesco");
        cmb.setNullSelectionAllowed(false);
        cmb.addFocusListener(focusListener);
        cmb.addBlurListener(blurListener);
        cmb.addItems("Mama", "Papa", "Hijo(a)", "Hermano(a)", "T�o(a)", "Abuelo(a)", "Nieto(a)", "Esposo(a)",
                "Primo(a)", "Cu�ado(a)", "Nuera", "Yerno", "Otro", "No Tiene");
        return cmb;
    }

    public ComboBox createComboEdoCivil(String caption) {
        ComboBox cmb = new ComboBox(caption);
        //cmb.setInputPrompt("Seleccione Estado Civil");
        cmb.setNullSelectionAllowed(false);
        cmb.addFocusListener(focusListener);
        cmb.addBlurListener(blurListener);
        cmb.addItems("Soltero(a)", "Casado(a)", "Viudo(a)", "Divorciado(a)", "Uni�n Libre", "Concubinato");
        return cmb;
    }

    public ComboBox createComboLadaCel(String caption) {
        ComboBox cmb = new ComboBox(caption);
        //cmb.setInputPrompt("Seleccione Estado Civil");
        cmb.setNullSelectionAllowed(false);
        cmb.addFocusListener(focusListener);
        cmb.addBlurListener(blurListener);
        cmb.addItems("044", "045");
        return cmb;
    }

    public ComboBox createComboGenero(String caption) {
        ComboBox cmb = new ComboBox(caption);
        //cmb.setInputPrompt("Seleccione Género");
        cmb.setNullSelectionAllowed(false);
        cmb.addFocusListener(focusListener);
        cmb.addBlurListener(blurListener);
        cmb.addItems("Femenino", "Masculino");
        return cmb;
    }

    public ComboBox createComboReligion(String caption) {
        ComboBox cmb = new ComboBox(caption);
        //cmb.setInputPrompt("Seleccione Religi�n");
        cmb.setNullSelectionAllowed(false);
        cmb.addFocusListener(focusListener);
        cmb.addBlurListener(blurListener);
        cmb.addItems("Cat�lica", "Protestante", "Evang�lica", "Testigos de Jehov�", "Adventista", "Morm�n", "Juda�smo", "Isl�mica");
        return cmb;
    }

    public ComboBox createComboProfesion(String caption) {
        ComboBox cmb = new ComboBox(caption);
        //cmb.setInputPrompt("Seleccione Profesi�n");
        cmb.setNullSelectionAllowed(false);
        cmb.addFocusListener(focusListener);
        cmb.addBlurListener(blurListener);
        cmb.addItems("Administrativos", "Biolog�a", "Comunicaciones", "Construcci�n", "Contabilidad",
                "Creatividad", "Producci�n", "Dise�o Comercial", "Derecho y Leyes", "Educaci�n",
                "Ingenier�a", "Log�stica", "Transportaci�n y Distribuci�n", "Manufactura", "Produci�n y Operaci�n",
                "Mercadotecnia", "Publicidad y Relaciones P�blicas", "Recursos Humanos", "Salud y Belleza",
                "Sector Salud", "Seguro y Reaseguro", "Tecnolog�a de la Informaci�n / Sistemas", "Turismo", "Hospitalidad y Gastronom�a",
                "Ventas", "Veterinaria / Zoologia");
        return cmb;
    }

    public ComboBox createComboProfSalud(String caption) {
        ComboBox cmb = new ComboBox(caption);
        cmb.setNullSelectionAllowed(false);
        cmb.addFocusListener(focusListener);
        cmb.addBlurListener(blurListener);
        cmb.addItems("M�dico", "Psic�logo", "Nutri�logo");
        return cmb;
    }

    public ComboBox createComboHours(String caption, Integer hourStart, Integer hourEnd, Integer comodin) {
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

        hours.addFocusListener(focusListener);
        hours.addBlurListener(blurListener);
        hours.setNullSelectionAllowed(false);
        hours.setTextInputAllowed(false);

        return hours;
    }

    public String getMonth() {
        String month = "";
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        month = sdf.format(today);
        return month;
    }

    public String getToday() {
        String day = "";
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        day = sdf.format(today);
        return day;
    }
    
    public String setUpperMonth(Date date) {
        String upperMonth = "";
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("dd MMMM yyyy");
        //Obteniendo mes de la fecha
        upperMonth = df.format(date).substring(df.format(date).indexOf(" ") + 1, df.format(date).length() - 4);
        //Primera letra del mes en Mayuscula
        upperMonth = df.format(date).replaceFirst(upperMonth.substring(0, 1), upperMonth.substring(0, 1).toUpperCase());

        return upperMonth;
    }

    FieldEvents.FocusListener focusListener = new FieldEvents.FocusListener() {

        public void focus(FieldEvents.FocusEvent event) {
            event.getComponent().addStyleName("blue-caption");
        }
    };

    FieldEvents.BlurListener blurListener = new FieldEvents.BlurListener() {

        public void blur(FieldEvents.BlurEvent event) {
            event.getComponent().removeStyleName("blue-caption");
        }
    };
    
    
    

}
