
package com.vaadin.demo.dashboard.component;

import com.vaadin.demo.dashboard.DashboardUtils;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Eduardo
 */
public class PatientMediosContactoForm extends FormLayout{
    
    private final TextField txtEmail;
    //private final TextField txtLadaTel;
    private final TextField txtTelefono;
    private final TextField txtCelular;
    //private final TextField txtDireccion;
    private final TextField txtEstados;
    private final TextField txtDel_Mun;
    private final TextField txtCodPostal;
    
    //private final ComboBox cmbLadaCel;
    private final ComboBox cmbColonia;
    
    private final Label lblSeccion_1;
    private final Label lblSeccion_2;
    
    private final Button btnSearch;
    
    private final DashboardUtils util = new DashboardUtils();

    public PatientMediosContactoForm() {
        addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        
        btnSearch = new Button();
        btnSearch.setIcon(FontAwesome.SEARCH);
        
        lblSeccion_1 = util.createLabelH4("MEDIOS DE CONTACTO");
        txtEmail = util.createTextField("Email");
        //txtLadaTel = util.createTextField("Lada");
        txtTelefono = util.createTextField("Teléfono Fijo");
        //cmbLadaCel = util.createComboLadaCel("Lada");
        txtCelular = util.createTextField("Teléfono Celular");
        
        
        lblSeccion_2 = util.createLabelH4("DIRECCIÓN ACTUAL");
        txtCodPostal = util.createTextField("Cod. Postal");
        //cmbColonia = util.createComboEstados("Colonia");
        txtDel_Mun = util.createTextField("Deleg./Mpo.");
        txtEstados = util.createTextField("Estado");
        //txtDireccion = util.createTextField("Dirección");
        
        HorizontalLayout wrapColonia = new HorizontalLayout();
        wrapColonia.setWidth(100.0f, Unit.PERCENTAGE);
        wrapColonia.setCaption("Colonia");
       
        //Label lblField = new Label("Label");
                
        TextField txtField = new TextField();
        txtField.setWidth(100.0f, Unit.PERCENTAGE);
        txtField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        
        cmbColonia = new ComboBox();
        cmbColonia.setWidth(100.0f, Unit.PERCENTAGE);
        cmbColonia.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        
        wrapColonia.addComponents(cmbColonia,btnSearch);
        //wrapColonia.addComponents(cmbColonia,lblField,txtField);
        wrapColonia.setExpandRatio(cmbColonia, 1);

        addComponents(lblSeccion_1,
                      txtEmail,
                      txtTelefono,
                      txtCelular,
                      lblSeccion_2,
                      txtCodPostal,
                      wrapColonia,
                      txtDel_Mun,
                      txtEstados);
    }
    
}
