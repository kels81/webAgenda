/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.component;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.demo.dashboard.domain.User;
import com.vaadin.demo.dashboard.event.DashboardEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Eduardo
 */
public class PatientPreferencesWindow extends Window {
    
    private final BeanFieldGroup<User> fieldGroup;
    
    private final UploadPicture pic = new UploadPicture();
    private final PatientGeneralForm patientGralForm = new PatientGeneralForm();
    private final PatientMediosContactoForm patientContactoForm = new PatientMediosContactoForm();
    private final PatientAddressForm patientAddressForm = new PatientAddressForm();

    public PatientPreferencesWindow(final User user, final boolean preferencesTabOpen) {
        addStyleName("patient-window");        
        setId("idWndPatient");
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(true);
        setHeight(90.0f, Sizeable.Unit.PERCENTAGE);

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

        detailsWrapper.addComponent(buildGeneralTab());
        detailsWrapper.addComponent(buildContactoTab());

        if (preferencesTabOpen) {
            detailsWrapper.setSelectedTab(0);
        }

        content.addComponent(buildFooter());

        fieldGroup = new BeanFieldGroup<User>(User.class);
        fieldGroup.bindMemberFields(this);
        fieldGroup.setItemDataSource(user);
    }
       
    // [ PRIMER TAB ]
    private Component buildGeneralTab() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("Datos Paciente");
        root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);     //importante
        root.setSpacing(true);
        root.setMargin(true);
        root.addStyleName("profile-form");
          
        root.addComponents(pic, patientGralForm);
        root.setExpandRatio(patientGralForm, 1);
        
        return root;
    }
    
    // [ SEGUNDO TAB ]
    private Component buildContactoTab() {
        VerticalLayout root = new VerticalLayout();
        root.setCaption("Contacto Paciente");
        root.setIcon(FontAwesome.COGS);
        root.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        root.setMargin(true);
        
        root.addComponents(patientContactoForm);

        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);

        Button ok = new Button("OK");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    fieldGroup.commit();
                    // Updated user should also be persisted to database. But
                    // not in this demo.

                    Notification success = new Notification(
                            "Perfil actualizado exitosamente");
                    success.setDelayMsec(2000);
                    success.setStyleName("bar success small");
                    success.setPosition(Position.TOP_CENTER);
                    success.show(Page.getCurrent());

                    DashboardEventBus.post(new DashboardEvent.ProfileUpdatedEvent());
                    close();
                } catch (FieldGroup.CommitException e) {
                    Notification.show("Error while updating profile",
                            Notification.Type.ERROR_MESSAGE);
                }

            }
        });
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    public static void open(final User user, final boolean preferencesTabActive) {
        DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent());
        Window w = new PatientPreferencesWindow(user, preferencesTabActive);
        UI.getCurrent().addWindow(w);
        w.focus();
    }
    
    
}
