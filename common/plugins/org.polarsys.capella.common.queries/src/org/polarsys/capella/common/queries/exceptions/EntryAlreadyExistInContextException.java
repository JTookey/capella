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
package org.polarsys.capella.common.queries.exceptions;

public class EntryAlreadyExistInContextException extends QueryException {

  public EntryAlreadyExistInContextException(String entryName) {
    super("Entry " + entryName + " already exist in the context. Use overwrite(...) instead of setValue(...).");
  }
}
