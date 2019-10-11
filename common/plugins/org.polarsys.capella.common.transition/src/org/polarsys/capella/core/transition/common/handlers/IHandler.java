/*******************************************************************************
 * Copyright (c) 2006, 2016 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.core.transition.common.handlers;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.polarsys.kitalpha.transposer.rules.handler.rules.api.IContext;

public interface IHandler {

  default IStatus init(IContext context) {
    return new Status(IStatus.OK, getClass().getCanonicalName(), "init ok"); //$NON-NLS-1$
  }

  default IStatus dispose(IContext context) {
    return Status.OK_STATUS;
  }

}
