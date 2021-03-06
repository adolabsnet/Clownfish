/*
 * Copyright 2019 sulzbachr.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.clownfish.clownfish.serviceimpl;

import io.clownfish.clownfish.daointerface.CfWebserviceDAO;
import io.clownfish.clownfish.dbentities.CfWebservice;
import io.clownfish.clownfish.serviceinterface.CfWebserviceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sulzbachr
 */
@Service
@Transactional
public class CfWebserviceServiceImpl implements CfWebserviceService {
    private final CfWebserviceDAO cfwebserviceDAO;
    
    @Autowired
    public CfWebserviceServiceImpl(CfWebserviceDAO cfwebserviceDAO) {
        this.cfwebserviceDAO = cfwebserviceDAO;
    }

    @Override
    public CfWebservice findById(Long id) {
        return cfwebserviceDAO.findById(id);
    }

    @Override
    public CfWebservice findByName(String name) {
        return cfwebserviceDAO.findByName(name);
    }

    @Override
    public CfWebservice create(CfWebservice entity) {
        return cfwebserviceDAO.create(entity);
    }

    @Override
    public boolean delete(CfWebservice entity) {
        return cfwebserviceDAO.delete(entity);
    }

    @Override
    public CfWebservice edit(CfWebservice entity) {
        return cfwebserviceDAO.edit(entity);
    }

    @Override
    public List<CfWebservice> findAll() {
        return cfwebserviceDAO.findAll();
    }
    
    
}
