/*
 * Copyright Rainer Sulzbach
 */
package io.clownfish.clownfish.beans;

import io.clownfish.clownfish.dbentities.CfDatasource;
import io.clownfish.clownfish.dbentities.CfSitedatasource;
import io.clownfish.clownfish.serviceinterface.CfDatasourceService;
import io.clownfish.clownfish.serviceinterface.CfSitedatasourceService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author sulzbachr
 */
@Named("datasourceList")
@ViewScoped
@Component
public class DatasourceList implements Serializable {
    @Autowired CfDatasourceService cfdatasourceService;
    @Autowired CfSitedatasourceService cfsitedatasourceService;
    
    private @Getter @Setter List<CfDatasource> datasourcelist = null;
    private @Getter @Setter CfDatasource selectedDatasource = null;
    private @Getter @Setter String datasourceName;
    private @Getter @Setter String datasourceServer;
    private @Getter @Setter String datasourceURL;
    private @Getter @Setter int datasourcePort;
    private @Getter @Setter String datasourceDatabasename;
    private @Getter @Setter String datasourceUser;
    private @Getter @Setter String datasourcePassword;
    private @Getter @Setter String datasourceDriverclass;
    private @Getter @Setter boolean newContentButtonDisabled = false;

    @PostConstruct
    public void init() {
        datasourcelist = cfdatasourceService.findAll();
        newContentButtonDisabled = false;
    }
    
    public void onSelect(SelectEvent event) {
        selectedDatasource = (CfDatasource) event.getObject();
        
        datasourceName = selectedDatasource.getName();
        datasourceDatabasename = selectedDatasource.getDatabasename();
        datasourceDriverclass = selectedDatasource.getDriverclass();
        datasourcePassword = selectedDatasource.getPassword();
        datasourcePort = selectedDatasource.getPort();
        datasourceServer = selectedDatasource.getServer();
        datasourceURL = selectedDatasource.getUrl();
        datasourceUser = selectedDatasource.getUser();
        
        newContentButtonDisabled = true;
    }
    
    public void onCreateContent(ActionEvent actionEvent) {
        try {
            CfDatasource newdatasourcecontent = new CfDatasource();
            newdatasourcecontent.setName(datasourceName);
            newdatasourcecontent.setDatabasename(datasourceDatabasename);
            newdatasourcecontent.setDriverclass(datasourceDriverclass);
            newdatasourcecontent.setPassword(datasourcePassword);
            newdatasourcecontent.setPort(datasourcePort);
            newdatasourcecontent.setServer(datasourceServer);
            newdatasourcecontent.setUrl(datasourceURL);
            newdatasourcecontent.setUser(datasourceUser);
            
            cfdatasourceService.create(newdatasourcecontent);
            datasourcelist = cfdatasourceService.findAll();
        } catch (ConstraintViolationException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void onEditContent(ActionEvent actionEvent) {
        try {
            if (selectedDatasource != null) {
                selectedDatasource.setName(datasourceName);
                selectedDatasource.setDatabasename(datasourceDatabasename);
                selectedDatasource.setDriverclass(datasourceDriverclass);
                selectedDatasource.setPassword(datasourcePassword);
                selectedDatasource.setPort(datasourcePort);
                selectedDatasource.setServer(datasourceServer);
                selectedDatasource.setUrl(datasourceURL);
                selectedDatasource.setUser(datasourceUser);
                cfdatasourceService.edit(selectedDatasource);
                datasourcelist = cfdatasourceService.findAll();
            }
        } catch (ConstraintViolationException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void onDeleteContent(ActionEvent actionEvent) {
        if (selectedDatasource != null) {
            List<CfSitedatasource> sitedatasourcelist = cfsitedatasourceService.findByDatasourceref(selectedDatasource.getId());
            for (CfSitedatasource sitedatasource : sitedatasourcelist) {
                cfsitedatasourceService.delete(sitedatasource);
            }
            cfdatasourceService.delete(selectedDatasource);
            datasourcelist = cfdatasourceService.findAll();
            
        }
    }
    
    public void onChangeName(ValueChangeEvent changeEvent) {
        try {
            CfDatasource validateList = cfdatasourceService.findByName(datasourceName);
            newContentButtonDisabled = true;
        } catch (NoResultException ex) {
            newContentButtonDisabled = datasourceName.isEmpty();
        }
    }
}
