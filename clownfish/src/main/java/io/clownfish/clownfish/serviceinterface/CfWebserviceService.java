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
package io.clownfish.clownfish.serviceinterface;

import io.clownfish.clownfish.dbentities.CfWebservice;
import java.util.List;

/**
 *
 * @author sulzbachr
 */
public interface CfWebserviceService {
    CfWebservice findById(Long id);
    CfWebservice findByName(String name);
    List<CfWebservice> findAll();
    CfWebservice create(CfWebservice entity);
    boolean delete(CfWebservice entity);
    CfWebservice edit(CfWebservice entity);
}
