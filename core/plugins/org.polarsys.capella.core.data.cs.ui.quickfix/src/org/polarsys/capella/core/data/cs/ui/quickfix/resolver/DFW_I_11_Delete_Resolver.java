/*******************************************************************************
 * Copyright (c) 2006, 2020 THALES GLOBAL SERVICES.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.core.data.cs.ui.quickfix.resolver;

import org.polarsys.capella.core.data.cs.ExchangeItemAllocation;
import org.polarsys.capella.core.validation.ui.ide.quickfix.AbstractDeleteCommandResolver;

/**
 * Delete given element
 */
public class DFW_I_11_Delete_Resolver extends AbstractDeleteCommandResolver {

 
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Object getElementToDelete(Object obj) {
    if (obj instanceof ExchangeItemAllocation) {
      return obj;
    }
    return null;
  }
  

}
