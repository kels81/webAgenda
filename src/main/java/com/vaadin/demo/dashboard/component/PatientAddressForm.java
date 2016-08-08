
package com.vaadin.demo.dashboard.component;

import com.vaadin.demo.dashboard.DashboardUtils;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Eduardo
 */
public class PatientAddressForm extends FormLayout {
    
    private final TextField txtDireccion;
    private final TextField txtEstados;
    private final TextField txtDel_Mun;
    private final TextField txtColonia;
    private final TextField txtCodPostal;
    
    private final Label lblSeccion;
    
    private final DashboardUtils util = new DashboardUtils();

    public PatientAddressForm() {
        addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        
        lblSeccion = util.createLabelH4("DIRECCIÓN ACTUAL");
        txtDireccion = util.createTextField("Dirección");
        txtEstados = util.createTextField("Estado");
        txtDel_Mun = util.createTextField("Deleg./Mpo.");
        txtColonia = util.createTextField("Colonia");
        txtColonia.setIcon(FontAwesome.SEARCH);
        //txtColonia.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        txtCodPostal = util.createTextField("Cod. Postal");
        
        addComponents(txtDireccion,
                      txtEstados,
                      txtDel_Mun,
                      txtColonia,
                      txtCodPostal);
    }
    
}
