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
package org.polarsys.capella.core.explorer.activity.ui.hyperlinkadapter.sa;

import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.core.data.capellamodeller.Project;
import org.polarsys.capella.core.explorer.activity.ui.hyperlinkadapter.AbstractCapellaNewDiagramHyperlinkAdapter;
import org.polarsys.capella.core.model.helpers.ModelQueryHelper;
import org.polarsys.capella.core.sirius.analysis.IDiagramNameConstants;

/**
 * Create a System Functions / Operational Activities Traceability Matrix
 */
public class NewSystemFunctionsOperationalActivitiesMatrixAdapter extends AbstractCapellaNewDiagramHyperlinkAdapter {

	public NewSystemFunctionsOperationalActivitiesMatrixAdapter() {
		super();
	}

	@Override
	public String getRepresentationName() {
		return IDiagramNameConstants.SYSTEM_FUNCTIONS_OPERATIONAL_ACTIVITIES_DIAGRAM_NAME;
	}

	@Override
	protected ModelElement getModelElement(EObject rootSemanticModel) {
		return ModelQueryHelper.getSystemAnalysis((Project) rootSemanticModel);
	}
}
