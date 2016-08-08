/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.demo.dashboard.component;

import com.google.gwt.resources.client.ImageResource;
import com.vaadin.addon.onoffswitch.OnOffSwitch;
import com.vaadin.data.Property;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.teemu.switchui.Switch;

/**
 *
 * @author Eduardo
 */
public class UploadPicture extends VerticalLayout {

    private final Image profilePic;
    private final OnOffSwitch onOffswitch;
    //private final Switch swtch;
    private final Button upload;
    private final Label lblAct; 
            
    public UploadPicture() {
        setSizeUndefined();
        setSpacing(true);
        profilePic = new Image(null, new ThemeResource(
                "img/profile-pic-300px.jpg"));
        profilePic.setWidth(150.0f, Sizeable.Unit.PIXELS);
        addComponent(profilePic);

        upload = new Button("Cambiar", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show("Not implemented in this demo");
            }
        });
        upload.addStyleName(ValoTheme.BUTTON_TINY);
        
        HorizontalLayout hor = new HorizontalLayout();
        hor.setSpacing(true);
        onOffswitch = new OnOffSwitch(true);
        onOffswitch.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Object checked = event.getProperty().getValue();
                if (checked.equals(true)) {
                    lblAct.setCaption("Activo");
                    lblAct.removeStyleName("red-label");
                    lblAct.addStyleName("green-label");
                } else {
                    lblAct.setCaption("Inactivo");
                    lblAct.removeStyleName("green-label");
                    lblAct.addStyleName("red-label");
                }
                    
            }
        });
        lblAct = new Label();
        lblAct.setCaption("Activo");
        lblAct.addStyleName("green-label");
        hor.addComponents(onOffswitch, lblAct);
        
        //swtch = new Switch();
        //swtch.addStyleName("compact");
        //swtch.setImmediate(true);
        
        addComponents(upload,hor);
    }
    
}
