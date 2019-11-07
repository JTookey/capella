/*******************************************************************************
 * Copyright (c) 2006, 2019 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.test.diagram.common.ju.step.tools.xab;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.core.data.cs.Component;
import org.polarsys.capella.core.data.cs.Part;
import org.polarsys.capella.test.diagram.common.ju.context.DiagramContext;
import org.polarsys.capella.test.diagram.common.ju.step.tools.SelectFromListTool;

/**
 * In this tool, we don't test that the selected component in wizard has a view (which is not the case), we test that
 * the tool has created a part that is displayed in the diagram
 */
public class ReuseComponentTool extends SelectFromListTool {
  protected Map<Component, List<Part>> representingParts;

  public ReuseComponentTool(DiagramContext context, String toolName, String containerId) {
    // We don't test insertedViews as a new part is created
    super(context, toolName, containerId, new String[0]);
  }

  @Override
  protected void preRunTest() {
    super.preRunTest();
    representingParts = new HashMap<Component, List<Part>>();
    for (EObject component : getDiagramContext().getSessionContext().getSemanticElements(selectedElements)) {
      if (component instanceof Component) {
        representingParts.put((Component) component, ((Component) component).getRepresentingParts());
      }
    }
  }

  @Override
  protected void postRunTest() {
    super.postRunTest();

    for (EObject component : getDiagramContext().getSessionContext().getSemanticElements(selectedElements)) {
      if (component instanceof Component) {
        List<Part> parts = ((Component) component).getRepresentingParts();
        getDiagramContext().hasView(parts.get(parts.size() - 1).getId());

        assertTrue("A new part referencing " + ((Component) component).getId() + " should have been created",
            parts.size() == representingParts.get(component).size() + 1);
      }
    }
  }
}
