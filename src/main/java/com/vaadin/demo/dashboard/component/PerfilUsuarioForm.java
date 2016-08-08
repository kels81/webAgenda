
package com.vaadin.demo.dashboard.component;

import com.vaadin.demo.dashboard.DashboardUtils;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Eduardo
 */
public class PerfilUsuarioForm extends FormLayout {
    
    private final Label lblSeccion;
    private final TextField txtUsuario;
    private final TextField txtEmail;
    private final TextField txtPassword;
    private final TextField txtRepPassword;
    private final ComboBox cmbRol;
    
    private final DashboardUtils util = new DashboardUtils();
    
    public PerfilUsuarioForm() {
        addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        lblSeccion = util.createLabelH4("Datos Usuario");
        txtUsuario = new TextField("Usuario");
        txtEmail = new TextField("Email");
        txtPassword = new TextField("Password");
        txtRepPassword = new TextField("Repetir Password");
        cmbRol = util.createComboProfSalud("Especialidad");
        cmbRol.setValue("Psicólogo");
        
        addComponents(lblSeccion,
                      txtUsuario,
                      txtEmail,
                      txtPassword,
                      txtRepPassword,
                      cmbRol);
    }

}
