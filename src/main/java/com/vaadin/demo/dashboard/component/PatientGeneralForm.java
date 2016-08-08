
package com.vaadin.demo.dashboard.component;

import com.vaadin.demo.dashboard.DashboardUtils;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Eduardo
 */
public class PatientGeneralForm extends FormLayout {
    
    private final TextField txtNombre;
    private final TextField txtApPaterno;
    private final TextField txtApmaterno;
//    private final TextField txtEmail;
//    private final TextField txtTelefono;
//    private final TextField txtCelular;
    private final TextField txtCURP;
//    private TextField txtNombreCom;
//    private TextField txtTelefonoMed;
//    private TextField txtNombreParent;
//    private TextField txtTelefonoParent;
//    private TextField txtFechaProceso;
//    private TextField txtContacto;
//    private TextField txtMotConsulta;

//    private TextArea txAEnfermedades;
//    private TextArea txAMedicamento;

//    private final ComboBox cmbProfesion;
    private final ComboBox cmbEstados;
//    private final ComboBox cmbEdoCivil;
//    private final ComboBox cmbReligion;
//    private ComboBox cmbParentesco;
    private final ComboBox cmbGenero;

    private final PopupDateField txtFechNac;
    
    private final Label lblSeccion;
    
    private final DashboardUtils util = new DashboardUtils();

    public PatientGeneralForm() {
        addStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        FieldEvents.FocusListener focusListener = new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent event) {
                event.getComponent().addStyleName("blue-caption");
            }
        };

        FieldEvents.BlurListener blurListener = new FieldEvents.BlurListener() {
            @Override
            public void blur(FieldEvents.BlurEvent event) {
                event.getComponent().removeStyleName("blue-caption");
            }
        };

        lblSeccion = util.createLabelH4("INFORMACI”N GENERAL");
        txtNombre = util.createTextField("Nombre(s)");
        txtNombre.addFocusListener(focusListener);
        txtNombre.addBlurListener(blurListener);
        txtApPaterno = util.createTextField("Apellido Paterno");
        txtApmaterno = util.createTextField("Apellido Materno");
        txtFechNac = util.createDateFieldNac("Fecha Nacimiento");
        cmbGenero = util.createComboGenero("GÈnero");
        cmbEstados = util.createComboEstados("Lugar Nacimiento");
        txtCURP = util.createTextField("CURP");

        addComponents(lblSeccion,
                      txtNombre,
                      txtApPaterno,
                      txtApmaterno,
                      txtFechNac,
                      cmbGenero,
                      cmbEstados,
                      txtCURP);

//        //CREACION FORMULARIO 2
//        cmbProfesion = util.createComboProfesion("Profesi√≥n");
//        cmbEdoCivil = util.createComboEdoCivil("Estado Civil");
//        cmbReligion = util.createComboReligion("Religi√≥n");
//        txtEmail = util.createTextField("Email");
//        txtTelefono = util.createTextField("Tel√©fono");
//        txtCelular = util.createTextField("Tel. Celular");
//
//        form2.addComponent(cmbProfesion);
//        form2.addComponent(cmbEdoCivil);
//        form2.addComponent(cmbReligion);
//        form2.addComponent(txtEmail);
//        form2.addComponent(txtTelefono);
//        form2.addComponent(txtCelular);
    
    }
    
}
